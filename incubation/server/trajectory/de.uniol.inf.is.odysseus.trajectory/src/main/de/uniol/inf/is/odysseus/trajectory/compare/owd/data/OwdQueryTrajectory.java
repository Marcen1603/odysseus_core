package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

public class OwdQueryTrajectory extends AbstractConvertedQueryTrajectory<OwdData> {

	public OwdQueryTrajectory(RawQueryTrajectory rawTrajectory,
			OwdData convertedData, Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
