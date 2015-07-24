package de.uniol.inf.is.odysseus.iql.basic.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IQLUtils {

	
	public static List createList(Object ... obj) {
		return new ArrayList<Object>(Arrays.asList(obj));
	}
	
	public static List createList(List obj) {
		return new ArrayList<Object>(obj);
	}
	
	public static List createEmptyList() {
		return new ArrayList<Object>();
	}
	
	public static Map createMap(Object ... obj) {
		Map map = new HashMap<>();
		for (int i = 0; i < obj.length; i = i+2) {
			map.put(obj[i], obj[i+1]);
		}
		return map;
	}
	
	public static Map createMap(Map obj) {
		return new HashMap(obj);
	}
	
	public static Map createEmptyMap() {
		return new HashMap<>();
	}
}
