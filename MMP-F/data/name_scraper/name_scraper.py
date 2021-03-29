# pylint: disable=missing-docstring
import urllib.error
import urllib.parse
import urllib.request
import json
from collections import OrderedDict, defaultdict
import re
from math import ceil
from random import randint
import unicodedata
from bs4 import BeautifulSoup


# http://www.chessgames.com/alpha3.html

class OrderedDefaultDict(OrderedDict):
    def __missing__(self, key):
        self[key] = value = 0
        return value


class OrderedDefaultListDict(OrderedDict):
    def __missing__(self, key):
        self[key] = value = []
        return value


class OrderedDefaultFourElementListDict(OrderedDict):
    def __missing__(self, key):
        self[key] = value = [0, 0, 0, 0]
        return value


country_code_rewrite_rules = {
    "SCO": "GBR",
    "ENG": "GBR",
    "WLS": "GBR",
    "AGR": "ALG",
    "AGL": "ALG",
    "BAD": "BAR",
    "DAY": "BEN",
    "DAH": "BEN",
    "BSH": "BIH",
    "HBR": "BIZ",
    "BLZ": "BIZ",
    "VOL": "BUR",
    "AFC": "CAF",
    "CAB": "CAM",
    "KHM": "CAM",
    "CHD": "CHA",
    "CIL": "CHI",
    "PRC": "CHN",
    "IVC": "CIV",
    "CML": "CIV",
    # "COK": "COD", Also Cook Islands
    "ZAI": "COD",
    "COS": "CRC",
    "TCH": "CZE",
    "DAN": "DEN",
    "DIN": "DEN",
    "RAU": "EGY", # Shared with Syria
    "UAR": "EGY",
    "SAL": "ESA",
    "SPA": "ESP",
    "ETI": "ETH",
    "FIG": "FIJ",
    "GRB": "GBR",
    "GBI": "GBR",
    "ALL": "GER",
    "ALE": "GER",
    "GUT": "GUA",
    # "GUA": "GUY", Also Guatemala
    # "GUI": "GUY", Also Guinea
    "HOK": "HKG",
    "UNG": "HUN",
    "INS": "INA",
    "IRN": "IRI",
    "IRA": "IRI",
    "IRK": "IRQ",
    "ICE": "ISL",
    "GIA": "JPN",
    "JAP": "JPN",
    "COR": "KOR",
    "ARS": "KSA",
    "SAU": "KSA",
    "LYA": "LBA",
    "LBY": "LBA",
    "LEB": "LBN",
    "LIB": "LBN",
    "LIC": "LIE",
    "LIT": "LTU",
    "MAG": "MAD",
    "MRC": "MAR",
    "MAL": "MAS",
    "MLD": "MDA",
    #"MON": "MGL", Also Monaco
    "MAT": "MLT",
    "BIR": "MYA",
    # "BUR": "MYA", Also Burkina Faso
    "NCG": "NCA",
    "NIC": "NCA",
    "OLA": "NED",
    "NET": "NED",
    "PBA": "NED",
    "NLD": "NED",
    "HOL": "NED",
    "NGA": "NGR",
    # "NIG": "NGR", Also Niger
    # "NGR": "NIG, Also Nigeria
    "NZE": "NZL",
    "FIL": "PHI",
    "NGY": "PNG",
    "NGU": "PNG",
    "NKO": "PRK",
    "CDN": "PRK",
    "PRI": "PUR",
    "PRO": "PUR",
    "ROM": "ROU",
    "RUM": "ROU",
    "SAF": "RSA",
    "OAR": "RUS",
    "SGL": "SEN",
    "SIN": "SGP",
    "SLA": "SLE",
    "SMA": "SMR",
    "CEY": "SRI",
    "CEI": "SRI",
    "SVI": "SUI",
    "SWI": "SUI",
    "SVE": "SWE",
    "SUE": "SWE",
    "TON": "TGA",
    "RCF": "TWN",
    "TPE": "TWN",
    "ROC": "TWN",
    "TRT": "TTO",
    "TRI": "TTO",
    "URG": "URU",
    "VET": "VIE",
    "VNM": "VIE",
    "NRH": "ZAM",
    "RHO": "ZIM",

    "BOH": "CZE",
    "FRG": "GER",
    "GDR": "GER",
    "ADE": "GER",
    "SAA": "GER",
    "CSR": "CZE",
    "CSL": "CZE",
    "CSV": "CZE",
    "CZS": "CZE",
    "CHE": "CZE",
    "SOV": "URS",
    "JUG": "YUG",
    "YUS": "YUG",
    "MNC": "MON",
    "SCG": "SRB"
}


def rewrite_country_code(code):
    try:
        return country_code_rewrite_rules[code]
    except KeyError:
        return code


def final_fix(name, first):
    name = name.title()
    name = name.replace("De La", "de la")
    name = re.sub("Do ([A-Za-z]+)", "do \g<1>", name)
    name = re.sub("De ([A-Za-z]+)", "de \g<1>", name)
    name = re.sub("Da ([A-Za-z]+)", "da \g<1>", name)
    name = re.sub("Di ([A-Za-z]+)", "di \g<1>", name)
    name = re.sub("Dos ([A-Za-z]+)", "dos \g<1>", name)
    if not first:
        name = name.replace("Van ", "van ")
        name = name.replace("Von ", "von ")
        name = name.replace("Den ", "den ")
        name = name.replace("Der ", "der ")
    name = re.sub(" Jr$", " Jr.", name)
    name = re.sub(" Sr$", " Sr.", name)
    name = re.sub("^-", "", name)
    name = re.sub("-$", "", name)
    name = re.sub(" ?- ?", "-", name)
    name.strip()
    if first:
        name = "{}{}".format(name[0].upper(), name[1:])
    return name


# BEHOLD THE HORROR
def generate_names():
    country_codes_with_num_to_gen = OrderedDefaultDict()
    country_codes_with_counts = OrderedDefaultFourElementListDict()
    female_last_by_country = OrderedDefaultListDict()
    female_first_by_country = OrderedDefaultListDict()
    male_last_by_country = OrderedDefaultListDict()
    male_first_by_country = OrderedDefaultListDict()
    print("Reading out_female_last.txt\n")
    with open("out_female_last.txt", "r") as in_female_last:
        current_country_code = "<QUALITY ERROR REPORT>"
        for line in in_female_last:
            if line[0] == "-":
                current_country_code = line[1:4]
                country_codes_with_counts[current_country_code][3] = int(line[5:])
            else:
                count, name = line.split('|')
                female_last_by_country[current_country_code].append([count, name.strip('\n')])
    print("Reading out_female_first.txt\n")
    with open("out_female_first.txt", "r") as in_female_first:
        current_country_code = "<QUALITY ERROR REPORT>"
        for line in in_female_first:
            if line[0] == "-":
                current_country_code = line[1:4]
                country_codes_with_counts[current_country_code][2] = int(line[5:])
            else:
                count, name = line.split('|')
                female_first_by_country[current_country_code].append([count, name.strip('\n')])
    print("Reading out_male_last.txt\n")
    with open("out_male_last.txt", "r") as in_male_last:
        current_country_code = "<QUALITY ERROR REPORT>"
        for line in in_male_last:
            if line[0] == "-":
                current_country_code = line[1:4]
                country_codes_with_counts[current_country_code][1] = int(line[5:])
            else:
                count, name = line.split('|')
                male_last_by_country[current_country_code].append([count, name.strip('\n')])
    print("Reading out_male_first.txt\n")
    with open("out_male_first.txt", "r") as in_male_first:
        current_country_code = "<QUALITY ERROR REPORT>"
        for line in in_male_first:
            if line[0] == "-":
                current_country_code = line[1:4]
                country_codes_with_counts[current_country_code][0] = int(line[5:])
            else:
                count, name = line.split('|')
                male_first_by_country[current_country_code].append([count, name.strip('\n')])
    for country_code, count in country_codes_with_counts.items():
        country_codes_with_num_to_gen[country_code] = min(1000, ceil(min(count) / 1.75))
    # { "POL": { nameCountPerSex: 1000, names: ["name1", "name2", ...] }, ...}
    print("Generating names\n")
    out = OrderedDict()
    generated_count = 0
    generated_country_count = 0
    for country_code, num_to_gen in country_codes_with_num_to_gen.items():
        print("   {}".format(country_code))
        # out_generated_names.write("-{},{}\n".format(country_code, num_to_gen))
        country_out = OrderedDict()
        country_out["nameCountPerSex"] = num_to_gen
        country_out["names"] = []
        for n in range(0, num_to_gen):
            rand = randint(0, int(male_first_by_country[country_code][-1][0]) - 1)
            first = \
            next(filter(lambda el: int(el[0]) > rand, male_first_by_country[country_code]),
                    None)[1]
            rand = randint(0, int(male_last_by_country[country_code][-1][0]) - 1)
            last = \
            next(filter(lambda el: int(el[0]) > rand, male_last_by_country[country_code]),
                    None)[1]
            country_out["names"].append("{}|{}".format(first, last))
            generated_count += 1
        for n in range(0, num_to_gen):
            rand = randint(0, int(female_first_by_country[country_code][-1][0]) - 1)
            first = \
            next(filter(lambda el: int(el[0]) > rand, female_first_by_country[country_code]),
                    None)[1]
            rand = randint(0, int(female_last_by_country[country_code][-1][0]) - 1)
            last = \
            next(filter(lambda el: int(el[0]) > rand, female_last_by_country[country_code]),
                    None)[1]
            country_out["names"].append("{}|{}".format(first, last))
            generated_count += 1
        out[country_code] = country_out
    with open("out_generated_names.json", "w") as out_generated_names:
        out_generated_names.write(json.dumps(out))
        print(
            "Generated {} names for {} countries".format(generated_count, len(out)))


# BEHOLD THE HORROR 2: ELECTRIC BOOGALOO
def collect():
    excluded_country_codes = [
        "UNK",
        "rc=",
        "FID",
        "ZZX",
        "ANZ",
        "IOA",
        "EUA",
        "RU1",
        "BWI",
        "EUN",
        "EUA",
        "NRD",
        "BRI",
        "CAL",
        "CAR",
        "CUW",
        "TKS",
        "NMI",
        "NAW",
        "ITF",
        "PDC",
        "GUD",
        "IOP"
    ]
    male_first = OrderedDict()
    male_last = OrderedDict()
    female_first = OrderedDict()
    female_last = OrderedDict()
    nonunique_counts = [defaultdict(lambda: 0), defaultdict(lambda: 0)]
    write_counts = [0, 0, 0, 0]
    all_country_codes = set()
    print("Reading olympic_medallists.csv\n")
    with open("olympic_medallists.csv", "r") as olympic_medallists:
        for line in olympic_medallists.readlines()[1:]:
            try:
                first_quotemark_idx = line.index('"')
                second_quotemark_idx = line.index('"', first_quotemark_idx + 1)
            except ValueError:
                continue
            name = line[first_quotemark_idx + 1:second_quotemark_idx]
            name = unicodedata.normalize('NFKD', name).encode('ascii', 'ignore').decode("UTF-8")
            if name.count(',') != 1:
                continue
            after_name = line[second_quotemark_idx + 2:].split(',')
            country_code = after_name[0]
            if len(country_code) != 3 or country_code in excluded_country_codes:
                continue
            country_code = rewrite_country_code(country_code)
            try:
                raw_sex = after_name[1]
            except IndexError:
                continue
            if raw_sex == "Men":
                sex = "M"
            elif raw_sex == "Women":
                sex = "F"
            else:
                continue
            name_parts = name.split(',')
            first_name = name_parts[1].strip('\n').strip()
            uniq = set(first_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            first_name = first_name.replace(".", ". ")
            first_name = first_name.strip()
            first_name = re.sub(" +", " ", first_name)
            last_name = name_parts[0].strip().upper()
            if ' ' in last_name and '-' in last_name:
                continue
            if '.' in last_name:
                continue
            if last_name.count('-') > 1:
                continue
            if len(re.findall("[^A-Za-z' -]", last_name)) > 0:
                continue
            uniq = set(last_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            last_name = last_name.strip()
            last_name = re.sub(" +", " ", last_name)
            last_name = last_name.title()
            last_name += ' '
            last_name = re.sub("([A-Z]) ", "\g<1>. ", last_name)
            last_name = last_name.strip()
            if sex == "M":
                if country_code not in male_first:
                    male_first[country_code] = OrderedDefaultDict()
                    male_last[country_code] = OrderedDefaultDict()
                male_first[country_code][first_name] += 1
                male_last[country_code][last_name] += 1
                nonunique_counts[0][country_code] += 1
            elif sex == "F":
                if country_code not in female_first:
                    female_first[country_code] = OrderedDefaultDict()
                    female_last[country_code] = OrderedDefaultDict()
                female_first[country_code][first_name] += 1
                female_last[country_code][last_name] += 1
                nonunique_counts[1][country_code] += 1
    print("Reading atp_players.csv\n")
    with open("atp_players.csv", "r") as atp_in_file:
        for line in atp_in_file.readlines():
            cells = line.split(",")
            first_name = cells[1].strip()
            uniq = set(first_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            first_name = first_name.replace(".", ". ")
            first_name += ' '
            first_name = re.sub("([A-Z]) ", "\g<1>. ", first_name)
            first_name = first_name.strip()
            first_name = re.sub(" +", " ", first_name)
            last_name = cells[2].strip()
            if ' ' in last_name and '-' in last_name:
                continue
            if '.' in last_name:
                continue
            if last_name.count('-') > 1:
                continue
            if len(re.findall("[^A-Za-z' -]", last_name)) > 0:
                continue
            uniq = set(last_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            last_name = last_name.strip()
            last_name = re.sub(" +", " ", last_name)
            country_code = cells[5].strip()
            if len(country_code) != 3 or country_code in excluded_country_codes:
                continue
            country_code = rewrite_country_code(country_code)
            if country_code not in male_first:
                male_first[country_code] = OrderedDefaultDict()
                male_last[country_code] = OrderedDefaultDict()
            male_first[country_code][first_name] += 1
            male_last[country_code][last_name] += 1
            nonunique_counts[0][country_code] += 1
    print("Reading wta_players.csv\n")
    with open("wta_players.csv", "r") as wta_in_file:
        for line in wta_in_file.readlines():
            cells = line.split(",")
            first_name = cells[1].strip()
            uniq = set(first_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            first_name = first_name.replace(".", ". ")
            first_name += ' '
            first_name = re.sub("([A-Z]) ", "\g<1>. ", first_name)
            first_name = first_name.strip()
            first_name = re.sub(" +", " ", first_name)
            last_name = cells[2].strip()
            if ' ' in last_name and '-' in last_name:
                continue
            if '.' in last_name:
                continue
            if last_name.count('-') > 1:
                continue
            if len(re.findall("[^A-Za-z' -]", last_name)) > 0:
                continue
            uniq = set(last_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            last_name = last_name.strip()
            last_name = re.sub(" +", " ", last_name)
            country_code = cells[5].strip()
            if len(country_code) != 3 or country_code in excluded_country_codes:
                continue
            country_code = rewrite_country_code(country_code)
            if country_code not in female_first:
                female_first[country_code] = OrderedDefaultDict()
                female_last[country_code] = OrderedDefaultDict()
            female_first[country_code][first_name] += 1
            female_last[country_code][last_name] += 1
            nonunique_counts[1][country_code] += 1
    print("Reading out.csv\n")
    n = 0
    with open("out.csv", "r") as chessdb_in_file:
        for line in chessdb_in_file.readlines():
            if n % 10000 == 0:
                print("  {}".format(n))
            cells = line.split("|")
            name_parts = cells[1].split(",")
            if len(name_parts) != 2:
                continue

            first_name = name_parts[1].strip()
            uniq = set(first_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            first_name = first_name.replace(".", ". ")
            first_name = first_name.replace(" - ", "-")
            first_name += ' '
            first_name = re.sub("([A-Z]) ", "\g<1>. ", first_name)
            first_name = first_name.strip()
            first_name = re.sub(" +", " ", first_name)
            last_name = name_parts[0].strip()
            if ' ' in last_name and '-' in last_name:
                continue
            if '.' in last_name:
                continue
            if last_name.count('-') > 1:
                continue
            if len(re.findall("[^A-Za-z' -]", last_name)) > 0:
                continue
            uniq = set(last_name)
            uniq_count = len(uniq)
            if (uniq_count < 2) \
                    or (uniq_count == 2 and '.' in uniq):
                continue
            last_name = last_name.strip()
            last_name = last_name.replace(" - ", "-")
            last_name = re.sub(" +", " ", last_name)
            last_name += ' '
            last_name = re.sub("([A-Z]) ", "\g<1>. ", last_name)
            last_name = last_name.strip()
            sex = cells[2].strip()
            if len(sex) != 1:
                continue
            country_code = cells[3].strip()
            if len(country_code) != 3 or country_code in excluded_country_codes:
                continue
            country_code = rewrite_country_code(country_code)
            if sex == "M":
                if country_code not in male_first:
                    male_first[country_code] = OrderedDefaultDict()
                    male_last[country_code] = OrderedDefaultDict()
                male_first[country_code][first_name.strip()] += 1
                male_last[country_code][last_name.strip()] += 1
                nonunique_counts[0][country_code] += 1
            elif sex == "F":
                if country_code not in female_first:
                    female_first[country_code] = OrderedDefaultDict()
                    female_last[country_code] = OrderedDefaultDict()
                female_first[country_code][first_name.strip()] += 1
                female_last[country_code][last_name.strip()] += 1
                nonunique_counts[1][country_code] += 1
            else:
                continue
            n += 1
    print("Writing out_male_first.txt\n")
    with open("out_male_first.txt", "w") as out_male_first:
        for country_code, male_first_dict in {k: male_first[k] for k in sorted(male_first)}.items():
            all_country_codes.add(country_code)
            out_male_first.write("-{}|{}\n".format(country_code, nonunique_counts[0][country_code]))
            acc_count = 0
            for name, nonunique_count in {l: male_first_dict[l] for l in sorted(male_first_dict)}.items():
                name = final_fix(name, True)
                acc_count += nonunique_count
                out_male_first.write("{}|{}\n".format(acc_count, name))
                write_counts[0] += 1
    print("Writing out_male_last.txt\n")
    with open("out_male_last.txt", "w") as out_male_last:
        for country_code, male_last_dict in {k: male_last[k] for k in sorted(male_last)}.items():
            all_country_codes.add(country_code)
            out_male_last.write("-{}|{}\n".format(country_code, nonunique_counts[0][country_code]))
            acc_count = 0
            for name, nonunique_count in {l: male_last_dict[l] for l in sorted(male_last_dict)}.items():
                name = final_fix(name, False)
                acc_count += nonunique_count
                out_male_last.write("{}|{}\n".format(acc_count, name))
                write_counts[1] += 1
    print("Writing out_female_first.txt\n")
    with open("out_female_first.txt", "w") as out_female_first:
        for country_code, female_first_dict in {k: female_first[k] for k in sorted(female_first)}.items():
            all_country_codes.add(country_code)
            out_female_first.write("-{}|{}\n".format(country_code, nonunique_counts[1][country_code]))
            acc_count = 0
            for name, nonunique_count in {l: female_first_dict[l] for l in sorted(female_first_dict)}.items():
                name = final_fix(name, True)
                acc_count += nonunique_count
                out_female_first.write("{}|{}\n".format(acc_count, name))
                write_counts[2] += 1
    print("Writing out_female_last.txt\n")
    with open("out_female_last.txt", "w") as out_female_last:
        for country_code, female_last_dict in {k: female_last[k] for k in sorted(female_last)}.items():
            all_country_codes.add(country_code)
            out_female_last.write("-{}|{}\n".format(country_code, nonunique_counts[1][country_code]))
            acc_count = 0
            for name, nonunique_count in {l: female_last_dict[l] for l in sorted(female_last_dict)}.items():
                name = final_fix(name, False)
                acc_count += nonunique_count
                out_female_last.write("{}|{}\n".format(acc_count, name))
                write_counts[3] += 1
    print("Writing out_country_codes.txt\n")
    with open("out_country_codes.txt", "w") as out_country_codes:
        out_country_codes.write("\n".join(sorted(all_country_codes)))

    print("Done\n")
    print("Countries:", len(all_country_codes))
    print("Unique male first names:", write_counts[0])
    print("Unique male last names:", write_counts[1])
    print("Unique female first names:", write_counts[2])
    print("Unique female last names:", write_counts[3])


def scrape_chessdb():
    with open("out.csv", "a") as out_file:
        n = 0
        last = 1623000
        step = 1000
        for start in range(0, last + step, step):
            print("start = {}".format(start))
            url = "https://chess-db.com/public/execute.jsp?age=99&countries=all&sex=a&start={}" \
                .format(start)
            content = urllib.request.urlopen(url).read()
            soup = BeautifulSoup(content, "lxml")
            rows = soup.find("body").find("center").find("font").find("table").find_all("tr")
            records = []
            for row in rows[1:]:
                cells = row.find_all("td")
                if len(cells) < 9:
                    continue
                name = cells[2].find("a").get_text()
                if "," not in name:
                    continue
                country_code = cells[4].find("div")["class"][0][-3:]
                sex = "F" if "w" in cells[8].get_text() else "M"
                records.append([str(n), name, sex, country_code])
                n += 1
            out_file.write("\n".join(["|".join(record) for record in records]))
            out_file.write("\n")


if __name__ == "__main__":
    #collect()
    generate_names()
