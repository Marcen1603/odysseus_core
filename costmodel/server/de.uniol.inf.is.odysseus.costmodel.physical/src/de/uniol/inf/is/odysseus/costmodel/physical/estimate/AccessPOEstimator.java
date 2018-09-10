package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class AccessPOEstimator extends StandardPhysicalOperatorEstimator<AccessPO> {

	@Override
	protected Class<? extends AccessPO> getOperatorClass() {
		return AccessPO.class;
	}
	
	// TODO: Estimations are dependent from protocal handlers
}
