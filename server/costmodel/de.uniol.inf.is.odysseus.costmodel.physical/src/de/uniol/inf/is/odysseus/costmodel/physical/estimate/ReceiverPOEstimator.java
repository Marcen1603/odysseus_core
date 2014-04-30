package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class ReceiverPOEstimator extends StandardPhysicalOperatorEstimator<ReceiverPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<ReceiverPO>> getOperatorClasses() {
		return Lists.newArrayList(ReceiverPO.class);
	}

	@Override
	public double getDatarate() {
		Optional<Double> optDatarate = getCostModelKnowledge().getDatarate(getOperator().getName());
		return optDatarate.isPresent() ? optDatarate.get() : super.getDatarate();
	}
}
