package de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IConnectionForWebservices;

/* Note: Based on a copy of TransportHandlerRegistry */
public class ConnectionForWebservicesRegistry {
	
	static Logger logger = LoggerFactory
			.getLogger(ConnectionForWebservicesRegistry.class);

	static private Map<String, IConnectionForWebservices> connections = new HashMap<String, IConnectionForWebservices>();

	static public void register(IConnectionForWebservices connection) {
		logger.trace("Register new Handler " + connection.getName());
		if (!connections.containsKey(connection.getName().toLowerCase())) {
			connections.put(connection.getName().toLowerCase(), connection);
		} else {
			logger.error("Handler with name " + connection.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IConnectionForWebservices connection){
		logger.trace("Remove handler "+connection.getName());
		connections.remove(connection.getName().toLowerCase());
	}
	
	static public IConnectionForWebservices getInstance(String name){
		IConnectionForWebservices ret = connections.get(name.toLowerCase());
		if (ret != null){
			return ret.createInstance();
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(connections.keySet());
	}

}
