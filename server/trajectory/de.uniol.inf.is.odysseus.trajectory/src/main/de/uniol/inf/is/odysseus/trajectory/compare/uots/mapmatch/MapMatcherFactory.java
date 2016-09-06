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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;

/**
 * An implementation of <tt>AbstractFactory</tt> that returns the same <tt>IMapMatcher</tt>
 * for the same <i>String</i>. the <tt>String</i> describes the match matching strategy 
 * (i.e. POINT-TO-POINT, POINT-TO-ARCPOINT). The <tt>String</tt> is <i>case sensitive</i>. 
 * 
 * @author marcus
 *
 */
public class MapMatcherFactory extends AbstractFactory<IMapMatcher, String> {

	/** the singleton instance */
	private static final MapMatcherFactory INSTANCE = new MapMatcherFactory();
	
	/**
	 * Returns the <tt>MapMatcherFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>MapMatcherFactory</tt> as an eager singleton
	 */
	public static MapMatcherFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private MapMatcherFactory() {}
	
	@Override
	protected String convertKey(final String key) {
		return key.toUpperCase(Locale.US);
	}

	@Override
	protected IMapMatcher createProduct(final String convertedKey) {
		switch(convertedKey) {
			case "POINT-TO-POINT" : return PointToPointMapMatcher.getInstance();
			case "POINT-TO-ARCPOINT" : return PointToArcPointMapMatcher.getInstance();
		}
		throw new IllegalArgumentException("Map matching strategy not found: " + convertedKey);
	}

}
