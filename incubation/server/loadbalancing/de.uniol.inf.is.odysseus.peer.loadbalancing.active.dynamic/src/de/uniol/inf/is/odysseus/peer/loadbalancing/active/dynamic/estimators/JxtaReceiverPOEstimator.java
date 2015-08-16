package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.estimators;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.transmission.ITransmissionReceiver;

@SuppressWarnings("rawtypes")
public class JxtaReceiverPOEstimator extends
		StandardPhysicalOperatorEstimator<JxtaReceiverPO> {
	

	//TODO Move to seperate Project

	@Override
	protected Class<? extends JxtaReceiverPO> getOperatorClass() {
		return JxtaReceiverPO.class;
	}
	
	@Override
	public double getMemory() {
		JxtaReceiverPO op = (JxtaReceiverPO) getOperator();
		ITransmissionReceiver transmission = op.getTransmission();
		if(transmission==null) {
			return 0.0;
		}
		else {
			return transmission.getBufferSize();
		}
	}
	
	@Override
	public double getNetwork() {
		JxtaReceiverPO op = (JxtaReceiverPO) getOperator();
		return op.getDownloadRateBytesPerSecond();
	}
	

	
	//To avoid infinity Estimation and as a JxtaReceiverPO is usually more limited by network than by window size, we return Datarate instead of window size... 
	//TODO Find better solution!
	@Override
	public double getWindowSize() {
		return getDatarate();
	}
}
