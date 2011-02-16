package de.uniol.inf.is.odysseus.metadata;



/**
 * @author Jonas Jacobi
 */
public interface ITimeInterval extends IMetaAttribute, Comparable<ITimeInterval>{
	public PointInTime getStart();
	public PointInTime getEnd();
	public void setStart(PointInTime point);
	public void setEnd(PointInTime point);
	public boolean isValid();
	@Override
	public ITimeInterval clone();
	public String toString(PointInTime baseTime);
}
