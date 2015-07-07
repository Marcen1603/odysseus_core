package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.util.HashMap;
import java.util.Map;


public class NeededMEPFunctions {
	
	private static Map<String, String> mepFunctions  = new HashMap<String, String>();

	public static void addMEPFunction(String fullClassName, String simpleClassName){
		
		if(!mepFunctions.containsKey(fullClassName)){
			mepFunctions.put(fullClassName,simpleClassName);
		}
	
	}
	
	public static Map<String, String> getNeededMEPFunctions(){
		return mepFunctions;
	}
}
