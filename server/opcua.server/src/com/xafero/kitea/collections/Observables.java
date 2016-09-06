/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.kitea.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xafero.kitea.collections.impl.ObservableCollection;
import com.xafero.kitea.collections.impl.ObservableIterable;
import com.xafero.kitea.collections.impl.ObservableList;
import com.xafero.kitea.collections.impl.ObservableMap;
import com.xafero.kitea.collections.impl.ObservableSet;

/**
 * This class is the entry point for using Kitea's various collections.
 */
public final class Observables {

	/**
	 * Instantiates a new observables.
	 */
	private Observables() {
	}

	/**
	 * Decorate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param iterable
	 *            the iterable
	 * @return the observable iterable
	 */
	public static <T> ObservableIterable<T> decorate(Iterable<T> iterable) {
		return ObservableIterable.wrap(iterable);
	}

	/**
	 * Decorate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            the collection
	 * @return the observable collection
	 */
	public static <T> ObservableCollection<T> decorate(Collection<T> collection) {
		return ObservableCollection.wrap(collection);
	}

	/**
	 * Decorate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            the set
	 * @return the observable set
	 */
	public static <T> ObservableSet<T> decorate(Set<T> set) {
		return ObservableSet.wrap(set);
	}

	/**
	 * Decorate.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @return the observable list
	 */
	public static <T> ObservableList<T> decorate(List<T> list) {
		return ObservableList.wrap(list);
	}

	/**
	 * Decorate.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param map
	 *            the map
	 * @return the observable map
	 */
	public static <K, V> ObservableMap<K, V> decorate(Map<K, V> map) {
		return ObservableMap.wrap(map);
	}

	/**
	 * To map.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param keys
	 *            the keys
	 * @param values
	 *            the values
	 * @return the map
	 */
	public static <K, V> Map<K, V> toMap(K[] keys, V[] values) {
		Map<K, V> map = new LinkedHashMap<K, V>();
		for (int i = 0; i < keys.length && i < values.length; i++) {
			K key = keys[i];
			V value = values[i];
			map.put(key, value);
		}
		return map;
	}
}