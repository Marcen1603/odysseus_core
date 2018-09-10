/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.util;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract factory for returning the same <i>product</i> for the same <i>key</i>.
 * 
 * @author marcus
 *
 * @param <P> the type of the <i>product</i>
 * @param <K> the type of the <i>key</i> for intantiating the product
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
	 * @param key the <i>key</i> for which the product will be returned or created
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
	 * Converts the <i>key</i> and returns it. May be used by subclasses
	 * to return the same <i>product</i> for different keys.
	 * 
	 * @param the <i>key</i> to be converted
	 * @return key the converted <i>key</i>
	 */
	protected abstract K convertKey(K key);
	
	/**
	 * Creates and returns a <i>product</i> for the <i>converted key</i>.
	 * This method is only called if there exists no product for the converted
	 * key.
	 * 
	 * @param convertedKey the <i>converted key</i> for which the product will be 
	 *        created
	 * @return the newly created <i>product</i> for the the <i>converted key</i>
	 */
	protected abstract P createProduct(K convertedKey);
}
