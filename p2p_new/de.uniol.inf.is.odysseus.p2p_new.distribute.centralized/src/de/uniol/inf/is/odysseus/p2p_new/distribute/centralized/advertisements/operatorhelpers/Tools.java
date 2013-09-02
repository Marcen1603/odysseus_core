package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.HashMap;
import java.util.Map;

public class Tools {
	/**
	 *  re-creates an int[]-array which was converted to a String via Arrays.toString()
	 */
	public static int[] fromStringToIntArray(String s) {
		// remove the brackets
		s= s.replace("[", "");
		s= s.replace("]", "");
		// split at the commas
		String[] elements = s.split(", ");
		int[] result = new int[elements.length];
		// parse
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(elements[i]);
		}
		return result;
	}
	
	/**
	 *  re-creates a String[]-array which was converted to a String via Arrays.toString()
	 */
	public static String[] fromStringToStringArray(String s) {
		// remove the brackets
		s= s.replace("[", "");
		s= s.replace("]", "");
		// split at the commas
		String[] elements = s.split(", ");
		String[] result = new String[elements.length];
		// parse
		for (int i = 0; i < result.length; i++) {
			result[i] = elements[i];
		}
		return result;
	}
	/**
	 * re-creates a Map<String,String>-object from its toString()-representation
	 */
	public static Map<String,String> fromStringToMap(String s) {
		Map<String,String> result = new HashMap<String,String>();
		if(s.equals("")) {
			return null;
		}
		s= s.replace("{", "");
		s= s.replace("}", "");
		String[] elements = s.split(", ");
		for(int i = 0; i < elements.length; i++) {
			String[] keyValuePair = elements[i].split("=");
			String key = keyValuePair[0];
			String value = keyValuePair[1];
			result.put(key, value);
		}
		return result;
	}
	
}
