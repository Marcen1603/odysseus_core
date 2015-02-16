package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Map;

import org.javatuples.Pair;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.uots.Uots;


public class TrajectoryCompareAlgorithmFactory {
	
	private final static TrajectoryCompareAlgorithmFactory INSTANCE = 
			new TrajectoryCompareAlgorithmFactory();
	
	public static TrajectoryCompareAlgorithmFactory getInstance() {
		return INSTANCE;
	}

	public ITrajectoryCompareAlgorithm<?> create(final String name, int k, String queryTrajectoryPath, Map<String, String> textualAttributes,
			int utmZone, Map<String, String> options) {
		
		switch(name.toUpperCase()) {
			case "OWD" : 
				return new Owd(k);
			case "UOTS" : 
				return new Uots(k, QueryTrajectoryLoaderFactory.getInstance().load(queryTrajectoryPath, new Pair<>(utmZone, textualAttributes)), utmZone, options);
		}
		return null;
	}
}
