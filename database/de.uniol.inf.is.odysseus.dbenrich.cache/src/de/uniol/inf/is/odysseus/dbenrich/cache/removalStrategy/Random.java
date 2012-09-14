package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

/**
 * This class implements a "random" caching strategy.
 */
public class Random implements IRemovalStrategy {
	
	private Map<?, CacheEntry<?>> cacheStore;

	private java.util.Random random;

	public Random() {
		// Needed for the RemovalStrategyRegistry
	}

	private Random(Map<?, CacheEntry<?>> cacheStore) {
		this.cacheStore = cacheStore;
		random = new java.util.Random();
	}

	@Override
	public void notifyNew(Object key, CacheEntry<?> value) {
		// do nothing
	}
	
	@Override
	public void notifyAccess(Object key, CacheEntry<?> value) {
		// do nothing
	}

	@Override
	public void removeNext() {
		Set<?> keySet = cacheStore.keySet();

		if (!keySet.isEmpty()) {
			/*
			 * Removing a random entry out of a set cannot be done efficiently
			 * in Java, since only an iterator or an array copy of the set can
			 * be used for that. So the performance is bad when there are many
			 * entries. Here the iterator version is used. Alternatively the
			 * first entry of the keyset could be removed, but that is not
			 * random in all implementations of a Map.
			 */
			int number = random.nextInt(keySet.size());
			int counter = 0;
			for (Object key : keySet) {
				if (counter++ == number) {
					cacheStore.remove(key);
					return;
				}
			}
		}
	}
	
	@Override
	public void clear() {
		cacheStore.clear();
	}

	@Override
	public String getName() {
		return "RANDOM";
	}

	@Override
	public IRemovalStrategy createInstance(Map<?, CacheEntry<?>> cacheStore) {
		return new Random(cacheStore);
	}
}
