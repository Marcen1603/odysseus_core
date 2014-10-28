package de.uniol.inf.is.odysseus.wsenrich.util.serviceregistry;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.ISoapMessageCreator;

/* Note: Based on a copy of TransportHandlerRegistry */
public class SoapMessageCreatorRegistry {
	
	static Logger logger = LoggerFactory
			.getLogger(SoapMessageCreatorRegistry.class);

	static private Map<String, ISoapMessageCreator> messageCreators = new HashMap<String, ISoapMessageCreator>();

	static public void register(ISoapMessageCreator messageCreator) {
		logger.trace("Register new Handler " + messageCreator.getName());
		if (!messageCreators.containsKey(messageCreator.getName().toLowerCase())) {
			messageCreators.put(messageCreator.getName().toLowerCase(), messageCreator);
		} else {
			logger.error("Handler with name " + messageCreator.getName()
					+ " already registered");
		}
	}
	
	static public void remove(ISoapMessageCreator messageCreator){
		logger.trace("Remove handler "+messageCreator.getName());
		messageCreators.remove(messageCreator.getName().toLowerCase());
	}
	
	static public ISoapMessageCreator getInstance(String name, String wsdlLocation, String operationName){
		ISoapMessageCreator ret = messageCreators.get(name.toLowerCase());
		if (ret != null){
			return ret.createInstance(wsdlLocation, operationName);
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(messageCreators.keySet());
	}

}

