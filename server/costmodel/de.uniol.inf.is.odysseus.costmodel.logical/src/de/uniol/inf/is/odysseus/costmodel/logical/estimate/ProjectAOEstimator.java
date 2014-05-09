package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class ProjectAOEstimator extends StandardLogicalOperatorEstimator<ProjectAO> {

	private static final double CPU_FACTOR = 1.5;

	@Override
	protected Class<? extends ProjectAO> getOperatorClass() {
		return ProjectAO.class;
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
