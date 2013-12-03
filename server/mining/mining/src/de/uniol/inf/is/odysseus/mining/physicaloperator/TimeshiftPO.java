package de.uniol.inf.is.odysseus.mining.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TimeshiftPO<M extends ITimeInterval> extends AbstractPipe<AbstractStreamObject<M>, AbstractStreamObject<M>> {

	private PointInTime timeToShift = PointInTime.getZeroTime();
	
	public TimeshiftPO(PointInTime timetoshift){
		this.timeToShift = timetoshift;
	}
	
	public TimeshiftPO(TimeshiftPO<M> timeshiftPO) {
		this.timeToShift = timeshiftPO.timeToShift;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(AbstractStreamObject<M> object, int port) {
		ITimeInterval old = object.getMetadata();		
		object.getMetadata().setStartAndEnd(old.getStart().plus(timeToShift), old.getEnd().plus(timeToShift));
		transfer(object);		
	}

	@Override
	public TimeshiftPO<M> clone() {
		return new TimeshiftPO<M>(this);
	}

}
