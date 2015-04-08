package de.uniol.inf.is.odysseus.sensormanagement.common.types;

public class Position
{
	public double longitude;
	public double latitude;
	public double altitude;
	
	public Position(Position other) 
	{
		longitude = other.longitude;
		latitude = other.latitude;
		altitude = other.altitude;
	}

	public Position() {}	
}
