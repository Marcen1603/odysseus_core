package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class MetadataUpdatePOEstimator extends StandardPhysicalOperatorEstimator<MetadataUpdatePO> {

	@Override
	public Class<? extends MetadataUpdatePO> getOperatorClass() {
		return MetadataUpdatePO.class;
	}

	@Override
	public double getMemory() {
		return super.getMemory() + 4;
	}
}
