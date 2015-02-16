package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class UotsDataTrajectory extends AbstractDataTrajectory<UotsData> {

	protected UotsDataTrajectory(RawWithId rawTrajectory,
			TimeInterval timeInterval, UotsData convertedData,
			Map<String, String> textualAttributes) {
		super(rawTrajectory, timeInterval, convertedData, textualAttributes);
	}
}
