package de.uniol.inf.is.odysseus.server.opcua.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class FluentMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1489554452120845670L;

	public FluentMap() {
		super();
	}

	public FluentMap(Map<K, V> other) {
		super(other);
	}

	public FluentMap<K, V> set(K key, V value) {
		put(key, value);
		return this;
	}

	public FluentMap<K, V> del(K key) {
		remove(key);
		return this;
	}

	public FluentMap<K, V> clean() {
		clear();
		return this;
	}

	public FluentMap<K, V> copy() {
		return new FluentMap<K, V>(this);
	}
}