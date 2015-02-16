package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class OwdDataTrajectory extends AbstractDataTrajectory<OwdData> {

	protected OwdDataTrajectory(RawWithId rawTrajectory,
			TimeInterval timeInterval, OwdData convertedData,
			Map<String, String> textualAttributes) {
		super(rawTrajectory, timeInterval, convertedData, textualAttributes);
	}

}
