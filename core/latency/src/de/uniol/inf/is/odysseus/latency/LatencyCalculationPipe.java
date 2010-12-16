package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class LatencyCalculationPipe<T extends IMetaAttributeContainer<? extends ILatency>> extends AbstractPipe<T, T>{

	public LatencyCalculationPipe(){}
	
	public LatencyCalculationPipe(
			LatencyCalculationPipe<T> latencyCalculationPipe) {	
		super(latencyCalculationPipe);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected void process_next(T object, int port) {
		object.getMetadata().setLatencyEnd(System.nanoTime());
		transfer(object);
	}
	
	@Override
	public LatencyCalculationPipe<T> clone(){
		return new LatencyCalculationPipe<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}


}
