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
package de.uniol.inf.is.odysseus.core.server.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

/**
 * A generic interface for different key value store
 * @author Marco Grawunder
 *
 * @param <IDType> The type of the key
 * @param <STORETYPE> The type of the elements that should be stored
 */
public interface IStore<IDType extends Comparable<?>, STORETYPE> extends Serializable{

	/**
	 * Create a new instance of the store. Every storetype can have different options
	 * so this is packed into an OptionMap
	 * @param options
	 * @return
	 * @throws StoreException If the store cannot be created, an exception is thrown
	 */
	IStore<IDType, STORETYPE> newInstance(OptionMap options) throws StoreException;

	/**
	 * Retrieve value for key id
	 * @param id the key to the value
	 * @return the value or null if value not available
	 */
	STORETYPE get(IDType id);

	/**
	 * Retrieve elements in ascending order and limit
	 * to the first limit results
	 * @param limit
	 * @return
	 */
	List<Entry<IDType, STORETYPE>> getOrderedByKey(long limit);

	/**
	 * Put key value pair to store
	 * @param id The key of the value
	 * @param element The value that should be store
	 * @throws StoreException If the storing fails, an exception is thrown
	 */
	void put(IDType id, STORETYPE element) throws StoreException;

	/**
	 * Remove element from store
	 * @param id The key of the element that should be removed
	 * @return the value that is removed or null if element with key id cannot be found
	 * @throws StoreException If the removal fails, an exception is thrown
	 */
	STORETYPE remove(IDType id) throws StoreException;

	/**
	 * Retrieve all values from the store as entrySet (same as in java.util.Map)
	 * @return
	 */
	Set<Entry<IDType, STORETYPE>> entrySet();

	/**
	 * Retrieve all keys from the store (same as in java.util.Map)
	 * @return
	 */
	Set<IDType> keySet();

	/**
	 * Retrieve all values from the store (same as in java.util.Map)
	 * @return
	 */
	Collection<STORETYPE> values();

	/**
	 * Return true, if the given key is stored
	 * @param key
	 * @return
	 */
	boolean containsKey(IDType key);

	/**
	 * Returns true, if the store is empty
	 * @return
	 */
	boolean isEmpty();

	/**
	 * Clear the whole store
	 * @throws StoreException
	 */
	void clear() throws StoreException;

	/**
	 * Commit all changes
	 */
	void commit();

	/**
	 * The number of elements stored
	 * @return
	 */
	int size();

	/**
	 * Dump all elements that are stored to a string buffer (typically only used for debugging)
	 * @param buffer
	 */
	void dumpTo(StringBuffer buffer);

	/**
	 * Get the global unique type of the store
	 * @return
	 */
	String getType();

}
