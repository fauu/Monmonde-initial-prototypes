using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Monmonde.MonGear
{
	public class MonGearWorldRankingTableRowViewmodel
	{
		public string rank { get; private set; }
		public string trainerFullname { get; private set; }
		public Trainer.Sex sex { get; private set; }
		public string countryCode { get; private set; }
		public string rating { get; private set; }

		public MonGearWorldRankingTableRowViewmodel(string rank, string trainerFullname, Trainer.Sex sex, string countryCode, string rating)
		{
			this.rank = rank;
			this.trainerFullname = trainerFullname;
			this.sex = sex;
			this.countryCode = countryCode;
			this.rating = rating;
		}
	}
}