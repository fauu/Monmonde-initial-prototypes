public class Location
{
	public int id;
	public string settlementName;
	public string zoneName;
	public float latitude;
	public float longitude;
	public Country country;

	public Location(int id, string settlementName, string zoneName, float latitude, float longitude, Country country)
	{
		this.id = id;
		this.settlementName = settlementName;
		this.zoneName = zoneName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
	}
}
