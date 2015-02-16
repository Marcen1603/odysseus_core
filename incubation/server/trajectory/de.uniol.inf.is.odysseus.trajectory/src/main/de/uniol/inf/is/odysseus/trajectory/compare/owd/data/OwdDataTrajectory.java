package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawIdTrajectory;


public class OwdDataTrajectory extends AbstractDataTrajectory<OwdData> {

	protected OwdDataTrajectory(RawIdTrajectory rawTrajectory, OwdData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
