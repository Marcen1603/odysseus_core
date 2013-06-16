package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


public class BrokerService<T extends IStreamObject<?>> {

	static List<IBroker> brokers;
	
	public static void registerBrokers() {
		brokers = new ArrayList<IBroker>();
		brokers.add(new SimpleBroker("Broker_0"));			
	}
	
	public static void unregisterBrokers() {
		brokers.clear();
	}
	
	/**
	 * returns a broker with a given name
	 * @param brokername
	 * @return
	 */
	public static IBroker getBrokerByName(String brokername) {
		for (IBroker broker : brokers) {
			if (broker.getName().equals(brokername)){
				return broker;
			}
		}
		return null;
	}

}
