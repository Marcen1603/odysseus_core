package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

final public class Heartbeat extends AbstractPunctuation {

	public Heartbeat(long point) {
		super(point);
	}

	public Heartbeat(PointInTime point) {
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

}
