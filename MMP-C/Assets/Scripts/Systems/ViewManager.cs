using UnityEngine;
using UnityEngine.UI;
using Zenject;
using TMPro;

namespace Monmonde
{
	public class ViewManager : IInitializable
	{
		private GameObject worldMapView;
		private GameObject locationView;
		private GameObject monGear;
		private GameObject worldMapLocationDialog;

		public void Initialize()
		{
			worldMapView = GameObject.Find("WorldMapView");
			locationView = GameObject.Find("LocationView");
			monGear = GameObject.Find("MonGearView");
			worldMapLocationDialog = GameObject.Find("WorldMapLocationDialog");

			locationView.SetActive(false);
			worldMapLocationDialog.SetActive(false);

			HideMonGearView();
		}

		public void GoToWorldMapScreen()
		{
			locationView.SetActive(false);
			worldMapView.SetActive(true);
		}

		public void GoToLocationScreen()
		{
			locationView.SetActive(true);
			worldMapView.SetActive(false);
		}

		public void ShowMonGearView()
		{
			monGear.SetActive(true);
			monGear.GetComponent<MonGearViewController>().Show();
			worldMapView.GetComponent<WorldMapScroller>().enabled = false;
		}

		public void HideMonGearView()
		{
			monGear.SetActive(false);
			worldMapView.GetComponent<WorldMapScroller>().enabled = true;
		}

		public void ShowLocationDialog(Location location)
		{
			Resources.UnloadUnusedAssets();

			worldMapLocationDialog.SetActive(true);
			worldMapLocationDialog.GetComponent<WorldMapLocationDialogController>().Show();
			worldMapView.GetComponent<WorldMapScroller>().enabled = false;

			// TODO: Move the rest to LocationDialogController
			Transform locationHeader = GameObject.Find("LocationHeader").transform;
			locationHeader.Find("LocationName").GetComponent<TextMeshProUGUI>().text = location.settlementName;
			locationHeader.Find("LocationCountry/LocationCountryName")
				.GetComponent<TextMeshProUGUI>().text = location.country.name;

			// TODO: Use country codes for flag names
			string flagImagePath = string.Format("CountryFlags/Small/{0}", location.country.code);
			locationHeader.Find("LocationCountry/LocationCountryFlag")
				.GetComponent<Image>().sprite = Resources.Load<Sprite>(flagImagePath);
		}

		public void HideLocationDialog() {
			worldMapLocationDialog.SetActive(false);
			worldMapView.GetComponent<WorldMapScroller>().enabled = true;
		}
	}
}