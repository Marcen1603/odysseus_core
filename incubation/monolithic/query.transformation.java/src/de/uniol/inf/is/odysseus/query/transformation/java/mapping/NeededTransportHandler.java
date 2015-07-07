package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

public class NeededTransportHandler {

	private static Map<String, String> transportHandler  = new HashMap<String, String>();

	public static void addTransportHandler(String fullClassName, String simpleClassName){
		
		if(!transportHandler.containsKey(fullClassName)){
			transportHandler.put(fullClassName,simpleClassName);
		}
	}
	
	public static Map<String, String> getNeededTransportHandler(){
		return transportHandler;
	}
	
	
}
