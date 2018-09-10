package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class RenameAOEstimator extends StandardLogicalOperatorEstimator<RenameAO> {

	@Override
	protected Class<? extends RenameAO> getOperatorClass() {
		return RenameAO.class;
	}
	
	@Override
	public double getCpu() {
		return 0.0;
	}
	
	@Override
	public double getMemory() {
		return 0.0;
	}
}
