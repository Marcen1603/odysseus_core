package de.uniol.inf.is.odysseus.s100.common.geometry;

public class GM_Point {

	public DirectPosition position;
	
	public GM_Point(double longitude, double latitude)
	{
		position = new DirectPosition(longitude, latitude);
	}
	
	public GM_Point(double longitude, double latitude, double altitude)
	{
		position = new DirectPosition(longitude, latitude, altitude);
	}
}
