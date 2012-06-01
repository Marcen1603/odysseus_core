package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformerRegistry {

	static Logger logger = LoggerFactory.getLogger(TransformerRegistry.class);

	static private Map<String, ITransformer<?,?>> transformer = new HashMap<String, ITransformer<?,?>>();
	
	public static void register(ITransformer<?,?> handler){
		logger.debug("Register Transformer "+handler.getName());
		if (transformer.containsKey(handler)){
			logger.error("Input Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		transformer.put(handler.getName(),handler);
	}
	
	public static void remove(ITransformer<?,?> handler){
		logger.debug("Remove Transformer "+handler.getName());
		transformer.remove(handler.getName());
	}
	
	public static ITransformer<?,?> getDataHandler(String name){
		return transformer.get(name);
	}
}
