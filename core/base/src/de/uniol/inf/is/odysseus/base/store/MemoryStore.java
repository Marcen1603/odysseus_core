package de.uniol.inf.is.odysseus.base.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryStore<IDType,STORETYPE> implements IStore<IDType,STORETYPE>{

	private Map<IDType, STORETYPE> elements = new HashMap<IDType, STORETYPE>();
	
	@Override
	public STORETYPE getByName(IDType username) {
		return elements.get(username);
	}

	@Override
	public void store(IDType id, STORETYPE element) {
		elements.put(id, element);
	}
	
	@Override
	public Map<IDType, STORETYPE> getAll() {
		return Collections.unmodifiableMap(elements);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public void clear() {
		elements.clear();
	}
	
}
