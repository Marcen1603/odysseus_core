package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class ProtocolHandlerRegistry {

	static Logger logger = LoggerFactory.getLogger(ProtocolHandlerRegistry.class);

	static private Map<String, IProtocolHandler<?>> handlers = new HashMap<String, IProtocolHandler<?>>();

	static public void register(IProtocolHandler<?> handler) {
		logger.debug("Register new Handler " + handler.getName());
		if (!handlers.containsKey(handler.getName())) {
			handlers.put(handler.getName(), handler);
		} else {
			logger.error("Handler with name " + handler.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IProtocolHandler<?> handler){
		logger.debug("Remove handler "+handler.getName());
		handlers.remove(handler.getName());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public IProtocolHandler<?> getInstance(String name, Map<String, String> options, 
			ITransportHandler transportHandler, IDataHandler dataHandler, ITransferHandler transfer){
		IProtocolHandler<?> ret = handlers.get(name);
		if (ret != null){
			return ret.createInstance(options, transportHandler, dataHandler, transfer);
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
	@SuppressWarnings({ "rawtypes"})
	static public IProtocolHandler<?> getInstance(String name, Map<String, String> options, 
			ITransportHandler transportHandler, IDataHandler dataHandler){
		return getInstance(name, options, transportHandler, dataHandler, null);
	}
}
