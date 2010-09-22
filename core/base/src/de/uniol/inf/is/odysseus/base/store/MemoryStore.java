package de.uniol.inf.is.odysseus.base.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MemoryStore<IDType,STORETYPE> implements IStore<IDType,STORETYPE>{

	private Map<IDType, STORETYPE> elements = new HashMap<IDType, STORETYPE>();
	
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


	
}
