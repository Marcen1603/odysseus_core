package de.uniol.inf.is.odysseus.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;

import de.uniol.inf.is.odysseus.base.Pair;

public class LinkedMultiHashMap<K, V> implements Map<K, Collection<V>> {

	private class ValueIterator implements Iterator<V> {

		private Iterator<Pair<K, V>> iterator;
		private Pair<K, V> curElement;

		public ValueIterator() {
			this.iterator = entries.iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public V next() {
			curElement = iterator.next();
			return curElement.getE2();
		}

		@Override
		public void remove() {
			this.iterator.remove();
			LinkedMultiHashMap.this.remove(curElement.getE1(), curElement
					.getE2());
		}

	}

	final private MultiValueMap map = new MultiValueMap();
	final private LinkedList<Pair<K, V>> entries = new LinkedList<Pair<K, V>>();

	public LinkedMultiHashMap() {
	}
	
	public LinkedMultiHashMap(LinkedMultiHashMap<K, V> linkedMultiHashMap) {
		this();
		this.map.putAll(linkedMultiHashMap);
		this.entries.addAll(linkedMultiHashMap.entries);
	}

	@Override
	public void clear() {
		map.clear();
		entries.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<java.util.Map.Entry<K, Collection<V>>> entrySet() {
		return map.entrySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> get(Object key) {
		return map.getCollection(key);
	}

	@Override
	public boolean isEmpty() {
		return entries.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public int size() {
		return this.entries.size();
	}

	@Override
	public Collection<V> put(K key, Collection<V> values) {
		map.putAll(key, values);
		for (V curValue : values) {
			this.entries.add(new Pair<K, V>(key, curValue));
		}
		return values;
	}

	public void put(K key, V value) {
		map.put(key, value);
		entries.add(new Pair<K, V>(key, value));
	}

	@Override
	public void putAll(Map<? extends K, ? extends Collection<V>> m) {
		map.putAll(m);
		for (Entry<? extends K, ? extends Collection<V>> curValue : m
				.entrySet()) {
			for (V v : curValue.getValue()) {
				this.entries.add(new Pair<K, V>(curValue.getKey(), v));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> remove(Object key) {
		this.entries.remove(key);
		return (Collection<V>) this.map.remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Collection<V>> values() {
		return Collections.singleton((Collection<V>) map.values());
	}

	public void remove(K key, V value) {
		map.remove(key, value);
	}

	public Iterator<V> valueIterator() {
		return new ValueIterator();
	}
	
	@Override
	public LinkedMultiHashMap<K, V> clone() {
		return new LinkedMultiHashMap<K, V>(this);
	}

}
