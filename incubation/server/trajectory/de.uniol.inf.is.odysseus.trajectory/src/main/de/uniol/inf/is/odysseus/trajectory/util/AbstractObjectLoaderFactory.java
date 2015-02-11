package de.uniol.inf.is.odysseus.trajectory.util;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectLoaderFactory<L extends IObjectLoader<O, K, A>, O, K, A> {

	
	private final Map<K, L> objectLoaders = new HashMap<>();
	
	protected AbstractObjectLoaderFactory() {}
	
	public O load(K key, A additional) {
		final K convertedKey = this.convertKey(key);
		L loader = this.objectLoaders.get(convertedKey);
		if(loader == null)  {
			this.objectLoaders.put(key, loader = this.createLoader(convertedKey));
		}
		return loader.load(key, additional);
	}
	
	protected abstract K convertKey(K key);
	
	protected abstract L createLoader(K convertedKey);
}
