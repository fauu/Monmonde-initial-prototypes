using UnityEngine;
using UnityEngine.UI;
using Zenject;

namespace Monmonde
{
	public class ShowLeagueWebpageButtonController : MonoBehaviour
	{
		[Inject] private ViewManager viewManager;

		void Awake()
		{
			Button buttonComponent = transform.GetComponent<Button>();
			buttonComponent.onClick.AddListener(OnClick);
		}

		void OnClick() {
			viewManager.ShowMonGearView();
		}
	}
}