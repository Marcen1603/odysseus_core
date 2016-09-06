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

package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;

/**
 * An implementation of <tt>AbstractFactory</tt> that returns the same <tt>UtmPointCreator</tt>
 * for the same <i>UTM zone</i>.
 * 
 * @author marcus
 *
 */
public class UtmPointCreatorFactory extends AbstractFactory<UtmPointCreator, Integer>{

	/** the singleton instance */
	private static final UtmPointCreatorFactory INSTANCE = new UtmPointCreatorFactory();
	
	/**
	 * Returns the <tt>UtmPointCreatorFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>UtmPointCreatorFactory</tt> as an eager singleton
	 */
	public static UtmPointCreatorFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private UtmPointCreatorFactory() { }
	
	
	@Override
	protected Integer convertKey(final Integer key) {
		return key;
	}


	@Override
	protected UtmPointCreator createProduct(final Integer convertedKey) {
		return new UtmPointCreator(convertedKey);
	}

}
