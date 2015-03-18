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
				overwriteOption(e.getKey().toLowerCase(), e.getValue());
			}
		}
	}

	public void setOption(String key, String value) {
		if (optionMap.containsKey(key.toLowerCase())) {
			throw new IllegalStateException("Option " + key.toLowerCase()
					+ " already set with value " + optionMap.get(key));
		}
		overwriteOption(key.toLowerCase(), value);
	}

	public void overwriteOption(String key, String value) {
		optionMap.put(key.toLowerCase(), value);
		keyRead.put(key.toLowerCase(), Boolean.FALSE);
	}

	public boolean containsKey(String key) {
		return optionMap.containsKey(key.toLowerCase());
	}

	public String get(String key) {
		if (optionMap.containsKey(key.toLowerCase())) {
			keyRead.put(key.toLowerCase(), Boolean.TRUE);
		}
		return optionMap.get(key.toLowerCase());
	}

	public String get(String key, String defaultValue) {
		String v = get(key.toLowerCase());
		return v != null ? v : defaultValue;
	}

    public boolean getBoolean(String key, boolean defaultValue) {
		String v = get(key);
		return v != null ? Boolean.parseBoolean(v) : defaultValue;
	}

	public char getChar(String key, char defaultValue) {
		String v = get(key);
		return v != null ? v.toCharArray()[0] : defaultValue;
	}

    public short getShort(String key, short defaultValue) {
        String v = get(key);
        return v != null ? Short.parseShort(v) : defaultValue;
    }

	public int getInt(String key, int defaultValue) {
		String v = get(key);
		return v != null ? Integer.parseInt(v) : defaultValue;
	}

	public long getLong(String key, long defaultValue) {
		String v = get(key);
		return v != null ? Long.parseLong(v) : defaultValue;
	}

    public float getFloat(String key, float defaultValue) {
        String v = get(key);
        return v != null ? Float.parseFloat(v) : defaultValue;
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
			if (!containsKey(k.toLowerCase())){
				missing.add(k.toLowerCase());
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
