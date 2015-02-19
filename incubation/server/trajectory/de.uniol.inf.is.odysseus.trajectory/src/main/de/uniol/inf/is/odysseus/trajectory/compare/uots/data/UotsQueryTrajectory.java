package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

public class UotsQueryTrajectory extends AbstractConvertedQueryTrajectory<UotsData> {

	public UotsQueryTrajectory(RawQueryTrajectory rawTrajectory,
			UotsData convertedData, Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
