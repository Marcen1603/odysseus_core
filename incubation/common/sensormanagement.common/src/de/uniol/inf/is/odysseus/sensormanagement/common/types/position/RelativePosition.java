package de.uniol.inf.is.odysseus.sensormanagement.common.types.position;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "relativeposition")
@XmlAccessorType (XmlAccessType.FIELD)
public class RelativePosition extends Position
{
	double x, y, z, orientation;
	
	protected RelativePosition() {}
	
	public RelativePosition(double x, double y, double z, double orientation)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.orientation = orientation;
	}

	@Override public RelativePosition clone()
	{
		return new RelativePosition(x, y, z, orientation);
	}	
	
/*	@Override public AbsolutePosition asAbsolutePosition() 
	{
		// TODO: Hier auf korrekte Umrechnung achten!
		AbsolutePosition parentPos = parentBox.getPosition().asAbsolutePosition();
		
		return Position.add(parentPos, this);
	}*/
}