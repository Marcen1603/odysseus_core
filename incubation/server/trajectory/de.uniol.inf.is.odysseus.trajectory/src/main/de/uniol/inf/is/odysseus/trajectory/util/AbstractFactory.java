package de.uniol.inf.is.odysseus.trajectory.util;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract factory for returning the same <i>product</i> for the same <i>key</i>.
 * 
 * @author marcus
 *
 * @param <P> the type of the product
 * @param <K> the key for intantiating the product
 */
public abstract class AbstractFactory<P, K> {
	
	/** stores products for keys */
	private final Map<K, P> products = new HashMap<>();
	
	/**
	 * Beware class from being instantiated.
	 */
	protected AbstractFactory() {}
	
	/**
	 * Returns a <i>product</i> for the passed <i>key</i>. If there exists
	 * no product for the key a new one will be created.
	 * 
	 * @param key the <i>key</i> for which the product will be returned ore created
	 * @return an already existing <i>product</i> for the passed key or an newly 
	 *         created if there has not been a product for this key before
	 *      
	 */
	public P create(K key) {
		key = this.convertKey(key);
		P product = this.products.get(key);
		if(product == null) {
			this.products.put(key, product = this.createProduct(key));
		}
		return product;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	protected abstract K convertKey(K key);
	
	/**
	 * 
	 * @param convertedKey
	 * @return
	 */
	protected abstract P createProduct(K convertedKey);
}
