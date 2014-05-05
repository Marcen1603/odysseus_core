package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class MetadataCreationPOEstimator extends StandardPhysicalOperatorEstimator<MetadataCreationPO> {

	@Override
	public Class<? extends MetadataCreationPO> getOperatorClass() {
		return MetadataCreationPO.class;
	}

	@Override
	public double getMemory() {
		return super.getMemory() + 8;
	}
}
