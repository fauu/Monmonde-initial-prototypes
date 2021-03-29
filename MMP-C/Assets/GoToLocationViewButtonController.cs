using UnityEngine;
using UnityEngine.UI;
using Zenject;
using TMPro;

namespace Monmonde
{
	public class GoToLocationViewButtonController : MonoBehaviour
	{
		public TextMeshProUGUI locationValue;
		public Image locationFlag;

		[Inject] private EventManager eventManager;
		[Inject] private ViewManager viewManager;

		void Awake()
		{
			eventManager.AddListener<PlayerLocationChangedEvent>(OnPlayerLocationChanged);

			Button buttonComponent = transform.GetComponent<Button>();
			buttonComponent.onClick.AddListener(OnClick);
		}

		void OnPlayerLocationChanged(PlayerLocationChangedEvent e)
		{
			Resources.UnloadUnusedAssets();

			Location location = e.newLocation;

			locationValue.text = string.Format("{0}, {1}", location.settlementName, location.country.name);

			string flagImagePath = string.Format("CountryFlags/Small/{0}", location.country.code);
			locationFlag.sprite = Resources.Load<Sprite>(flagImagePath);
		}

		void OnClick() {
			viewManager.GoToLocationScreen();
		}
	}
}