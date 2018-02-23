package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface IValidTime extends IMetaAttribute {
	
	public PointInTime getStart();
	public PointInTime getEnd();
	public void setStart(PointInTime point);
	public void setEnd(PointInTime point);
	public void setStartAndEnd(PointInTime start, PointInTime end);

}
