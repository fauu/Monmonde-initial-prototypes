using UnityEngine;
using UnityEngine.UI;
using TMPro;
using Zenject;

namespace Monmonde.MonGear
{
	public class TheLeagueController : MonoBehaviour
	{
		public TextMeshProUGUI sidebarTrainerId;
		public Button navigationMyTrainerProfileEntry;
		public Button navigationWorldRankingEntry;
		public Color navigationActiveBackgroundColor;
		public Color navigationActiveForegroundColor;
		public Color navigationNormalBackgroundColor;
		public Color navigationNormalForegroundColor;
		public GameObject myTrainerProfileView;
		public GameObject worldRankingView;

		private Button navigationActiveEntry;

		[Inject] private Player player;

		private void Start()
		{
			Trainer trainer = player.trainer;
            // sidebarTrainerId.text = trainer.id.ToString();

			ShowMyTrainerProfileView();
		}

		private void SetNavigationEntryActive(Button entry)
		{
			if (entry == navigationActiveEntry) return;

			if (navigationActiveEntry != null)
			{
				navigationActiveEntry.GetComponent<Animator>().enabled = true;
				navigationActiveEntry.GetComponent<Animator>().CrossFade("Normal", 0f);
				navigationActiveEntry.GetComponent<Image>().color = navigationNormalBackgroundColor;
				navigationActiveEntry.transform.Find("Icon").GetComponent<Image>().color = navigationNormalForegroundColor;
				navigationActiveEntry.transform.Find("Text").GetComponent<TextMeshProUGUI>().color = navigationNormalForegroundColor;
			}

			entry.GetComponent<Animator>().enabled = false;
			entry.GetComponent<Animator>().CrossFade("Normal", 0f);
			entry.GetComponent<Image>().color = navigationActiveBackgroundColor;
			entry.transform.Find("Icon").GetComponent<Image>().color = navigationActiveForegroundColor;
			entry.transform.Find("Text").GetComponent<TextMeshProUGUI>().color = navigationActiveForegroundColor;

			navigationActiveEntry = entry;
		}

		public void ShowMyTrainerProfileView()
		{
			SetNavigationEntryActive(navigationMyTrainerProfileEntry);
			worldRankingView.SetActive(false);
			myTrainerProfileView.SetActive(true);
		}
		
		public void ShowWorldRankingView()
		{
			SetNavigationEntryActive(navigationWorldRankingEntry);
			myTrainerProfileView.SetActive(false);
			worldRankingView.SetActive(true);
		}
	}
}