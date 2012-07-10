/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;

public class MapParameter<K, V> extends AbstractParameter<Map<K, V>> {

	private static final long serialVersionUID = -5705485425700425823L;
	private IParameter<K> keyParameter;
	private IParameter<V> valueParameter;

	public MapParameter(String name, REQUIREMENT requirement, IParameter<K> keyParameter, IParameter<V> valueParameter) {
		super(name, requirement, USAGE.RECENT);
		this.keyParameter = keyParameter;
		this.valueParameter = valueParameter;
	}

	public MapParameter(String name, REQUIREMENT requirement, IParameter<K> keyParameter, IParameter<V> valueParameter, USAGE usage) {
		super(name, requirement, usage);
		this.keyParameter = keyParameter;
		this.valueParameter = valueParameter;
	}

	public MapParameter(IParameter<K> keyParameter, IParameter<V> valueParameter) {
		this.keyParameter = keyParameter;
		this.valueParameter = valueParameter;
	}

	@Override
	protected void internalAssignment() {
		try {
			HashMap<K, V> map = new HashMap<K, V>();
			for (Entry<?, ?> e : ((Map<?, ?>) inputValue).entrySet()) {
				keyParameter.setInputValue(e.getKey());
				keyParameter.setAttributeResolver(getAttributeResolver());
				keyParameter.setDataDictionary(getDataDictionary());
				if (!keyParameter.validate()) {
					throw new RuntimeException(keyParameter.getErrors().get(0));
				}
				valueParameter.setInputValue(e.getValue());
				valueParameter.setAttributeResolver(getAttributeResolver());
				valueParameter.setDataDictionary(getDataDictionary());
				if (!valueParameter.validate()) {
					throw new RuntimeException(valueParameter.getErrors().get(0));
				}
				map.put(keyParameter.getValue(), valueParameter.getValue());
			}
			setValue(map);

		} catch (ClassCastException e) {
			throw new IllegalArgumentException("wrong input for parameter " + getName() + ", Map expected, got " + inputValue.getClass().getSimpleName());
		}
	}

}
