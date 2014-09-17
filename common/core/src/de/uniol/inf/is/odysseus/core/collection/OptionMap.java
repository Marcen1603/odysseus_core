package de.uniol.inf.is.odysseus.core.collection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class provides options that can be set once and has a marker for
 * elements that have been read
 * 
 * @author Marco Grawunder
 *
 */

public class OptionMap {

	private Map<String, String> optionMap = new HashMap<>();
	private Map<String, Boolean> keyRead = new HashMap<String, Boolean>();

	public OptionMap() {
	}

	public OptionMap(Map<String, String> optionMap) {
		if (optionMap != null) {
			for (Entry<String, String> e : optionMap.entrySet()) {
				overwriteOption(e.getKey(), e.getValue());
			}
		}
	}

	public void setOption(String key, String value) {
		if (optionMap.containsKey(key)) {
			throw new IllegalStateException("Option " + key
					+ " already set with value " + optionMap.get(key));
		}
		overwriteOption(key, value);
	}

	public void overwriteOption(String key, String value) {
		optionMap.put(key, value);
		keyRead.put(key, Boolean.FALSE);
	}

	public boolean containsKey(String key) {
		return optionMap.containsKey(key);
	}

	public String get(String key) {
		if (optionMap.containsKey(key)) {
			keyRead.put(key, Boolean.TRUE);
		}
		return optionMap.get(key);
	}

	public String get(String key, String defaultValue) {
		String v = get(key);
		return v != null ? v : defaultValue;
	}

	public boolean getBoolean(String key, Boolean defaultValue) {
		String v = get(key);
		return v != null ? Boolean.parseBoolean(v) : defaultValue;
	}

	public char getChar(String key, char defaultValue) {
		String v = get(key);
		return v != null ? v.toCharArray()[0] : defaultValue;
	}

	public int getInt(String key, int defaultValue) {
		String v = get(key);
		return v != null ? Integer.parseInt(v) : defaultValue;
	}

	public long getLong(String key, long defaultValue) {
		String v = get(key);
		return v != null ? Long.parseLong(v) : defaultValue;
	}

	public double getDouble(String key, double defaultValue) {
		String v = get(key);
		return v != null ? Double.parseDouble(v) : defaultValue;
	}
	
	public List<String> getUnreadOptions() {
		List<String> unread = new LinkedList<String>();
		for (Entry<String, Boolean> e : keyRead.entrySet()) {
			if (!e.getValue()) {
				unread.add(e.getKey());
			}
		}
		return unread;
	}

	public List<String> checkRequired(String... keys){
		List<String> missing = new LinkedList<String>();
		for (String k:keys){
			if (!containsKey(k)){
				missing.add(k);
			}
		}
		return missing;
	}
	
	public void checkRequiredException(String... keys){
		List<String> missing = checkRequired(keys);
		if (!missing.isEmpty()){
			throw new IllegalArgumentException("The following required options are not set: "+missing);
		}
	}
}
