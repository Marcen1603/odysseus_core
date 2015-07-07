package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;

public class NeededDataHandler {

	private static Map<String, String> dataHandler  = new HashMap<String, String>();

	public static void addDataHandler(String fullClassName, String simpleClassName){
		
		if(!dataHandler.containsKey(fullClassName)){
			dataHandler.put(fullClassName,simpleClassName);
		}
	}
	
	public static Map<String, String> getNeededDataHandler(){
		return dataHandler;
	}
	
	
}
