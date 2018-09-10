package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class BufferAOEstimator extends StandardLogicalOperatorEstimator<BufferAO> {

	@Override
	public Class<? extends BufferAO> getOperatorClass() {
		return BufferAO.class;
	}

	@Override
	public double getMemory() {
		return (getOperator().getMaxBufferSize() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema())) + 4;
	}
}
