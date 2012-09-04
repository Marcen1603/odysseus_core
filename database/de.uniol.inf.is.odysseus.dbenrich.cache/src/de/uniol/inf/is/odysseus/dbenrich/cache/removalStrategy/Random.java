package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Map;
import java.util.Set;

public class Random<K, V> extends AbstractRemovalStrategy<K, V> {

	private final java.util.Random random;

	public Random(Map<K, V> cacheStore) {
		super(cacheStore);

		this.random = new java.util.Random();
	}

	@Override
	public void handleNewEntry(K key, V value) {
		// do nothing
	}

	@Override
	public void removeNext() {
		Set<K> keySet = cacheStore.keySet();

		if(!keySet.isEmpty()) {
			/* Removing a random entry out of a set cannot be done
			 * efficiently in Java, since only an iterator or an array
			 * copy of the set can be used for that. So the performance is bad
			 * when there are many entries.
			 * Here the iterator version is used.
			 * Alternatively the first entry of the keyset could be removed, but
			 * that is not random in all implementations of a Map.
			 */
			int number  = random.nextInt(keySet.size());
			int counter = 0;
			for(K key : keySet) {
				if(counter++ == number) {
					cacheStore.remove(key);
					return;
				}
			}
		}
	}

}
