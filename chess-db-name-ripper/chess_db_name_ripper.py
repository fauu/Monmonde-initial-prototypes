import os
import re
import sys
import collections
import json
from string import strip
from bs4 import BeautifulSoup
import urllib

OUT_DIRECTORY = "out"
REMOVE_DUPLICATES = True
NO_PAGES = 3
COUNTRIES = ["pol", "ger", "cze", "ltu"]
COUNTRIES_CHESSDB_TO_ISO = {
    'ger': 'deu'
}
SEXES = ["m", "w"]
PACK_FILENAME = "names.csv"
NAME_SEPARATOR = ","
FIELD_SEPARATOR = "|"

def rip_url(url):
    print "Processing %s" % url
    r = urllib.urlopen(url).read()
    soup = BeautifulSoup(r, "lxml")
    name_link_selector = "body > center > font > table > tr > td:nth-of-type(3) > a"
    return [element.get_text() for element in soup.select(name_link_selector)]

def get_urls(country, sex, number_of_pages):
    base = "https://chess-db.com/public/execute.jsp?age=99&countries=%s&sex=%s&start=" % (country.upper(), sex.lower())
    start_values = map(lambda x: str(x * 1000), range(number_of_pages))
    return [base + start_value for start_value in start_values]

def tidy(name_parts):
    name_parts = [re.sub(r"[^a-zA-Z ]+", "", name_part) for name_part in name_parts]
    map(lambda s: strip(s), name_parts)
    return name_parts

def rip_country_sex(country, sex):
    print "--- Starting ripping for country code '%s', sex '%s' ---" % (country.upper(), sex.upper())
    urls = get_urls(country, sex, NO_PAGES)

    names = []
    for url in urls:
        names += rip_url(url)

    first_names = []
    last_names = []
    for name in names:
        split_name = name.split(", ")
        if len(split_name) == 2 and len(split_name[0]) > 1 and len(split_name[1]) > 1:
            first_names.append(split_name[1])
            last_names.append(split_name[0])

    if REMOVE_DUPLICATES:
        first_names = list(set(first_names))
        last_names = list(set(last_names))
        print "Duplicates removed"

    tidy(first_names)
    tidy(last_names)

    if not os.path.exists(OUT_DIRECTORY):
        os.makedirs(OUT_DIRECTORY)
        print "Directory '%s' created" % OUT_DIRECTORY
    
    filename_base = "%s/%s_%s" % (OUT_DIRECTORY, country.lower(), sex.lower())
    first_names_filename = "%s_first.txt" % filename_base
    last_names_filename = "%s_last.txt" % filename_base
    with open(first_names_filename, "w") as f:
        f.write("\n".join(first_names))
        print "%s written (%d lines)" % (first_names_filename, len(first_names))
    with open(last_names_filename, "w") as f:
        f.write("\n".join(last_names))
        print "%s written (%d lines)" % (last_names_filename, len(last_names))

    print "--- Finished ripping for country code '%s', sex '%s' ---\n" % (country.upper(), sex.upper())

def rip_country(country):
    for sex in SEXES:
        rip_country_sex(country, sex)

def rip():
    for country in COUNTRIES:
        rip_country(country)
    
def read_data():
    nested_dict = lambda: collections.defaultdict(nested_dict)
    data = nested_dict()

    filenames = os.listdir(OUT_DIRECTORY)
    for filename in filenames:
        file_params = filename[:-4].split("_")

        error_msg = "Unrecognized filename format for file %s. Skipping" % filename
        if len(file_params) != 3:
           print error_msg
           continue
        
        country = file_params[0]
        sex = file_params[1]
        kind = file_params[2]
        if (len(country) != 3) or (sex not in ["m", "w"]) or (kind not in ["first", "last"]):
           print error_msg
           continue

        path = "%s/%s" % (OUT_DIRECTORY, filename)
        with open(path, "r") as f:
            names = f.readlines()
        names = [name.strip() for name in names]

        data[country][sex][kind] = names
    
    return data

def pack():
    print "Packing CSV file..."

    data = read_data()

    rows = []
    for country in data:
        fields = []
        fields.append(country if not COUNTRIES_CHESSDB_TO_ISO.has_key(country) else COUNTRIES_CHESSDB_TO_ISO[country])
        fields.append(NAME_SEPARATOR.join(data[country]["m"]["first"]))
        fields.append(NAME_SEPARATOR.join(data[country]["m"]["last"]))
        fields.append(NAME_SEPARATOR.join(data[country]["w"]["first"]))
        fields.append(NAME_SEPARATOR.join(data[country]["w"]["last"]))
        rows.append(FIELD_SEPARATOR.join(fields))

    path = "%s/%s" % (OUT_DIRECTORY, PACK_FILENAME)
    with open(path, "w") as f:
        for row in rows:
            f.write("%s\n" % row)
        print "%s written (%d lines)" % (path, len(rows))
    
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print "Wrong number of arguments. Exiting (B EXITING B DON'T KILL ME)"
        sys.exit()

    arg = sys.argv[1]

    if arg == "--rip":
        rip()
    elif arg == "--pack":
        pack()
    else:
        print "Unrecognized argument"
