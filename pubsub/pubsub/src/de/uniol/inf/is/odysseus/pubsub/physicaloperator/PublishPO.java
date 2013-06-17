package de.uniol.inf.is.odysseus.pubsub.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;

public class PublishPO<T extends IStreamObject<?>> extends AbstractSink<T>{
	
	private String topologyType;

	public PublishPO(String topologyType){
		super();
		this.topologyType = topologyType;
	}
	
	@Override
	protected void process_next(T object, int port) {
		IBrokerTopology<?> b = BrokerTopologyRegistry.getTopologyByType(topologyType);
		b.transfer(object);
	}

	@Override
	public AbstractSink<T> clone() {
		return new PublishPO<T>(this.topologyType);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

}
