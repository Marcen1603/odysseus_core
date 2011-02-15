package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

public class ObservativeMapEntryValue<K, V> extends AbstractObservableValue {

	private Map.Entry<K, V> entry;
	
	private PropertyChangeListener changeListener;

	private BenchmarkParam benchmarkParam;
	
	public ObservativeMapEntryValue(Map.Entry<K, V> entry, PropertyChangeListener changeListener, BenchmarkParam benchmarkParam) {
		super();
		this.entry = entry;
		this.changeListener = changeListener;
		this.benchmarkParam = benchmarkParam;
	}

	@Override
	public Object getValueType() {
		return entry.getValue().getClass();
	}

	@Override
	protected Object doGetValue() {
		return entry.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetValue(Object value) {
		entry.setValue((V) value);
		changeListener.propertyChange(new PropertyChangeEvent(benchmarkParam, null, null, null));
	}
}
