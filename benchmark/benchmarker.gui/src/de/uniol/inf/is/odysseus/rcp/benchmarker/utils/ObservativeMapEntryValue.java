package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.util.Map;

import org.eclipse.core.databinding.observable.ObservableTracker;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

public class ObservativeMapEntryValue<K, V> extends AbstractObservableValue {

	private Map.Entry<K, V> entry;
	
	public ObservativeMapEntryValue(Map.Entry<K, V> entry) {
		super();
		this.entry = entry;
	}

	@Override
	public Object getValueType() {
		return entry.getValue().getClass();
	}

	@Override
	protected Object doGetValue() {
		return entry.getValue();
	}

	@Override
	protected void doSetValue(Object value) {
		entry.setValue((V) value);
	}
}
