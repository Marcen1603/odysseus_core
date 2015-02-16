package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

public class OwdQueryTrajectory extends AbstractQueryTrajectory<OwdData> {

	protected OwdQueryTrajectory(RawQueryTrajectory rawTrajectory,
			OwdData convertedData, Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
