package de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IMessageManipulator;

/* Note: Based on a copy of TransportHandlerRegistry */
public class MessageManipulatorRegistry {
	
	static Logger logger = LoggerFactory
			.getLogger(MessageManipulatorRegistry.class);

	static private Map<String, IMessageManipulator> messageManipulators = new HashMap<String, IMessageManipulator>();

	static public void register(IMessageManipulator messageManipulator) {
		logger.trace("Register new Handler " + messageManipulator.getName());
		if (!messageManipulators.containsKey(messageManipulator.getName().toLowerCase())) {
			messageManipulators.put(messageManipulator.getName().toLowerCase(), messageManipulator);
		} else {
			logger.error("Handler with name " + messageManipulator.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IMessageManipulator messageManipulator){
		logger.trace("Remove handler "+messageManipulator.getName());
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