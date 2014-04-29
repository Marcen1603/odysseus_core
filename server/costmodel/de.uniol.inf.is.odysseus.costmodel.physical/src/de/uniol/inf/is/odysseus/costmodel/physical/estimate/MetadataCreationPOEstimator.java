package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;

@SuppressWarnings("rawtypes")
public class MetadataCreationPOEstimator extends StandardPhysicalOperatorEstimator<MetadataCreationPO> {

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<MetadataCreationPO>> getOperatorClasses() {
		return Lists.newArrayList(MetadataCreationPO.class);
	}

	@Override
	public double getMemory() {
		return super.getMemory() + 8;
	}
}
