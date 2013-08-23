package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.prictogram.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PictogramGroupRegistry {
	
	private static Map<String, IPictogramGroup<?>> groups = new HashMap<>();
	
	public static void addGroup(IPictogramGroup<?> group){		
		groups.put(group.getName(), group);
	}
	
	public static void removeGroup(String name){
		groups.remove(name);
	}
	
	public static void clearGroups(){
		groups.clear();
	}
	
	public static Map<String, IPictogramGroup<?>> getGroups(){
		return groups;
	}
	
	public static List<String> getGroupNames(){
		List<String> list = new ArrayList<>(groups.keySet());
		Collections.sort(list);
		return list;
	}

}
