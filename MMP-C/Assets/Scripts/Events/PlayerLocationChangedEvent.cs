namespace Monmonde
{
	public class PlayerLocationChangedEvent : GameEvent 
	{
		public Location newLocation { get; private set; }

		public PlayerLocationChangedEvent(Location newLocation)
		{
			this.newLocation = newLocation;			
		}
	}
}