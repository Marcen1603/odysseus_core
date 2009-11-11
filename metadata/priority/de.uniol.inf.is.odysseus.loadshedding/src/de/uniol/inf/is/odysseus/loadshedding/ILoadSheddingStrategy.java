package de.uniol.inf.is.odysseus.loadshedding;

import java.util.List;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

/**
 * Encapsulates the functionality to calculate the current system resource capacity and
 * to activate or deactivate the load shedding.
 * @author jan
 *
 */
public interface ILoadSheddingStrategy {

	public double calcCapacity(IPartialPlan plan);

	public void activateLoadShedding(double percentToRemove, List<DirectLoadSheddingBuffer<?>> shedders);

	public void deactivateLoadShedding(List<DirectLoadSheddingBuffer<?>> shedders);
	
}
