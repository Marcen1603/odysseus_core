package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.estimators;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

@SuppressWarnings("rawtypes")
public class JxtaSenderPOEstimator extends StandardPhysicalOperatorEstimator<JxtaSenderPO> {
	
	//TODO Move to seperate Project
	
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
