package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.SimpleBroker;

public class SingleBrokerTopology<T extends IStreamObject<?>> extends AbstractBrokerTopology<T>{

	private final String TOPOLOGY_NAME = "SingleBroker";
	private IBroker<T> singleBroker;
	
	public SingleBrokerTopology(){
		singleBroker = new SimpleBroker<T>("Broker_0");
	}
	
	public String getName(){
		return TOPOLOGY_NAME;
	}

	@Override
	public List<IBroker<T>> getAdressedBrokers() {
		// Only one Broker available
		List<IBroker<T>> ret = new ArrayList<IBroker<T>>();
		ret.add(singleBroker);
		return ret;
	}

	@Override
	IBroker<T> getBrokerByName(String name) {
		// Single Broker architecture, ignore name
		return singleBroker;
	}

}
