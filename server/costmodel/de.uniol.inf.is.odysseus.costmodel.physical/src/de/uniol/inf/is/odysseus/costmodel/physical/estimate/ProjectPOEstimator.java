package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

@SuppressWarnings("rawtypes")
public class ProjectPOEstimator extends StandardPhysicalOperatorEstimator<RelationalProjectPO> {

	private static final double CPU_FACTOR = 1.5;

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Class<RelationalProjectPO>> getOperatorClasses() {
		return Lists.newArrayList(RelationalProjectPO.class);
	}

	@Override
	public double getMemory() {
		return super.getMemory() + 4;
	}
	
	@Override
	public double getCpu() {
		return super.getCpu() * CPU_FACTOR;
	}
}
