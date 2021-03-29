using Zenject;
using UnityEngine;

namespace Monmonde
{
	public class Player : IInitializable
	{
		[Inject] private EventManager eventManager;
		[Inject] private GeographyManager geographyManager;
		[Inject] private WorldTimeManager worldTimeManager;

		private Location location;
		public Trainer trainer { get; private set; }

		public void Initialize()
		{
			location = geographyManager.locations[1];
			eventManager.QueueEvent(new PlayerLocationChangedEvent(location));

			trainer = new Trainer();
			trainer.id = 7355608;
			trainer.firstName = "Tomasz";
			trainer.lastName = "Hajto";
			trainer.sex = Trainer.Sex.Male;
			trainer.country = geographyManager.countries["POL"];
			trainer.birthDate = new WorldTime(1972, WorldTime.WorldSeason.Autumn, 13, 0, 0);
			trainer.leagueRegistrationDate = worldTimeManager.now;
			trainer.leagueClass = Trainer.LeagueClass.Green;
			trainer.leagueRank = 12893;
			trainer.leagueRating = 1000;
		}
	}
}