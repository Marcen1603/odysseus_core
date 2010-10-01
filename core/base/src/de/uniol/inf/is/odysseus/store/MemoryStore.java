package de.uniol.inf.is.odysseus.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class MemoryStore<IDType extends Comparable<?>,STORETYPE> implements IStore<IDType ,STORETYPE>{

	private Map<IDType, STORETYPE> elements = new TreeMap<IDType, STORETYPE>();
	
	@Override
	public STORETYPE get(IDType username) {
		return elements.get(username);
	}

	@Override
	public void put(IDType id, STORETYPE element) {
		elements.put(id, element);
	}
	
	@Override
	public Set<Entry<IDType, STORETYPE>> entrySet() {
		return elements.entrySet();
	};
	
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean containsKey(IDType key) {
		return elements.containsKey(key);
	}
	
	public STORETYPE remove(IDType id) {
		return elements.remove(id);
	}

	@Override
	public Set<IDType> keySet() {
		return elements.keySet();
	}

	@Override
	public Collection<STORETYPE> values() {
		return elements.values();
	}
	


	
}
