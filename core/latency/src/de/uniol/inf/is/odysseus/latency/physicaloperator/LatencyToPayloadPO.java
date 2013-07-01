package de.uniol.inf.is.odysseus.latency.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class LatencyToPayloadPO<M extends ILatency, T extends IStreamObject<M>> extends AbstractPipe<T, Tuple<M>>{

	public LatencyToPayloadPO() {
		
	}
	
	public LatencyToPayloadPO(LatencyToPayloadPO<M, T> latencyToPayloadPO) {
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		long latency = object.getMetadata().getLatency();
		Tuple<M> tuple = new Tuple<M>(1, false);
		tuple.setAttribute(0, latency);
		tuple.setMetadata(object.getMetadata());
		transfer(tuple);
	}

	@Override
	public LatencyToPayloadPO<M, T> clone() {
		return new LatencyToPayloadPO<M,T>(this);
	}

}
