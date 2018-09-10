package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class TopAOEstimator extends StandardLogicalOperatorEstimator<TopAO> {

	@Override
	protected Class<? extends TopAO> getOperatorClass() {
		return TopAO.class;
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
