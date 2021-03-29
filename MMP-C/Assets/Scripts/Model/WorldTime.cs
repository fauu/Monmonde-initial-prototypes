using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WorldTime
{
	public enum WorldSeason { Spring, Summer, Autumn, Winter }

	public static int EpochStartYear = 1900;
	public static int SeasonsInYear = 4;
	public static int DaysInSeason = 30;
	public static int HoursInDay = 24;
	public static int MinutesInHour = 60;

	private int epochTime;

	public int year
	{
		get { return EpochStartYear + (epochTime / (MinutesInHour * HoursInDay * DaysInSeason * SeasonsInYear)); }	
	}

	public WorldSeason season
	{
		get { return (WorldSeason) ((epochTime / (MinutesInHour * HoursInDay * DaysInSeason)) % SeasonsInYear); }	
	}

	public int day
	{
		get { return 1 + ((epochTime / (MinutesInHour * HoursInDay)) % DaysInSeason); }	
	}

	public int hour
	{
		get { return ((epochTime / MinutesInHour) % HoursInDay); }	
	}

	public int minute
	{
		get { return (epochTime % MinutesInHour); }
	}

	public string dateString {
		get { return string.Format("Day {0} of {1}, {2}", day, season, year); }
	}

	public string timeString {
		get { return string.Format("{0}:{1:00}", hour, minute); }
	}

	public WorldTime(int year, WorldSeason season, int day, int hour, int minute) {
		this.epochTime = minute + 
		                 hour * MinutesInHour +
						 (day - 1) * MinutesInHour * HoursInDay +
						 ((int) season) * MinutesInHour * HoursInDay * DaysInSeason +
						 (year - EpochStartYear) * MinutesInHour * HoursInDay * DaysInSeason * SeasonsInYear;
	}

	public WorldTime() {
		this.epochTime = 0;
	}

	public void advance(int minutes) 
	{
		this.epochTime += minutes;
	}
}
