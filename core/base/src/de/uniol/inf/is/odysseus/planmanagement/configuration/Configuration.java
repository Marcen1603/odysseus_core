package de.uniol.inf.is.odysseus.planmanagement.configuration;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class is uses an type safe map for configuration issues. Each entry could have a value with a special
 * type. This map is for example used to provide system options, query build
 * parameter and optimization parameter.
 * 
 * If a value of an entry changes registered objects are informed.
 * 
 * @author Wolf Bauer
 * 
 * @param <T>
 *            type of the entries which this map accepts.
 */
public class Configuration<T extends ISetting<?>> extends
		ValueChangeHandler<T> {

	/**
	 * Map of entries which are stored.
	 */
	protected HashMap<Type, T> entry = new HashMap<Type, T>();


	/**
	 * Creates a new map.
	 * 
	 * @param entries
	 *            Entries which should be stored.
	 */
	public Configuration(T... entries) {
		if (entries != null) {
			for (T hasValue : entries) {
				set(hasValue, false);
			}

		}
	}

	/**
	 * Returns an entry for a defined type.
	 * 
	 * @param entryType
	 *            Type of the searched entry.
	 * @return Entry for the defined type.
	 */
	@SuppressWarnings("unchecked")
	public <S extends T>S get(Class<S> entryType) {
		return (S) this.entry.get(entryType);
	}

	/**
	 * Sets an entry and sends a entry change event if requested.
	 * 
	 * @param entry
	 *            The new entry to set.
	 * @param sendEvent
	 *            TRUE: Send a change event. FALSE: Send no event.
	 */
	public void set(T entry, boolean sendEvent) {
		Type settingType = entry.getClass();
		this.entry.put(settingType, entry);
		settingChanged(entry);
	}

	/**
	 * Sets an entry.
	 * 
	 * @param entry
	 *            The new entry to set.
	 */
	public void set(T entry) {
		set(entry, true);
	}

	/**
	 * Checks if this map contains a special entry.
	 * 
	 * @param entryType
	 *            Type of the searched entry.
	 * @return TRUE: Map contains an entry of the requested type. FALSE: else.
	 */
	public boolean contains(Class<?> entryType) {
		return this.entry.containsKey(entryType);
	}

	public Collection<T> values(){
		return this.entry.values();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (T entry : this.entry.values()) {
			if (!result.equals("")) {
				result += AppEnv.LINE_SEPARATOR;
			}
			result += "entry: " + entry.toString() + ", value: "
					+ entry.getValue();
		}

		return result;
	}
}
