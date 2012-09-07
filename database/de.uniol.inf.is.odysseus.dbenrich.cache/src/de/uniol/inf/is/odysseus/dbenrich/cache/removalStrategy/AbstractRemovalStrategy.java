package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Map;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

public abstract class AbstractRemovalStrategy implements IRemovalStrategy {

	protected Map<?, CacheEntry<?>> cacheStore;
}
