package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class BufferPOEstimator extends StandardPhysicalOperatorEstimator<BufferPO> {

	@Override
	public Class<? extends BufferPO> getOperatorClass() {
		return BufferPO.class;
	}

	@Override
	public double getMemory() {
		return (getOperator().size() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema())) + 4;
	}
}
