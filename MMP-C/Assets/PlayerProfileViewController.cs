using UnityEngine;
using UnityEngine.UI;
using Zenject;
using TMPro;

namespace Monmonde.MonGear
{
	public class PlayerProfileViewController : MonoBehaviour
	{
		public TextMeshProUGUI fullName;
		public TextMeshProUGUI sex;
		public TextMeshProUGUI nationality;
		public Image flag;
		public TextMeshProUGUI birthDate;
		public TextMeshProUGUI leagueRegistrationDate;
		public TextMeshProUGUI leagueClass;
		public TextMeshProUGUI leagueRank;
		public TextMeshProUGUI leagueRating;

		[Inject] private Player player;

		private void Start()
		{
			Trainer trainer = player.trainer;

			fullName.text = trainer.fullName;

			sex.text = trainer.sex.ToString();

			nationality.text = trainer.nationality;

			string flagImagePath = string.Format("CountryFlags/Small/{0}", trainer.country.code);
			flag.sprite = Resources.Load<Sprite>(flagImagePath);

			birthDate.text = trainer.birthDate.dateString;

			leagueRegistrationDate.text = trainer.leagueRegistrationDate.dateString;

			leagueClass.text = trainer.leagueClass.ToString().ToUpper();

			leagueRank.text = string.Format("#{0}", trainer.leagueRank.ToString());

			leagueRating.text = trainer.leagueRating.ToString();
		}
	}
}