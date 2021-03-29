using UnityEngine;

namespace Monmonde
{
	public static class Util
	{
		public static float LatitudeToUnits(float mapHeightUnits, float latitude)
		{
			float sinLatitude = Mathf.Sin(latitude * Mathf.PI / 180f);
			float unscaledLatitude = 
				Mathf.Log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Mathf.PI);

			return unscaledLatitude * mapHeightUnits;
		}

		public static float LongitudeToUnits(float mapWidthUnits, float longitude) {
			float unscaledLongitude = (longitude + 180f) / 360f;
			
			return unscaledLongitude * mapWidthUnits;
		}
	}
}
