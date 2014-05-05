package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

@SuppressWarnings("rawtypes")
public class ProjectPOEstimator extends StandardPhysicalOperatorEstimator<RelationalProjectPO> {

	private static final double CPU_FACTOR = 1.5;

	@Override
	public Class<? extends RelationalProjectPO> getOperatorClass() {
		return RelationalProjectPO.class;
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
