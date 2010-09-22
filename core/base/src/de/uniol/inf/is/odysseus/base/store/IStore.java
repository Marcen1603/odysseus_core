package de.uniol.inf.is.odysseus.base.store;

import java.util.Map;

public interface IStore<IDType, STORETYPE> {
	public STORETYPE getByName(IDType id);
	void store(IDType id, STORETYPE element) throws StoreException;
	Map<IDType, STORETYPE> getAll();
	boolean isEmpty();
	void clear() throws StoreException;
}
