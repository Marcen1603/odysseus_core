package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize.AbstractStartEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;

public interface IOptimizer extends IInfoProvider, IErrorEventHandler {
	public Set<String> getRegisteredBufferPlacementStrategies();
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy);
	
	public void optimizeStartEvent(
			AbstractStartEvent<?> eventArgs)
			throws QueryOptimizationException;
}
