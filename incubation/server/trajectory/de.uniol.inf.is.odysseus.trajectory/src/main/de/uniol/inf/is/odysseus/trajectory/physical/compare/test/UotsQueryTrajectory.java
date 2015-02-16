package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.Map;

public class UotsQueryTrajectory extends AbstractQueryTrajectory<UotsData> implements IQueryTrajectory<UotsData> {

	protected UotsQueryTrajectory(UotsData convertedData,
			Map<String, String> textualAttributes) {
		super(convertedData, textualAttributes);
	}
}
