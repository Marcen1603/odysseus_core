package de.uniol.inf.is.odysseus.trajectory.physical.compare;

public class TrajectoryCompareAlgorithmFactory {

	public ITrajectoryCompareAlgorithm create(final String name, int k) {
		switch(name.toUpperCase()) {
			case "OWD": return new Owd(k);
		}
		return null;
	}
}
