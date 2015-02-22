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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractObjectLoaderFactory;

/**
 * Implementation of <tt>AbstractObjectLoaderFactory</tt> that holds different 
 * <tt>IGraphLoaders</tt> for loading <tt>NetGraphs</tt> from different <i>formats</i>.
 * 
 * @author marcus
 *
 */
public class GraphBuilderFactory extends AbstractObjectLoaderFactory<IGraphLoader<String, Integer>, NetGraph, String, Integer> {

	/** the singleton instance */
	private final static GraphBuilderFactory INSTANCE = new GraphBuilderFactory();
	
	/**
	 * Returns the <tt>GraphBuilderFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>GraphBuilderFactory</tt> as an eager singleton
	 */
	public static GraphBuilderFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private GraphBuilderFactory() { }
	
	@Override
	protected String convertKey(final String key) {
		return key.substring(key.lastIndexOf('.') + 1).toUpperCase(Locale.US);
	}

	@Override
	protected IGraphLoader<String, Integer> createLoader(final String convertedKey) {
		switch(convertedKey) {
			case "OSM" : return OsmGraphLoader.getInstance();
		}
		throw new IllegalArgumentException("No IGraphLoader found for file extension: " + convertedKey);
	}

}
