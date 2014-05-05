package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BlockingBufferPO;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class BlockingBufferPOEstimator extends StandardPhysicalOperatorEstimator<BlockingBufferPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<BlockingBufferPO>> getOperatorClasses() {
		return Lists.newArrayList(BlockingBufferPO.class);
	}

	@Override
	public double getMemory() {
		return (getOperator().getMaxBufferSize() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema())) + 4;
	}
}
