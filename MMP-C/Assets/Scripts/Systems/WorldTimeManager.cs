using UnityEngine;
using Zenject;

namespace Monmonde
{
	public class WorldTimeManager : ITickable, IInitializable
	{
		public static float RealSecondsPerWorldMinute = 1;

		public WorldTime time = new WorldTime();
		public WorldTime now
		{
			get
			{
				return time;
			}
		}

		private float lastAdvanceTime;

		public void Initialize()
		{
			time = new WorldTime(2017, WorldTime.WorldSeason.Spring, 1, 12, 05);
			lastAdvanceTime = Time.time;
		}
		
		public void Tick()
		{
			if (Time.time - lastAdvanceTime > RealSecondsPerWorldMinute)
			{
				time.advance(1);
				lastAdvanceTime = Time.time;
			}	
		}
	}
}