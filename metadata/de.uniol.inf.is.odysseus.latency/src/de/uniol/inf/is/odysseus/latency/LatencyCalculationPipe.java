package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class LatencyCalculationPipe<T extends IMetaAttribute<? extends ILatency>> extends AbstractPipe<T, T>{

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected void process_next(T object, int port) {
		object.getMetadata().setLatencyEnd(System.nanoTime());
		transfer(object);
	}

}
