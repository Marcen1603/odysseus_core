package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

public class FIFO<K, V extends CacheEntry<?>> extends AbstractRemovalStrategy<K, V> {

	private final Queue<K> queue;

	public FIFO(Map<K, V> cacheStore) {
		super(cacheStore);

		this.queue = new LinkedList<K>();
	}

	@Override
	public void handleNewEntry(K key, V value) {
		// Insert as last element in queue
		queue.offer(key);
	}

	@Override
	public void removeNext() {
		if(!queue.isEmpty()) {
			// Remove element from the head of the queue and the cache.
			K key = queue.remove();
			cacheStore.remove(key);
		}
	}
}
