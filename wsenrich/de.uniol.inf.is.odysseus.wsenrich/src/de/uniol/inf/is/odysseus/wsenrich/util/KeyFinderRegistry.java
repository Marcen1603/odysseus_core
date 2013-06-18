package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

/* Note: Based on a copy of TransportHandlerRegistry */
public class KeyFinderRegistry {
	
	static Logger logger = LoggerFactory
			.getLogger(KeyFinderRegistry.class);

	static private Map<String, IKeyFinder> keyFinders = new HashMap<String, IKeyFinder>();

	static public void register(IKeyFinder keyFinder) {
		logger.debug("Register new Handler " + keyFinder.getName());
		if (!keyFinders.containsKey(keyFinder.getName().toLowerCase())) {
			keyFinders.put(keyFinder.getName().toLowerCase(), keyFinder);
		} else {
			logger.error("Handler with name " + keyFinder.getName()
					+ " already registered");
		}
	}
	
	static public void remove(IKeyFinder keyFinder){
		logger.debug("Remove handler "+keyFinder.getName());
		keyFinders.remove(keyFinder.getName().toLowerCase());
	}
	
	static public IKeyFinder getInstance(String name){
		IKeyFinder ret = keyFinders.get(name.toLowerCase());
		if (ret != null){
			return ret.createInstance();
		}
		logger.error("No handler with name "+name+" found!");
		return null;
	}
	
	public static ImmutableList<String> getHandlerNames() {
		return ImmutableList.copyOf(keyFinders.keySet());
	}

}
