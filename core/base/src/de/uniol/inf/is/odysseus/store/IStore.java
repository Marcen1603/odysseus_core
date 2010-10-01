package de.uniol.inf.is.odysseus.store;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public interface IStore<IDType extends Comparable<?>, STORETYPE> {
	public STORETYPE get(IDType id);
	public void put(IDType id, STORETYPE element) throws StoreException;
	public STORETYPE remove(IDType id) throws StoreException;
	public Set<Entry<IDType, STORETYPE>> entrySet();
	public Set<IDType> keySet();
	public Collection<STORETYPE> values();
	public boolean containsKey(IDType key);
	boolean isEmpty();
	void clear() throws StoreException;
}
