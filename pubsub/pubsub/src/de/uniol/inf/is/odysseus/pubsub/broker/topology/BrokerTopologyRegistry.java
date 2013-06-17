package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


public class BrokerTopologyRegistry<T extends IStreamObject<?>> {

	static Logger logger = LoggerFactory.getLogger(BrokerTopologyRegistry.class);
	
	static Map<String, IBrokerTopology<?>> brokerTopologies = new HashMap<String, IBrokerTopology<?>>();
	
	public static void registerBrokers(IBrokerTopology<?> brokerTopology) {
		logger.debug("Register new Broker Topology " + brokerTopology.getName());
		if (!brokerTopologies.containsKey(brokerTopology.getName().toLowerCase())) {
			brokerTopologies.put(brokerTopology.getName().toLowerCase(), brokerTopology);
		} else {
			logger.error("Broker Topology with name " + brokerTopology.getName()
					+ " already registered");
		}
	}
	
	public static void unregisterBrokers(IBrokerTopology<?> brokerTopology) {
		logger.debug("Remove Broker Topology "+brokerTopology.getName());
		brokerTopologies.remove(brokerTopology.getName().toLowerCase());
	}
	
	/**
	 * returns a broker with a given name
	 * @param brokername
	 * @return
	 */
	public static IBrokerTopology<?> getTopologyByType(String topologyType) {
		return brokerTopologies.get(topologyType.toLowerCase());
	}

}
