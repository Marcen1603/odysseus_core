package de.uniol.inf.is.odysseus.iql.basic.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IQLUtils {

	
	public static <T> T[] toArray1(List<T> list) {
		T[] result = (T[]) new Object[list.size()];
		int i = 0;
		for (T element : list) {
			result[i++] = element;
		}
		return result;
	}
	
	public static <T> T[][] toArray2(List<List<T>> list) {
		T[][] result = (T[][]) new Object[list.size()][];
		int i = 0;
		for (List<T> element : list) {
			result[i++] = toArray1(element);
		}
		return result;
	}
	
	public static <T> T[][][] toArray3(List<List<List<T>>> list) {
		T[][][] result = (T[][][]) new Object[list.size()][];
		int i = 0;
		for (List<List<T>> element : list) {
			result[i++] = toArray2(element);
		}
		return result;
	}

	
	public static List toList(Object[] array) {
		List result = new ArrayList();
		for (Object obj : array) {
			result.add(obj);
		}
		return result;
	}
	
	public static List toList(Object[][] array) {
		List result = new ArrayList();
		for (Object[] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}
	
	public static List toList(Object[][][] array) {
		List result = new ArrayList();
		for (Object[][] obj : array) {
			result.add(toList(obj));
		}
		return result;
	}

	
	public static List createList(Object ... obj) {
		return new ArrayList(Arrays.asList(obj));
	}

	
//	public static List createList(List list) {		
//		return new ArrayList(list);
//	}
	
	public static List createEmptyList() {
		return new ArrayList();
	}
	
	public static Map createMap(Object ... obj) {
		Map map = new HashMap();
		for (int i = 0; i < obj.length; i = i+2) {
			map.put(obj[i], obj[i+1]);
		}
		return map;
	}
	
	public static Map createMap(Map obj) {
		return new HashMap(obj);
	}
	
	public static Map createEmptyMap() {
		return new HashMap();
	}
}
