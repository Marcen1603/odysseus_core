/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

/**
 * Diese Klasse observiert die Map-Einträge
 * @author Stefanie Witzke
 *
 * @param <K>
 * @param <V>
 */
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
