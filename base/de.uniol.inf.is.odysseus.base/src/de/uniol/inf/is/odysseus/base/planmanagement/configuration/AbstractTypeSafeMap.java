package de.uniol.inf.is.odysseus.base.planmanagement.configuration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractTypeSafeMap<T extends IMapValue<?>> implements
		IValueChangeHandler<T> {

	protected HashMap<Type, T> entry = new HashMap<Type, T>();

	private ArrayList<IValueChangeListener<T>> changeListener = new ArrayList<IValueChangeListener<T>>();

	public AbstractTypeSafeMap(T... entries) {
		if (entries != null) {
			for (T hasValue : entries) {
				set(hasValue, false);
			}
			
		}
	}

	public T get(Type entryType) {
		return this.entry.get(entryType);
	}

	public void set(T entry, boolean sendEvent) {
		Type settingType = entry.getClass();
		this.entry.put(settingType, entry);
		settingChanged(entry);
	}

	public void set(T entry) {
		set(entry, true);
	}

	public boolean contains(Class<?> entryType) {
		return this.entry.containsKey(entryType);
	}

	private void settingChanged(T newEntry) {
		for (IValueChangeListener<T> listener : this.changeListener) {
			listener.settingChanged(newEntry);
		}
	}

	@Override
	public void addValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.add(listener);
	}

	@Override
	public void removeValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.remove(listener);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (T entry : this.entry.values()) {
			if(!result.equals("")) {
				result += AppEnv.LINE_SEPERATOR;
			}
			result += "entry: " + entry.toString() + ", value: " + entry.getValue(); 
		}
		
		return result;
	}
}
