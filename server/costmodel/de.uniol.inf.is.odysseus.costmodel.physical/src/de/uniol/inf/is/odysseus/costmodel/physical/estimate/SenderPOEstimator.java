package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class SenderPOEstimator extends StandardPhysicalOperatorEstimator<SenderPO> {

	@Override
	protected Class<? extends SenderPO> getOperatorClass() {
		return SenderPO.class;
	}
	
	// TODO: Estimations are dependent from protocal handlers
}
