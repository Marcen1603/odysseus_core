package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;


public class OwdDataTrajectory extends AbstractConvertedDataTrajectory<OwdData> {

	public OwdDataTrajectory(RawDataTrajectory rawTrajectory, OwdData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
