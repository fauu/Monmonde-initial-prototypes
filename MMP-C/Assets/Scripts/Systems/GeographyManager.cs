using System.Collections.Generic;
using Zenject;

namespace Monmonde {
	public class GeographyManager : IInitializable
	{
		public Dictionary<string, Country> countries = new Dictionary<string, Country>();
		public Dictionary<int, Location> locations = new Dictionary<int, Location>();

		public void Initialize()
		{
			countries.Add("POL", new Country("POL", "Poland", "Polish"));
			countries.Add("DEU", new Country("DEU", "Germany", "German"));
			countries.Add("CZE", new Country("CZE", "Czechia", "Czech"));
			countries.Add("LTU", new Country("LTU", "Lithuania", "Lithuanian"));

			locations.Add(1, new Location(1, "Warsaw", "Kampinos Forest", 52.2297f, 21.0122f, countries["POL"]));
			locations.Add(2, new Location(2, "Zakopane", "Polish Tatras", 49.2992f, 19.9496f, countries["POL"]));
			locations.Add(3, new Location(3, "Łeba", "Słowiński Park", 54.7601f, 17.5563f, countries["POL"]));
			locations.Add(4, new Location(4, "Berlin", null, 52.5200f, 13.4050f, countries["DEU"]));
			locations.Add(5, new Location(5, "Kraków", "Wieliczka Salt Mine", 50.0647f, 19.9450f, countries["POL"]));
			locations.Add(6, new Location(6, "Prague", null, 50.0755f, 14.4378f, countries["CZE"]));
			locations.Add(7, new Location(7, "Kaunas", "Kaunas Reservoir", 54.8985f, 23.9036f, countries["LTU"]));
			locations.Add(8, new Location(8, "Rathen", "Bastei", 50.9588f, 14.0833f, countries["DEU"]));
		}
	}
}