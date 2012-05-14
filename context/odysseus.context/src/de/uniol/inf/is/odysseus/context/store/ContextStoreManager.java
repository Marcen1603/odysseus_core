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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.IContextManagementListener;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 * @param <Key>
 * @param <Value>
 */
public class ContextStoreManager<T extends Tuple<? extends ITimeInterval>> {
	
	private ContextStoreManager() {
		
	}
	
	private static List<IContextManagementListener> listeners = new ArrayList<IContextManagementListener>();

	private static HashMap<String, IContextStore<? extends Tuple<? extends ITimeInterval>>> stores = new HashMap<String, IContextStore<? extends Tuple<? extends ITimeInterval>>>();

	
	public static void addListener(IContextManagementListener listener){
		listeners.add(listener);
	}
	
	public static void removeListener(IContextManagementListener listener){
		listeners.remove(listener);
	}
	
	public static void notifyListeners(){
		for(IContextManagementListener listener : listeners){
			listener.contextManagementChanged();
		}
	}
	
	public static <T extends Tuple<? extends ITimeInterval>> void addStore(String name, IContextStore<T> store) throws ContextManagementException {
		if (storeExists(name)) {
			throw new ContextManagementException("Store already exists");
		}		
		stores.put(name, store);
		notifyListeners();
	}
	
	public static Collection<IContextStore<? extends Tuple<? extends ITimeInterval>>> getStores(){
		return stores.values();
	}
	
	public static void removeStore(String storeName) {
		if (storeExists(storeName)) {
			stores.remove(storeName);
			notifyListeners();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Tuple<? extends ITimeInterval>> IContextStore<T> getStore(String name){
		if(storeExists(name)){
			return (IContextStore<T>) stores.get(name);
		}
		return null;
	}
	
	public static boolean storeExists(String name) {
		return stores.containsKey(name);
	}

}
