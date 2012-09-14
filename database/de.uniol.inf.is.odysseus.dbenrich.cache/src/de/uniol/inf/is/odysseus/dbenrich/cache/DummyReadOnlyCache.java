package de.uniol.inf.is.odysseus.dbenrich.cache;

/**
 * A dummy cache implementation, that does not actually cache data. Instead it 
 * directly uses its retrieval strategy on get().
 */
public class DummyReadOnlyCache<K, V> implements IReadOnlyCache<K, V> {

	private final IRetrievalStrategy<K, V> retrievalStrategy;

	public DummyReadOnlyCache(IRetrievalStrategy<K, V> retrievalStrategy) {
		this.retrievalStrategy = retrievalStrategy;
	}

	public DummyReadOnlyCache(DummyReadOnlyCache<K, V> dummyReadOnlyCache) {
		retrievalStrategy = dummyReadOnlyCache.retrievalStrategy.clone();
	}

	@Override
	public V get(K key) {
		return retrievalStrategy.get(key);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public DummyReadOnlyCache<K, V> clone() {
		return new DummyReadOnlyCache<K, V> (this);
	}
}
