package de.uniol.inf.is.odysseus.base.store;

import java.util.Map.Entry;
import java.util.Set;

public interface IStore<IDType, STORETYPE> {
	public STORETYPE get(IDType id);
	public void put(IDType id, STORETYPE element) throws StoreException;
	public STORETYPE remove(IDType id) throws StoreException;
	Set<Entry<IDType, STORETYPE>> entrySet();
	public boolean containsKey(IDType key);
	boolean isEmpty();
	void clear() throws StoreException;
}
