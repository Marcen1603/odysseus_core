package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

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

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(AbstractStreamObject<M> object, int port) {
		ITimeInterval newMetadata = (ITimeInterval) object.getMetadata().clone();		
		newMetadata.setStartAndEnd(newMetadata.getStart().plus(timeToShift), newMetadata.getEnd().plus(timeToShift));
		object.setMetadata((M) newMetadata);
		transfer(object);		
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: handle punctations
		//sendPunctuation(punctuation);
	}
}
