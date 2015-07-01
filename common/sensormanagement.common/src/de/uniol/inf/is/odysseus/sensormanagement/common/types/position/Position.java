package de.uniol.inf.is.odysseus.sensormanagement.common.types.position;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({AbsolutePosition.class, RelativePosition.class})
public abstract class Position implements Cloneable
{
//	abstract public AbsolutePosition asAbsolutePosition();
	
	@Override abstract public Position clone();
	
	public static AbsolutePosition add(AbsolutePosition abs, RelativePosition rel)
	{
		// TODO: Orientation beachten. Umrechnung zu Longitude und Latitude!
		return new AbsolutePosition(abs.longitude + rel.x, abs.latitude+rel.y, abs.altitude+rel.z, 0);		
	}
	
	public static AbsolutePosition sub(AbsolutePosition abs, RelativePosition rel)
	{
		// TODO: Orientation beachten. Umrechnung zu Longitude und Latitude!
		return new AbsolutePosition(abs.longitude - rel.x, abs.latitude-rel.y, abs.altitude-rel.z, 0);		
	}	
}
