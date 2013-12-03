package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrapperRegistry {
	
	static List<String> wrapperNames = new ArrayList<String>();
	
	static public void registerWrapper(String name){
		wrapperNames.add(name);
	}
	
	static public void removeWrapper(String name){
		wrapperNames.remove(name);
	}
	
	static public List<String> getWrapperNames(){
		return Collections.unmodifiableList(wrapperNames);
	}
	
	static public boolean containsWrapper(String name){
		return wrapperNames.contains(name);
	}

}
