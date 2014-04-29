package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class MetadataUpdatePOEstimator extends StandardPhysicalOperatorEstimator<MetadataUpdatePO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<MetadataUpdatePO>> getOperatorClasses() {
		return Lists.newArrayList(MetadataUpdatePO.class);
	}

	@Override
	public double getMemory() {
		return super.getMemory() + 4;
	}
}
