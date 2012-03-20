/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.store;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 * @param <Key>
 * @param <Value>
 */
public class ContextStore<T> {

	public static final String CONTEXT_STORE_NAME = "Contextstore";

	protected static ContextStore<Object> instance;

	protected ContextStore() {
	}

	public static synchronized ContextStore<Object> getInstance() {
		if (instance == null) {
			instance = new ContextStore<Object>();
		}
		return instance;
	} 

	protected HashMap<String, ContextStoreEntry<T>> stores = new HashMap<String, ContextStoreEntry<T>>();

	public void createStore(String name, SDFSchema schema) throws ContextManagementException {
		if (storeExists(name)) {
			throw new ContextManagementException("Store already exists");
		}

		ContextStoreEntry<T> entry = new ContextStoreEntry<T>(schema);
		this.stores.put(name, entry);
	}

	public SDFSchema getStoreSchema(String storeName) throws ContextManagementException {
		if (storeExists(storeName)) {
			return this.stores.get(storeName).getSchema();
		}
		
		throw new ContextManagementException("Context store does not exists");
	}

	public void insertValue(String storeName, T value) {
		stores.get(storeName).setValue(value);
	}

	public void removeStore(String storeName) {
		if (storeExists(storeName)) {
			stores.remove(storeName);
		}
	}

	public Object getValue(String storeName) throws ContextManagementException {
		if (storeExists(storeName)) {
			return this.stores.get(storeName).getValue();
		} 
		
		throw new ContextManagementException("Context store does not exists");
	}

	public SDFAttribute getSDFAttribute(String name) {
		SDFAttribute attribute = new SDFAttribute("ContextStore", name, SDFDatatype.INTEGER);
		return attribute;
	}

	public boolean storeExists(String name) {
		return this.stores.containsKey(name);
	}

}
