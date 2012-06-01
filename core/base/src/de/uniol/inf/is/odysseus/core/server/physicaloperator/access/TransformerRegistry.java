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
		if (transformer.containsKey(handler.getName().toLowerCase())){
			logger.error("Input Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		transformer.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(ITransformer<?,?> handler){
		logger.debug("Remove Transformer "+handler.getName());
		transformer.remove(handler.getName().toLowerCase());
	}
	
	public static ITransformer<?,?> getTransformer(String name){
		return transformer.get(name.toLowerCase());
	}
}
