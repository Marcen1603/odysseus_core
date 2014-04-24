package de.uniol.inf.is.odysseus.p2p_new.costmodel;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.DummyReceiverPO;

@SuppressWarnings("rawtypes")
public class DummyReceiverPOEstimator extends StandardPhysicalOperatorEstimator<DummyReceiverPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<DummyReceiverPO>> getOperatorClasses() {
		return Lists.newArrayList(DummyReceiverPO.class);
	}

	@Override
	public double getDatarate() {
		return getOperator().getDataRate();
	}

	@Override
	public double getWindowSize() {
		return getOperator().getIntervalLength();
	}
}
