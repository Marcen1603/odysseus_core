package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.Uots;


public class TrajectoryCompareAlgorithmFactory {
	
	private final static TrajectoryCompareAlgorithmFactory INSTANCE = 
			new TrajectoryCompareAlgorithmFactory();
	
	public static TrajectoryCompareAlgorithmFactory getInstance() {
		return INSTANCE;
	}

	public ITrajectoryCompareAlgorithm create(final String name, int k, String queryTrajectoryPath, int utmZone, Map<String, String> options) {
		switch(name.toUpperCase()) {
			case "OWD" : return new Owd(k);
			case "UOTS" : return new Uots(k, QueryTrajectoryLoaderFactory.getInstance().load(queryTrajectoryPath, utmZone), utmZone, options);
		}
		return null;
	}
}
