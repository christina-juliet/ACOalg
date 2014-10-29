package AntColony;

/* This class gives the latitude and longitude details of the location from which the request is fired
 * Created by Christina */

public class Location {
	private double latitude;
	private double longitude;

	// Constructors
	// If no location is given, the default is Santa Clara,CA.
	Location()
	{
		latitude = 37;
		longitude = 121;
	}

	Location(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	// get and set methods
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double lat)
	{
		this.latitude = lat;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longi)
	{
		this.longitude = longi;
	}

}
