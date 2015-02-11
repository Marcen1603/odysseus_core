package de.uniol.inf.is.odysseus.trajectory.util;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFactory<P, K> {
	
	
	private final Map<K, P> products = new HashMap<>();
	
	protected AbstractFactory() {}
	
	public P create(K key) {
		key = this.convertKey(key);
		P product = this.products.get(key);
		if(product == null) {
			this.products.put(key, product = this.createProduct(key));
		}
		return product;
	}
	
	protected abstract K convertKey(K key);
	
	protected abstract P createProduct(K convertedKey);
}
