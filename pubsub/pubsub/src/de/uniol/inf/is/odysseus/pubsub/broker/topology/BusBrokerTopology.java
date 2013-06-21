package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.SimpleBroker;

public class BusBrokerTopology<T extends IStreamObject<?>> extends AbstractBrokerTopology<T> {

	private final String TOPOLOGY_TYPE = "BusTopology";
	private List<IBroker<T>> brokerBus = new ArrayList<IBroker<T>>();;
	
	public BusBrokerTopology(){
		// needed for OSGi
	}
	
	public BusBrokerTopology(String domain){
		setDomain(domain);
	}
	
	@Override
	public String getType(){
		return TOPOLOGY_TYPE;
	}


	@Override
	public List<IBroker<T>> getAdressedBrokers() {
		// It's a Bus, so every Broker needs to be adressed, but only Brokers with Subscriptions
		return brokerBus;
	}


	@Override
	IBroker<T> getBrokerByName(String name) {
		// Find Broker in existing Bus
		for (IBroker<T> broker : brokerBus) {
			if (broker.getName().toLowerCase().equals(name.toLowerCase())){
				return broker;
			}
		}
		// If Broker with given name doesnt exist, create new one and return it
		IBroker<T> newBroker = new SimpleBroker<T>(name, super.getDomain());
		brokerBus.add(newBroker);
		return newBroker;
	}

	@Override
	public <E extends IStreamObject<?>> IBrokerTopology<E> getInstance(String domain) {
		return new BusBrokerTopology<E>(domain);
	}

}
