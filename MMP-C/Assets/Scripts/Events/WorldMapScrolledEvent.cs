using UnityEngine;

namespace Monmonde
{
	public class WorldMapScrolledEvent : GameEvent
	{
		public Vector3 newPosition { get; private set; }

		public WorldMapScrolledEvent(Vector3 newPosition)
		{
			this.newPosition = newPosition;
		}
	}
}