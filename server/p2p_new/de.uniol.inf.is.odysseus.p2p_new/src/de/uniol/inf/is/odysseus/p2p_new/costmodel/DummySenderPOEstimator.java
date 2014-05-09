package de.uniol.inf.is.odysseus.p2p_new.costmodel;

import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummySenderPO;

@SuppressWarnings("rawtypes")
public class DummySenderPOEstimator extends StandardPhysicalOperatorEstimator<DummySenderPO> {

	@Override
	protected Class<? extends DummySenderPO> getOperatorClass() {
		return DummySenderPO.class;
	}
	
	@Override
	public double getNetwork() {
		return getDatarate() * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
	}
}
