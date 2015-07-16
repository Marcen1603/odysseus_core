package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.estimators;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

@SuppressWarnings("rawtypes")
public class JxtaSenderPOEstimator extends StandardPhysicalOperatorEstimator<JxtaSenderPO> {
	@Override
	protected Class<? extends JxtaSenderPO> getOperatorClass() {
		return JxtaSenderPO.class;
	}
	
	
	@Override
	public double getNetwork() {
		JxtaSenderPO op = (JxtaSenderPO) getOperator();
		return op.getUploadRateBytesPerSecond();
	}
	

}
