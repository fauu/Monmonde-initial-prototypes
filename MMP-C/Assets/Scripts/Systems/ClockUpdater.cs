using UnityEngine;
using Zenject;
using TMPro;

namespace Monmonde
{
	public class ClockUpdater : MonoBehaviour
	{
		public TextMeshProUGUI time;
		public TextMeshProUGUI date;

		[Inject] private WorldTimeManager timeManager;

		void Update()
		{
			time.text = timeManager.time.timeString;
			date.text = timeManager.time.dateString;
		}
	}
}