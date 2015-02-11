package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITupleToRawTrajectoryConverter {

	public RawTrajectory convert(final Tuple<ITimeInterval> tuple, final int utmZone);
}
