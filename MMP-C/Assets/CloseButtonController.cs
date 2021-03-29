using UnityEngine;
using UnityEngine.UI;
using Zenject;

namespace Monmonde
{
	public class CloseButtonController : MonoBehaviour
	{
		[Inject] private ViewManager viewManager;

		void Start()
		{
			Button buttonComponent = transform.GetComponent<Button>();
			buttonComponent.onClick.AddListener(OnClick);
		}

		void OnClick() {
			viewManager.HideMonGearView();
		}
	}
}