package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is an type safe map. Each entry could have a value with a special
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
public abstract class AbstractTypeSafeMap<T extends IMapValue<?>> implements
		IValueChangeHandler<T> {

	/**
	 * Map of entries which are stored.
	 */
	protected HashMap<Type, T> entry = new HashMap<Type, T>();

	/**
	 * List of objects which are informed if an entry value changes.
	 */
	private ArrayList<IValueChangeListener<T>> changeListener = new ArrayList<IValueChangeListener<T>>();

	/**
	 * Creates a new map.
	 * 
	 * @param entries
	 *            Entries which should be stored.
	 */
	public AbstractTypeSafeMap(T... entries) {
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

	/**
	 * Sends a setting changed event to all registered listeners.
	 * 
	 * @param newEntry Entry which changed. 
	 */
	private void settingChanged(T newEntry) {
		for (IValueChangeListener<T> listener : this.changeListener) {
			listener.settingChanged(newEntry);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.configuration.IValueChangeHandler#addValueChangeListener(de.uniol.inf.is.odysseus.base.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.add(listener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.configuration.IValueChangeHandler#removeValueChangeListener(de.uniol.inf.is.odysseus.base.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.remove(listener);
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
