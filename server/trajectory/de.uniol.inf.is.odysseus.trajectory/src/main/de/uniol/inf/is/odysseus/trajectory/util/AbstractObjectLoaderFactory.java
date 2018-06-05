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
 * Abstract factory implementation which holds a <tt>Map</tt> with <i>keys</i> of
 * type <tt>K</tt> and their respective <i>values</i> of type <tt>IObjectLoader</tt>.
 * Subclasses can use this to bundle instances of <tt>IObjectLoaders</tt>.
 * 
 * @author marcus
 *
 * @param <L> the type of the <tt>IObjectLoaders</tt>
 * @param <O> the type of the object to be load by the <tt>IObjectLoader</tt>
 * @param <K> the type of the key for the <tt>IObjectLoader</tt>
 * @param <A> the type of the additional info for the <tt>IObjectLoader</tt>
 */
public abstract class AbstractObjectLoaderFactory<L extends IObjectLoader<O, K, A>, O, K, A> {

	/** stores IObjectLoader for keys */
	private final Map<K, L> objectLoaders = new HashMap<>();
	
	/**
	 * Beware class from being instantiated.
	 */
	protected AbstractObjectLoaderFactory() {}
	
	/**
	 * Invokes an <tt>IObjectLoader's load(...)</tt> method for the specified <i>key</i>.
	 * If their is no <tt>IObjectLoader</tt> for the <i>key</i> a new one will be created and
	 * its <tt>load</tt> method will be invoked.
	 * 
	 * @param key the <i>key</i> for which the an <tt>IObjectLoader</tt> is supposed to
	 * 		  load the object
	 * @param additional addition infoation for the <tt>IObjectLoader</tt>
	 * @return an object for the passed <i>key</i> and <i>additional info</i>
	 *         loaded by the underneath <tt>IObjectLoader</tt>
	 *      
	 */
	public O load(final K key, final A additional) {
		final K convertedKey = this.convertKey(key);
		L loader = this.objectLoaders.get(convertedKey);
		if(loader == null)  {
			this.objectLoaders.put(key, loader = this.createLoader(convertedKey));
		}
		return loader.load(key, additional);
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
	 * Creates and returns a <tt>IObjectLoader</tt> for the <i>converted key</i>.
	 * This method is only called if there exists <tt>IObjectLoader</tt> for the converted
	 * key.
	 * 
	 * @param convertedKey the <i>converted key</i> for which the <tt>IObjectLoader</tt> will be 
	 *        created
	 * @return the newly created <tt>IObjectLoader</tt> for the the <i>converted key</i>
	 */
	protected abstract L createLoader(K convertedKey);
}
