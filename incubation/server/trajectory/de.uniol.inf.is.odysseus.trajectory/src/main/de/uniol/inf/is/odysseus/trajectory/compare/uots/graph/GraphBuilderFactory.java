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
