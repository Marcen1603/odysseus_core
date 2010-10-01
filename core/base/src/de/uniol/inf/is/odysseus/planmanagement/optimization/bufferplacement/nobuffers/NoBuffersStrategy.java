package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.nobuffers;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

/**
 * @author Jonas Jacobi
 */
public class NoBuffersStrategy implements IBufferPlacementStrategy{

	@Override
	public void addBuffers(IPhysicalOperator plan) {
	}

	@Override
	public String getName() {
		return "No Buffers";
	}

}
