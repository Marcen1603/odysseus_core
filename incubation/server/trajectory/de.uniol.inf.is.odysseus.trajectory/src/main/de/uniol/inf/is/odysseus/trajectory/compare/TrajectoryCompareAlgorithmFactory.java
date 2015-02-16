package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.uots.Uots;
import de.uniol.inf.is.odysseus.trajectory.compare.util.QueryTrajectoryLoaderFactory;


public class TrajectoryCompareAlgorithmFactory {
	
	private final static TrajectoryCompareAlgorithmFactory INSTANCE = 
			new TrajectoryCompareAlgorithmFactory();
	
	public static TrajectoryCompareAlgorithmFactory getInstance() {
		return INSTANCE;
	}

	public ITrajectoryCompareAlgorithm<?, ?> create(final String name, int k, String queryTrajectoryPath, Map<String, String> textualAttributes,
			int utmZone, double lambda, Map<String, String> options) {
		
		switch(name.toUpperCase()) {
			case "OWD" : 
				return null;//;new Owd(k);
			case "UOTS" : 
				return new Uots(options, 
						k, 
						QueryTrajectoryLoaderFactory.getInstance().load(queryTrajectoryPath, utmZone), 
						textualAttributes, 
						utmZone, 
						lambda);
		}
		throw new IllegalArgumentException("No Algorithm '" + name.toUpperCase() + "' found.");
	}
}
