package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.estimators;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;

@SuppressWarnings("rawtypes")
public class JxtaSenderPOEstimator extends StandardPhysicalOperatorEstimator<JxtaSenderPO> {
	
	//TODO Move to seperate Project
	
	private static final double FALLBACK_CPU = 0.005;
	
	@Override
	protected Class<? extends JxtaSenderPO> getOperatorClass() {
		return JxtaSenderPO.class;
	}
	
	
	@Override
	public double getCpu() {
		double cpu = super.getCpu();
		//This sometimes produces too large values. Using Fallback if that happens, to kepp LBalive.
		if(cpu>1) {
			return getDatarate()*FALLBACK_CPU;
		}
		else return cpu;
	}
	
	@Override
	public double getNetwork() {
		JxtaSenderPO op = (JxtaSenderPO) getOperator();
		return op.getUploadRateBytesPerSecond();
	}
	

	

}
