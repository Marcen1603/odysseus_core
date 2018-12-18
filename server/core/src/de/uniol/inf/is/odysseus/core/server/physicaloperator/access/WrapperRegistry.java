package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrapperRegistry {
	
	static List<String> wrapperNames = new ArrayList<String>();
	
	public void registerWrapper(String name){
		wrapperNames.add(name.toUpperCase());
	}
	
	public void removeWrapper(String name){
		wrapperNames.remove(name.toUpperCase());
	}
	
	public static List<String> getWrapperNames(){
		return Collections.unmodifiableList(wrapperNames);
	}
	
	public static boolean containsWrapper(String name){
		return wrapperNames.contains(name.toUpperCase());
	}

}
