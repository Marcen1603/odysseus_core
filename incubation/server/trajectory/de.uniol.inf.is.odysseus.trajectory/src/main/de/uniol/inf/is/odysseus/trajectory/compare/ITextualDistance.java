package de.uniol.inf.is.odysseus.trajectory.compare;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IHasTextualAttributes;
import de.uniol.inf.is.odysseus.trajectory.compare.data.ITrajectory;

public interface ITextualDistance {

	public double getDistance(IHasTextualAttributes o1, IHasTextualAttributes o2);
}
