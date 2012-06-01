package de.uniol.inf.is.odysseus.core.objecthandler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectHandlerRegistry{

	static Logger logger = LoggerFactory.getLogger(ObjectHandlerRegistry.class);
	
	private static Map<String, IObjectHandler<?>> objectHandlers = new HashMap<String, IObjectHandler<?>>();
	
	static public void register(IObjectHandler<?> handler){
		logger.debug("Registering new object handler  "+handler.getName());
		if (objectHandlers.containsKey(handler.getName().toLowerCase())){
			logger.error("Object Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		objectHandlers.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(IObjectHandler<?> handler){
		logger.debug("Remove object Handler "+handler.getName());
		objectHandlers.remove(handler.getName().toLowerCase());
	}
	
	public static IObjectHandler<?> get(String name){
		return objectHandlers.get(name.toLowerCase());
	}
}
