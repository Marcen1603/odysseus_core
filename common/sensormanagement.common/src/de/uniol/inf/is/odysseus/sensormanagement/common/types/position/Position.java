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
	
	// TODO: Temporäre Funktion!
	public static Position fromString(String posString)
	{
		if (posString == null || posString.isEmpty()) return null;
		
		String[] split = posString.split(":");
		boolean absolute;
		
		if (split.length == 1) {
			absolute = true;
		} else if (split.length == 2) {
			if (split[0].trim().equals("absolute"))
				absolute = true;
			else if (split[0].trim().equals("relative"))
				absolute = false;
			else
				throw new IllegalArgumentException("Unknown position type: " + split[0]);
			
			posString = split[1];
		} else
			throw new IllegalArgumentException("Position string may only contain one ':'");
			
		String[] values = posString.split(",");
		double v0 = Double.parseDouble(values[0]);
		double v1 = Double.parseDouble(values[1]);
		double v2 = Double.parseDouble(values[2]);
		double v3 = Double.parseDouble(values[3]);
		
		if (absolute)
			return new AbsolutePosition(v0, v1, v2, v3);
		else
			return new RelativePosition(v0, v1, v2, v3);
	}
}
