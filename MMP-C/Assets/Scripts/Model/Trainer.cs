namespace Monmonde
{
	public class Trainer
	{
		public enum Sex { Male, Female }
		public enum LeagueClass { Green, Yellow, Silver, Crystal, Master }

		public int id { get; set; }
		public string firstName { get; set; }
		public string lastName { get; set; }
		public string fullName
		{
			get
			{
				return string.Format("{0} {1}", firstName, lastName);
			}
		}
		public Sex sex { get; set; }
		public Country country { get; set; }
		public string nationality
		{
			get
			{
				return country.nationalityName;
			}
		}
		public WorldTime birthDate { get; set; }
		public WorldTime leagueRegistrationDate { get; set; }
		public LeagueClass leagueClass { get; set; }
		public int leagueRank { get; set; }
		public int leagueRating { get; set; }

		public override string ToString()
		{
			return string.Format("[TRAINER id='{0}' name='{1}' sex='{2}' country='{3}', birthDate='{4}'",
				id, fullName, sex.ToString(), country.name, birthDate.dateString);
		}
	}
}