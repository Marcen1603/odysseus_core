package de.uniol.inf.is.odysseus.core.objecthandler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;

public class InputDataHandlerRegistry{

	static Logger logger = LoggerFactory.getLogger(InputDataHandlerRegistry.class);
	
	private static Map<String, IInputDataHandler<?, ?>> inputDataHandlers = new HashMap<String, IInputDataHandler<?, ?>>();
	
	static public void register(IInputDataHandler<?, ?> handler){
		logger.debug("Registering handler  "+handler.getName());
		if (inputDataHandlers.containsKey(handler.getName().toLowerCase())){
			logger.error("Handler with name "+handler.getName()+" is already registered!");
			return;
		}
		inputDataHandlers.put(handler.getName().toLowerCase(),handler);
	}
	
	public static void remove(IInputDataHandler<?, ?> handler){
		logger.debug("Remove Handler "+handler.getName());
		inputDataHandlers.remove(handler.getName().toLowerCase());
	}
	
	public static IInputDataHandler<?, ?> get(String name){
		return inputDataHandlers.get(name.toLowerCase());
	}
}
