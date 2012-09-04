package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Map;

import de.uniol.inf.is.odysseus.dbenrich.cache.IRemovalStrategy;

public abstract class AbstractRemovalStrategy<K, V> implements IRemovalStrategy<K, V> {

	protected final Map<K, V> cacheStore;

	public AbstractRemovalStrategy(Map<K, V> cacheStore) {
		this.cacheStore = cacheStore;
	}
}
