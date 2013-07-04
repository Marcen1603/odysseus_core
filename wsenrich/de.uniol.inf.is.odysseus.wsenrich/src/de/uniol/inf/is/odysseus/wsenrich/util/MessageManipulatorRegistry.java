package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableList;

/* Note: Based on a copy of TransportHandlerRegistry */
public class MessageManipulatorRegistry {
	
	static Logger logger = LoggerFactory
			.getLogger(MessageManipulatorRegistry.class);

	static private Map<String, IMessageManipulator> messageManipulators = new HashMap<String, IMessageManipulator>();

	static public void register(IMessageManipulator messageManipulator) {
		logger.debug("Register new Handler " + messageManipulator.getName());
		if (!messageManipulators.containsKey(messageManipulator.getName().toLowerCase())) {
			messageManipulators.put(messageManipulator.getName().toLowerCase(), messageManipulator);
		} else {
			logger.error("Handler with name " + messageManipulator.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IMessageManipulator messageManipulator){
		logger.debug("Remove handler "+messageManipulator.getName());
		messageManipulators.remove(messageManipulator.getName().toLowerCase());
	}
	
	static public IMessageManipulator getInstance(String name){
		IMessageManipulator ret = messageManipulators.get(name.toLowerCase());
		if (ret != null){
			return ret.createInstance();
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(messageManipulators.keySet());
	}

}