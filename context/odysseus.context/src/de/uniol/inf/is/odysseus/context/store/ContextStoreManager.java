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
import java.util.List;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 * @param <Key>
 * @param <Value>
 */
public class ContextStoreManager {

	public static final String CONTEXT_STORE_NAME = "Contextstore";	

	private static ContextStoreManager instance;

	private ContextStoreManager() {
		
	}
	
	public static synchronized  ContextStoreManager getInstance() {
		if (instance == null) {
			instance = new ContextStoreManager();
		}
		return  instance;
	} 

	private HashMap<String, IContextStore<Tuple<? extends ITimeInterval>>> stores = new HashMap<String, IContextStore<Tuple<? extends ITimeInterval>>>();

	public void createStore(String name, IContextStore<Tuple<? extends ITimeInterval>> store) throws ContextManagementException {
		if (storeExists(name)) {
			throw new ContextManagementException("Store already exists");
		}		
		this.stores.put(name, store);				
	}

	public SDFSchema getStoreSchema(String storeName) throws ContextManagementException {
		if (storeExists(storeName)) {
			return this.stores.get(storeName).getSchema();
		}
		
		throw new ContextManagementException("Context store does not exists");
	}

	public void insertValue(String storeName, Tuple<? extends ITimeInterval> value) {
		stores.get(storeName).insertValue(value);
	}

	public void removeStore(String storeName) {
		if (storeExists(storeName)) {
			stores.remove(storeName);
		}
	}

	public List<Tuple<? extends ITimeInterval>> getValues(String storeName, ITimeInterval ti) throws ContextManagementException {
		if (storeExists(storeName)) {
			return this.stores.get(storeName).getValues(ti);
		} 		
		throw new ContextManagementException("Context store does not exists");
	}
	
	public List<Tuple<? extends ITimeInterval>> getLastValues(String storeName) throws ContextManagementException {
		if (storeExists(storeName)) {
			return this.stores.get(storeName).getLastValues();
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
