package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

public class NeededProtocolHandler {

	private static Map<String, String> protocolHandler  = new HashMap<String, String>();

	public static void addProtocolHandler(String fullClassName, String simpleClassName){
		
		if(!protocolHandler.containsKey(fullClassName)){
			protocolHandler.put(fullClassName,simpleClassName);
		}
	}
	
	public static Map<String, String> getNeededProtocolHandler(){
		return protocolHandler;
	}
	
	
}
