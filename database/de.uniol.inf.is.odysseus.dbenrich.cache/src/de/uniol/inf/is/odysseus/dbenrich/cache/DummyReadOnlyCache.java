package de.uniol.inf.is.odysseus.dbenrich.cache;

public class DummyReadOnlyCache<K, V> implements IReadOnlyCache<K, V> {
	
	private final IRetrievalStrategy<K, V> retrievalStrategy;
	
	public DummyReadOnlyCache(IRetrievalStrategy<K, V> retrievalStrategy) {
		this.retrievalStrategy = retrievalStrategy;
	}

	@Override
	public V get(K key) {
		return retrievalStrategy.get(key);
	}

	@Override
	public void debug() {}

	@Override
	public void open() {}

	@Override
	public void close() {}

}
