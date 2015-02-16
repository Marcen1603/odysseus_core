package de.uniol.inf.is.odysseus.trajectory.compare;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IHasTextualAttributes;


public class VectorTextualDistance implements ITextualDistance {

	private final static VectorTextualDistance INSTANCE = new VectorTextualDistance();
	
	public static VectorTextualDistance getInstance() {
		return INSTANCE;
	}
	
	
	
	
	
	
	private VectorTextualDistance() {}
	
	@Override
	public double getDistance(IHasTextualAttributes o1, IHasTextualAttributes o2) {
		return 99;
	}
}
