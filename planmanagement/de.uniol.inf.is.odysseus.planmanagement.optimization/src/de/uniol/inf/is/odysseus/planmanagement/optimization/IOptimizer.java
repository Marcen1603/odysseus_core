package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * @author Jonas Jacobi, Wolf Bauer
 */
public interface IOptimizer extends IInfoProvider, IErrorEventHandler {
	public Set<String> getRegisteredBufferPlacementStrategies();

	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy);

	public IEditableExecutionPlan preStartOptimization(IQuery queryToStart,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException;

	public IEditableExecutionPlan preStopOptimization(IQuery queryToStop,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException;

	public IExecutionPlan reoptimize(IQuery sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException;

	public IExecutionPlan reoptimize(IPlan sender,
			IEditableExecutionPlan executionPlan);

	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException;

	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan, OptimizeParameter parameter)
			throws QueryOptimizationException;

	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException;

	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries, OptimizeParameter parameter)
			throws QueryOptimizationException;
}
