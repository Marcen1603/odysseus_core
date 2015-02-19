package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;


public class UotsDataTrajectory extends AbstractConvertedDataTrajectory<UotsData> {

	public UotsDataTrajectory(final RawDataTrajectory rawTrajectory, final UotsData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
