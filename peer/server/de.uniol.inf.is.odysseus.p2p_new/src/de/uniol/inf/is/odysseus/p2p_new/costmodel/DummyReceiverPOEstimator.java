package de.uniol.inf.is.odysseus.p2p_new.costmodel;

import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummyReceiverPO;

@SuppressWarnings("rawtypes")
public class DummyReceiverPOEstimator extends StandardPhysicalOperatorEstimator<DummyReceiverPO> {

	@Override
	protected Class<? extends DummyReceiverPO> getOperatorClass() {
		return DummyReceiverPO.class;
	}

	@Override
	public double getDatarate() {
		return getOperator().getDataRate();
	}

	@Override
	public double getWindowSize() {
		return getOperator().getIntervalLength();
	}
	
	@Override
	public double getNetwork() {
		return getDatarate() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
	}
}
