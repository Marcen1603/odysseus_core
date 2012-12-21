package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

final public class Heartbeat extends AbstractPunctuation {

	private Heartbeat(long point) {
		super(point);
	}

	private Heartbeat(PointInTime point) {
		super(point);
	}

	@Override
	public boolean isHeartbeat() {
		return true;
	}
	
	@Override
	public AbstractPunctuation clone() {
		return this;
	}
	
	@Override
	public String toString() {
		return "Heartbeat "+getTime();
	}

	static public Heartbeat createNewHeartbeat(long point){
		return new Heartbeat(point);
	}
	
	static public Heartbeat createNewHeartbeat(PointInTime point){
		return new Heartbeat(point);
	}
	
	
}
