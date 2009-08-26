package de.uniol.inf.is.odysseus.base.planmanagement;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

public interface IBufferPlacementStrategy {
	public void addBuffers(IPhysicalOperator plan);

	public String getName();
}
