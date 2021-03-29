using System.Collections;
using System.IO;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

namespace Monmonde.MonGear
{
	public class MonGearWorldRankingTableRowController : MonoBehaviour
	{
		public TextMeshProUGUI rank;
		public TextMeshProUGUI trainerFullname;
		public Image sex;
		public Image country;
		public TextMeshProUGUI rating;
		public Sprite maleIcon;
		public Color maleIconColor;
		public Sprite femaleIcon;
		public Color femaleIconColor;

		private void Start()
		{
			//sexToIcon = new Dictionary<Trainer.Sex, Sprite>();
			//sexToIcon.Add(Trainer.Sex.Male, Resources.Load<Sprite>(Path.Combine("Icons", )));
		}

		public void SetData(MonGearWorldRankingTableRowViewmodel viewmodel)
		{
			this.rank.text = viewmodel.rank;
			this.trainerFullname.text = viewmodel.trainerFullname;
			this.sex.sprite = GetSexIcon(viewmodel.sex);
			this.sex.color = GetSexColor(viewmodel.sex);
			this.country.sprite = Resources.Load<Sprite>(Path.Combine("CountryFlags/Small", viewmodel.countryCode));
			this.rating.text = viewmodel.rating;
		}

		private Sprite GetSexIcon(Trainer.Sex sex)
		{
			return sex == Trainer.Sex.Male ? maleIcon : femaleIcon;
		}

		private Color GetSexColor(Trainer.Sex sex)
		{
			return sex == Trainer.Sex.Male ? maleIconColor : femaleIconColor;
		}

		public void MarkAsPlayerRow()
		{
			GetComponent<Image>().enabled = true;
		}
	}
}