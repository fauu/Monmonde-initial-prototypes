using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Zenject;
using TMPro;

namespace Monmonde
{
	public class WorldMapLocationMarkers : MonoBehaviour
	{
		// TODO: Perhaps store these only in WorldMap
		public static int MapWidthUnits = 128;
		public static int MapHeightUnits = 128;

		public GameObject markerPrefab;

		[Inject] private EventManager eventManager;
		[Inject] private ViewManager viewManager;
		[Inject] private GeographyManager geographyManager;
		private Dictionary<int, GameObject> markers;
		// TODO: Calculate properly
		private Vector2 mapOffset = new Vector2(70.5f, 21.425f);

		void Start()
		{
			eventManager.AddListener<PlayerLocationChangedEvent>(OnPlayerLocationChanged);

			markers = new Dictionary<int, GameObject>();
			foreach (KeyValuePair<int, Location> locationEntry in geographyManager.locations)
			{
				Location location = locationEntry.Value;

				float x = Util.LongitudeToUnits(MapWidthUnits, location.longitude);
				float y = Util.LatitudeToUnits(MapHeightUnits, location.latitude);

				GameObject newMarker = CreateMarker(location, x - mapOffset.x, y - mapOffset.y);
				markers.Add(location.id, newMarker);
			}
		}

		private void OnPlayerLocationChanged(PlayerLocationChangedEvent e)
		{
			GameObject playerLocationMarker;
			if (markers.TryGetValue(e.newLocation.id, out playerLocationMarker))
			{
				Transform label = playerLocationMarker.transform.Find("Label");
				label.GetComponent<Image>().color = new Color(0.882f, 0.718f, 0.298f, 0.598f);
				label.GetComponent<Button>().interactable = false;
			}
		}

		private GameObject CreateMarker(Location location, float x, float y)
		{
			GameObject marker = 
				Instantiate(markerPrefab, new Vector3(x, y, 0), Quaternion.identity) as GameObject;

            marker.GetComponent<WorldMapLocationMarkerController>().SetData(location);
			marker.GetComponentInChildren<Button>().onClick.AddListener(() => viewManager.ShowLocationDialog(location));

			return marker;
		}
	}
}