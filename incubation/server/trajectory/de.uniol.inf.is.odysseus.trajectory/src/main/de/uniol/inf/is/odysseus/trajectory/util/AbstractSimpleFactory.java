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

/**
 * An abstract simple factory for returning a <i>product</i>.
 * 
 * @author marcus
 *
 * @param <P> the type of the <i>product</i>
 */
public abstract class AbstractSimpleFactory<P> {
	
	/** the <i>product</i> to be returned */
	private final P product;
	
	/**
	 * Beware class from being instantiated.
	 */
	protected AbstractSimpleFactory() {
		this.product = this.create();
	}
	
	/**
	 * Returns the <i>product</i> of this <tt>AbstractSimpleFactory</tt>.
	 * 
	 * @return the <i>product</i> of this <tt>AbstractSimpleFactory</tt>
	 */
	public P getProduct() {
		return this.product;
	}

	/**
	 * Creates and returns the <i>product</i> of this <tt>AbstractSimpleFactory</tt>.
	 * 
	 * @return the newly created <i>product</i> of this <tt>AbstractSimpleFactory</tt>
	 */
	protected abstract P create();
}
