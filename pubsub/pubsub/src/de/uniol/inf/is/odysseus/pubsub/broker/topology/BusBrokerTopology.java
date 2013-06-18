package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.SimpleBroker;

public class BusBrokerTopology<T extends IStreamObject<?>> extends AbstractBrokerTopology<T> {

	private final String TOPOLOGY_NAME = "BusTopology";
	private List<IBroker<T>> brokerBus;
	
	public BusBrokerTopology(){
		brokerBus = new ArrayList<IBroker<T>>();
		// add first Broker to Bus
		brokerBus.add(new SimpleBroker<T>("Broker_0"));
	}
	
	@Override
	public String getName(){
		return TOPOLOGY_NAME;
	}


	@Override
	public List<IBroker<T>> getAdressedBrokers() {
		// It's a Bus, so every Broker needs to be adressed, but only Brokers with Subscriptions
		List<IBroker<T>> ret = new ArrayList<IBroker<T>>(); 
		for (IBroker<T> broker : brokerBus) {
			if (broker.hasSubscriptions()){
				ret.add(broker);
			}
		}
		
		return ret;
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
		IBroker<T> newBroker = new SimpleBroker<T>(name);
		brokerBus.add(newBroker);
		return newBroker;
	}
}
