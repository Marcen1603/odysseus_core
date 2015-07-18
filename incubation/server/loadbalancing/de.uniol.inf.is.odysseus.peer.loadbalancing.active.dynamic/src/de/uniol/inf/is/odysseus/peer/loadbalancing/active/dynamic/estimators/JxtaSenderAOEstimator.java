package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.estimators;

import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

public class JxtaSenderAOEstimator extends StandardLogicalOperatorEstimator<JxtaSenderAO> {

	@Override
	protected Class<? extends JxtaSenderAO> getOperatorClass() {
		return JxtaSenderAO.class;
	}
	
	@Override
	//To estimate network bandwith guess, how many bytes need to be processed per second to not be a bottleneck
	public double getNetwork() {
		return getDatarate()*EstimatorHelper.sizeInBytes(getOperator().getInputSchema(0));
	}
	
	
}
