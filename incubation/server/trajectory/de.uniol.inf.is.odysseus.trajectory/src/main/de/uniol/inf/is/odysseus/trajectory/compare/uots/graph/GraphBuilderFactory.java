package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractObjectLoaderFactory;

public class GraphBuilderFactory extends AbstractObjectLoaderFactory<IGraphLoader<String, Integer>, NetGraph, String, Integer> {

	private final static GraphBuilderFactory INSTANCE = new GraphBuilderFactory();
	
	public static GraphBuilderFactory getInstance() {
		return INSTANCE;
	}
	
	private GraphBuilderFactory() { }
	
	@Override
	protected String convertKey(String key) {
		return key.substring(key.lastIndexOf('.') + 1).toUpperCase(Locale.US);
	}

	@Override
	protected IGraphLoader<String, Integer> createLoader(String convertedKey) {
		switch(convertedKey) {
			case "OSM" : return OsmGraphLoader.getInstance();
		}
		throw new IllegalArgumentException("No GraphLoader found for file extension: " + convertedKey);
	}

}
