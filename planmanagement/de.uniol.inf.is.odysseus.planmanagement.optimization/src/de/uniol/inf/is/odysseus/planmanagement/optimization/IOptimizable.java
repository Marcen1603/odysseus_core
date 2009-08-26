package de.uniol.inf.is.odysseus.planmanagement.optimization;

import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;

public interface IOptimizable extends IQueryOptimizable, IPlanOptimizable, IPlanMigratable {
	public void optimizeEndEvent(AbstractEndEvent<?> eventArgs) throws QueryOptimizationException;
}