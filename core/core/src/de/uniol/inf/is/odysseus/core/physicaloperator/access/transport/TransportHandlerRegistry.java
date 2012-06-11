package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportHandlerRegistry {

	static Logger logger = LoggerFactory.getLogger(TransportHandlerRegistry.class);

	static private Map<String, ITransportHandler> handlers = new HashMap<String, ITransportHandler>();

	static public void register(ITransportHandler handler) {
		logger.debug("Register new Handler " + handler.getName());
		if (!handlers.containsKey(handler.getName())) {
			handlers.put(handler.getName(), handler);
		} else {
			logger.error("Handler with name " + handler.getName()
					+ " already registered");
		}
	}
	
	static public void remove(ITransportHandler handler){
		logger.debug("Remove handler "+handler.getName());
		handlers.remove(handler.getName());
	}
	
	static public ITransportHandler getInstance(String name, Map<String, String> options){
		ITransportHandler ret = handlers.get(name);
		if (ret != null){
			return ret.createInstance(options);
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
}
