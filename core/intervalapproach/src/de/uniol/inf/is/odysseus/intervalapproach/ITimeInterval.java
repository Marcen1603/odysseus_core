package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;


/**
 * @author Jonas Jacobi
 */
public interface ITimeInterval extends IMetaAttribute, Comparable<ITimeInterval>{
	public PointInTime getStart();
	public PointInTime getEnd();
	public void setStart(PointInTime point);
	public void setEnd(PointInTime point);
//	public void setTimeInterval(PointInTime start, PointInTime end);
//	public void setTimeInterval(ITimeInterval ti);
	public boolean isValid();
	public ITimeInterval clone();
	public String toString(PointInTime baseTime);
}
