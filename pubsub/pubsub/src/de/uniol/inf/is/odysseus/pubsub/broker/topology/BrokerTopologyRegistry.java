package de.uniol.inf.is.odysseus.pubsub.broker.topology;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;


public class BrokerTopologyRegistry<T extends IStreamObject<?>> {

	static Logger logger = LoggerFactory.getLogger(BrokerTopologyRegistry.class);
	
	// Maps Type and the corresponding Topology
	static Map<String, IBrokerTopology<?>> brokerTopologyTypes = new HashMap<String, IBrokerTopology<?>>();
	
	// Maps Domainname and the corresponding Topology
	static Map<String, IBrokerTopology<?>> brokerTopologies = new HashMap<String, IBrokerTopology<?>>();
	
	
	public static void registerBrokertopologies(IBrokerTopology<?> brokerTopology) {
		logger.debug("Register new Broker Topology " + brokerTopology.getType());
		if (!brokerTopologyTypes.containsKey(brokerTopology.getType().toLowerCase())) {
			brokerTopologyTypes.put(brokerTopology.getType().toLowerCase(), brokerTopology);
		} else {
			logger.error("Broker Topology with name " + brokerTopology.getType()
					+ " already registered");
		}
	}
	
	
	public static void unregisterBrokertopologies(IBrokerTopology<?> brokerTopology) {
		logger.debug("Remove Broker Topology "+brokerTopology.getType());
		brokerTopologyTypes.remove(brokerTopology.getType().toLowerCase());
	}
	
	// public static <T> IBrokerTopology<?> getTopologyByTypeAndDomain(String topologyType, String domain) may be better
	public static IBrokerTopology<?> getTopologyByTypeAndDomain(String topologyType, String domain) {
		// Check if topology Type is valid
		if (!brokerTopologyTypes.containsKey(topologyType.toLowerCase())){
			logger.info("Topology Type: '"+ topologyType + "' is not valid.");
			return null;
		}
		
		if (brokerTopologies.containsKey(domain.toLowerCase())){
			IBrokerTopology<?> topology = brokerTopologies.get(domain.toLowerCase());
			if (topology.getType().toLowerCase().equals(topologyType)){
				// Broker with type and domain exists
				return topology;				
			} else {
				// Broker with domain exists, but has a different type
				logger.info("Domain: '"+ domain +"' already exists. It's not possible to register a Topology with the same domain and a different topologyType.");
				return null;
			}
		} else {
			// Broker with type and domain doesnt exists, create new Instance
			IBrokerTopology<?> topology = brokerTopologyTypes.get(topologyType.toLowerCase());
			IBrokerTopology<?> ret = topology.getInstance(domain);
			brokerTopologies.put(domain, ret);
			return ret;
		}
	}
	
	public static IBrokerTopology<?> getTopologyByDomain(String domain) {
		// Check if domain exists
		if (brokerTopologies.containsKey(domain.toLowerCase())){
			return brokerTopologies.get(domain.toLowerCase());			
		} else {
			logger.info("Topology with domain: '"+ domain + "' does not exists.");
			return null;
		}
		
	}

}
