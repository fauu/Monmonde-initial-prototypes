using UnityEngine;
using TMPro;

namespace Monmonde
{
	public class WorldMapLocationMarkerController : MonoBehaviour 
	{
		public TextMeshProUGUI settlement;
		public TextMeshProUGUI zone;

		public void SetData(Location location)
		{
			this.settlement.text = location.settlementName;
			if (location.zoneName != null)
			{
				this.zone.text = location.zoneName;
			}
			else
			{
				this.zone.enabled = false;
			}
		}
	}
}
