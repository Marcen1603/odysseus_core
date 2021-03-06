package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

abstract public class AbstractPunctuation implements
		IPunctuation {

	private static final long serialVersionUID = 1L;
	
	final public byte NUMBER = -1; 
	
	final PointInTime point;
	
	public AbstractPunctuation(long point) {
		this.point = new PointInTime(point);
	}

	public AbstractPunctuation(PointInTime p) {
		this.point = p;
	}
	
	public AbstractPunctuation(AbstractPunctuation punct){
		this.point = punct.point;
	}
	
	@Override
	public PointInTime getTime() {
		return point;
	}

	@Override
	public boolean before(PointInTime time) {
		return this.point.before(time);
	}

	@Override
	public boolean beforeOrEquals(PointInTime time) {
		return this.point.beforeOrEquals(time);
	}

	@Override
	public boolean after(PointInTime time) {
		return this.point.after(time);
	}

	@Override
	public boolean afterOrEquals(PointInTime time) {
		return this.point.afterOrEquals(time);
	}

	@Override
	public boolean isPunctuation() {
		return true;
	}

	@Override
	public boolean isHeartbeat() {
		return false;
	}
	
	@Override
	public String toString() {
		return this.getClass()+" "+getTime();
	}
	
	@Override
	public byte getNumber() {
		return NUMBER;
	}
	
	@Override
	abstract public AbstractPunctuation clone();

	@Override
	abstract public AbstractPunctuation clone(PointInTime newTime);

}
