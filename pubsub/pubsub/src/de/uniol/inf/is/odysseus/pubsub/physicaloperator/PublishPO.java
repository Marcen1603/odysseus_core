package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerService;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;

public class PublishPO<T extends IStreamObject<?>> extends AbstractSink<T>{
	
	private String brokerName;

	public PublishPO(String brokername){
		super();
		this.brokerName = brokername;
	}
	
	@Override
	protected void process_next(T object, int port) {
		IBroker<T> b = BrokerService.getBrokerByName(brokerName);
		b.transfer(object);
	}

	@Override
	public AbstractSink<T> clone() {
		return new PublishPO<T>(this.brokerName);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
