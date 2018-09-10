package de.uniol.inf.is.odysseus.sensormanagement.common.types.position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "absoluteposition")
@XmlAccessorType (XmlAccessType.FIELD)
public class AbsolutePosition extends Position
{
	protected AbsolutePosition() {}
	
	public double longitude, latitude, altitude, orientation;
	
	public AbsolutePosition(double longitude, double latitude, double altitude, double orientation)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.orientation = orientation;
	}

/*	@Override public AbsolutePosition asAbsolutePosition() 
	{
		return this;
	}*/
	
	@Override public String toString()
	{
//		return "lon="+longitude + " lat=" + latitude + " alt=" + altitude + " or=" + orientation;
		return "absolute: " + longitude + ", " + latitude + ", " + altitude + ", " + orientation;
	}	
	
	@Override public AbsolutePosition clone()
	{
		return new AbsolutePosition(longitude, latitude, altitude, orientation);
	}
}