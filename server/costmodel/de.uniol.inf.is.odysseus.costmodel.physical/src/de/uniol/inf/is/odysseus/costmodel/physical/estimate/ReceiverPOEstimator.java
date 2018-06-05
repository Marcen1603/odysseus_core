package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class ReceiverPOEstimator extends StandardPhysicalOperatorEstimator<ReceiverPO> {

	@Override
	public Class<? extends ReceiverPO> getOperatorClass() {
		return ReceiverPO.class;
	}

	@Override
	public double getDatarate() {
		Optional<Double> optDatarate = getCostModelKnowledge().getDatarate(getOperator().getName());
		return optDatarate.isPresent() ? optDatarate.get() : super.getDatarate();
	}
	
}
