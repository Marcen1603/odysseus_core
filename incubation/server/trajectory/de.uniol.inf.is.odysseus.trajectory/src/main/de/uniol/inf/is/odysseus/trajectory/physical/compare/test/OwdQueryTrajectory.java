package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

public class OwdQueryTrajectory extends AbstractQueryTrajectory<OwdData> {

	protected OwdQueryTrajectory(OwdData convertedData,
			Map<String, String> textualAttributes) {
		super(convertedData, textualAttributes);
	}

}
