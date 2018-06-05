package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BlockingBufferPO;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class BlockingBufferPOEstimator extends StandardPhysicalOperatorEstimator<BlockingBufferPO> {

	@Override
	public Class<? extends BlockingBufferPO> getOperatorClass() {
		return BlockingBufferPO.class;
	}

	@Override
	public double getMemory() {
		return (getOperator().getMaxBufferSize() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema())) + 4;
	}
}
