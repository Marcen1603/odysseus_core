package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class BufferPOEstimator extends StandardPhysicalOperatorEstimator<BufferPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<BufferPO>> getOperatorClasses() {
		return Lists.newArrayList(BufferPO.class);
	}

	@Override
	public double getMemory() {
		return (getOperator().size() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema())) + 4;
	}
}
