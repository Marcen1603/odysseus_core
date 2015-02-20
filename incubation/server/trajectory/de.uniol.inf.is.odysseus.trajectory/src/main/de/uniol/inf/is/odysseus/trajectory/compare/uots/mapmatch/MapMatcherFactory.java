package de.uniol.inf.is.odysseus.trajectory.compare.uots.mapmatch;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;


public class MapMatcherFactory extends AbstractFactory<IMapMatcher, String> {

	private static final MapMatcherFactory INSTANCE = new MapMatcherFactory();
	
	public static MapMatcherFactory getInstance() {
		return INSTANCE;
	}
	
	private MapMatcherFactory() {}
	
	@Override
	protected String convertKey(String key) {
		return key.toUpperCase(Locale.US);
	}

	@Override
	protected IMapMatcher createProduct(String convertedKey) {
		switch(convertedKey) {
		case "POINT-TO-POINT" : return PointToPointMapMatcher.getInstance();
		case "POINT-TO-ARCPOINT" : return PointToArcPointMapMatcher.getInstance();
		}
		return null;
	}

}
