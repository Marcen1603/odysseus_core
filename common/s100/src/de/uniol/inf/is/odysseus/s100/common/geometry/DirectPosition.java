package de.uniol.inf.is.odysseus.s100.common.geometry;

import java.util.ArrayList;

public class DirectPosition 
{
	public double longitude, latitude, altitude;
	public boolean useAltitude;
	
	public DirectPosition()
	{
		longitude = 0;
		latitude = 0;
		altitude = 0;
		useAltitude = false;
	}
	
	public DirectPosition(double longitude, double latitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = 0;
		this.useAltitude = false;
	}
	
	public DirectPosition(double longitude, double latitude, double altitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.useAltitude = true;
	}	

	public ArrayList<Double> getCoordinate() 
	{
		ArrayList<Double> result = new ArrayList<Double>();
		result.add(longitude);
		result.add(latitude);
		if (useAltitude)
			result.add(altitude);
		
		return result;
	}	
}
