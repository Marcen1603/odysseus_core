package de.uniol.inf.is.odysseus.trajectory.compare.util;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;

public interface ITupleToRawTrajectoryConverter {

	public RawIdTrajectory convert(final Tuple<ITimeInterval> tuple, final int utmZone);
}
