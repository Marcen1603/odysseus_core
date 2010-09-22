package de.uniol.inf.is.odysseus.base.store;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryStore<STORETYPE extends IHasId<IDType>,IDType> {

	private Map<IDType, STORETYPE> elements = new HashMap<IDType, STORETYPE>();
	
	public STORETYPE getByName(IDType username) {
		return elements.get(username);
	}

	public void store(STORETYPE element) {
		elements.put(element.getId(), element);
	}
	
	public Collection<STORETYPE> getAll() {
		return Collections.unmodifiableCollection(elements.values());
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
}
