package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.intervalapproach.TimestampToPayloadPO;

public class RelationalTimestampToPayloadPO extends
		TimestampToPayloadPO<ITimeInterval, RelationalTuple<ITimeInterval>> {

	public RelationalTimestampToPayloadPO(
			RelationalTimestampToPayloadPO relationalTimestampToPayloadPO) {
		super(relationalTimestampToPayloadPO);
	}

	public RelationalTimestampToPayloadPO() {
	}

	@Override
	protected void process_next(RelationalTuple<ITimeInterval> object, int port) {
		int inputSize = object.getAttributeCount();
		RelationalTuple<ITimeInterval> out = new RelationalTuple<ITimeInterval>(
				object.getAttributeCount()+2);
		
		for (int i=0;i<inputSize;i++){
			out.setAttribute(i, object.getAttribute(i));
		}
		
		if (object.getMetadata().getStart().isInfinite()) {
			out.setAttribute(inputSize,-1L);
		} else {
			out.setAttribute(inputSize, object.getMetadata().getStart().getMainPoint());
		}
		if (object.getMetadata().getEnd().isInfinite()) {
			out.setAttribute(inputSize+1,-1L);
		} else {
			out.setAttribute(inputSize+1,object.getMetadata().getEnd().getMainPoint());
		}
		transfer(out);
	}

	@Override
	public AbstractPipe<RelationalTuple<ITimeInterval>, RelationalTuple<ITimeInterval>> clone() {
		return new RelationalTimestampToPayloadPO(this);
	}

}
