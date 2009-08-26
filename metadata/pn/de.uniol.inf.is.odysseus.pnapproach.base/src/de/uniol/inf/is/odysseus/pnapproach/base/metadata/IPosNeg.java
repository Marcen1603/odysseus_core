package de.uniol.inf.is.odysseus.pnapproach.base.metadata;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;

public interface IPosNeg extends IClone, Comparable<IPosNeg>, Serializable{
	
	public ElementType getElementType();
	public List<Long> getID();
	public PointInTime getTimestamp();
	
	public void setElementType(ElementType type);
	public void setID(List<Long> id);
	public void setTimestamp(PointInTime timestamp);

}
