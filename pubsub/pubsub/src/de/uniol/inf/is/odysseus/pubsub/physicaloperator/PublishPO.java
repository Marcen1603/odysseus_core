package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;

public class PublishPO extends AbstractSink<IStreamObject<?>>{
	
	//private String brokerName;

	@Override
	protected void process_next(IStreamObject<?> object, int port) {
		// Broker b = getBroker("Broker1");
		// b.transfer;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ????
		
	}

	@Override
	public AbstractSink<IStreamObject<?>> clone() {
		return new PublishPO();
	}

}
