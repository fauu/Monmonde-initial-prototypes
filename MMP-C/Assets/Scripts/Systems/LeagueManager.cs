using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using Zenject;

namespace Monmonde
{
	public class LeagueManager : IInitializable
	{
		public List<Trainer> trainers { get; private set; }
		public List<MonGear.MonGearWorldRankingTableRowViewmodel> ranking { get; private set; }

		[Inject] private GeographyManager geographyManager;

		public void Initialize()
		{
			GenerateTrainers(100);

			foreach (var trainer in trainers)
			{
				Debug.Log(trainer.ToString());
			}

			ranking = new List<MonGear.MonGearWorldRankingTableRowViewmodel>();
			var top10Trainers = trainers.OrderByDescending(t => t.leagueRating).Take(10).ToList();
			for (int i = 0; i < 10; i++)
			{
				Trainer trainer = top10Trainers[i];	
				var entry = new MonGear.MonGearWorldRankingTableRowViewmodel((i + 1).ToString(), trainer.fullName, trainer.sex, trainer.country.code, trainer.leagueRating.ToString());
				ranking.Add(entry);
			}
		}

		private void GenerateTrainers(int count)
		{
			trainers = new List<Trainer>();

			Dictionary<string, NameDatabaseEntry> nameDatabase = 
				new Dictionary<string, NameDatabaseEntry>();
			HashSet<string> countryCodes = new HashSet<string>();

			var namesFile = Resources.Load<TextAsset>("Names");
			string unparsedNames = namesFile.text;
			Resources.UnloadAsset(namesFile);

			string[] lines = unparsedNames.Split(new string[] { "\r\n", "\r", "\n" }, StringSplitOptions.RemoveEmptyEntries);
			foreach (string line in lines)
			{
				NameDatabaseEntry entry = new NameDatabaseEntry();

				string[] fields = line.Split('|');
				entry.countryCode = fields[0].ToUpper();
				entry.maleFirstNames = fields[1].Split(',');
				entry.maleLastNames = fields[2].Split(',');
				entry.femaleFirstNames = fields[3].Split(',');
				entry.femaleLastNames = fields[4].Split(',');

				nameDatabase.Add(entry.countryCode, entry);
				countryCodes.Add(entry.countryCode);
			}

			var countryCodesAsArray = new string[countryCodes.Count];
			countryCodes.CopyTo(countryCodesAsArray);
			var random = new System.Random();
			for (int i = 0; i < count; i++)
			{
				Trainer trainer = new Trainer();

				trainer.id = -1;

				trainer.sex = (random.NextDouble() > 0.7) ? Trainer.Sex.Female : Trainer.Sex.Male;

				string randomCountryCode = countryCodesAsArray[random.Next(countryCodesAsArray.Length)];
				trainer.country = geographyManager.countries[randomCountryCode];

				NameDatabaseEntry nameDatabaseEntry = nameDatabase[trainer.country.code];
				trainer.firstName = 
					(trainer.sex == Trainer.Sex.Male) ? 
					nameDatabaseEntry.maleFirstNames[random.Next(nameDatabaseEntry.maleFirstNames.Length)] :
					nameDatabaseEntry.femaleFirstNames[random.Next(nameDatabaseEntry.femaleFirstNames.Length)];
				trainer.lastName = 
					(trainer.sex == Trainer.Sex.Male) ? 
					nameDatabaseEntry.maleLastNames[random.Next(nameDatabaseEntry.maleLastNames.Length)] :
					nameDatabaseEntry.femaleLastNames[random.Next(nameDatabaseEntry.femaleLastNames.Length)];
				
				trainer.birthDate = new WorldTime(2000, WorldTime.WorldSeason.Summer, 1, 2, 3);
				trainer.leagueRegistrationDate = new WorldTime(2000, WorldTime.WorldSeason.Summer, 1, 2, 3);
				trainer.leagueRank = -1;
				trainer.leagueRating = random.Next(800, 2800);

				trainers.Add(trainer);
			}
		}
	}
}