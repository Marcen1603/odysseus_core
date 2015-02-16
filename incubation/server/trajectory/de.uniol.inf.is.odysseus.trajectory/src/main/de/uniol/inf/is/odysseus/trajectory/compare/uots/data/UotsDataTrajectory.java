package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;


public class UotsDataTrajectory extends AbstractDataTrajectory<UotsData> {

	public UotsDataTrajectory(RawIdTrajectory rawTrajectory, UotsData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
