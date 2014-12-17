package de.uniol.inf.is.odysseus.s100.common.datatype;

import de.uniol.inf.is.odysseus.s100.common.FeatureType;
import de.uniol.inf.is.odysseus.s100.common.geometry.GM_Point;

public class GPSPoint extends FeatureType 
{
	public long timestamp;
	public GM_Point point;
	
	public GPSPoint(long timestamp, double longitude, double latitude)
	{
		this.timestamp = timestamp;
		point = new GM_Point(longitude, latitude);
	}
	
	public GPSPoint(long timestamp, double longitude, double latitude, double altitude)
	{
		this.timestamp = timestamp;
		point = new GM_Point(longitude, latitude, altitude);
	}	
}