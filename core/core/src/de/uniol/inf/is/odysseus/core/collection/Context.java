package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to handle different contexts (i.e. can provider further information)
 * 
 * @author Marco Grawunder
 *
 */

public class Context {

	private final Map<String, Serializable> contextMap = new HashMap<>();
		
	public void put(String key, Serializable value){
		if (contextMap.containsKey(key)){
			throw new IllegalArgumentException("Key "+key+" already used. Delete first.");
		}
		contextMap.put(key, value);
	}
	
	public Serializable get(String key){
		return contextMap.get(key);
	}

	public static Context emptyContext() {
		return new Context();
	}
}
