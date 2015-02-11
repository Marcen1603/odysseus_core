package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.mapmatch;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractFactory;

public class MapMatcherFactory extends AbstractFactory<IMapMatcher, String> {

	private static final MapMatcherFactory INSTANCE = new MapMatcherFactory();
	
	public static MapMatcherFactory getInstance() {
		return INSTANCE;
	}
	
	
	private MapMatcherFactory() {}
	
	@Override
	protected String convertKey(String key) {
		return key.toUpperCase();
	}

	@Override
	protected IMapMatcher createProduct(String convertedKey) {
		switch(convertedKey) {
		case "POINT-TO-POINT" : return new PointToPointMapMatcher();
		case "POINT-TO-ARCPOINT" : return new PointToArcPointMapMatcher();
		}
		return null;
	}

}
