package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class BrokerPO <T extends IStreamObject<?>> extends AbstractPipe<T, T>{

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// TODO Publish/Subscribe Logic
		
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new BrokerPO<T>();
	}

}
