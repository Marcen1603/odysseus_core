package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;

/**
 * @author Wolf Bauer, Jonas Jacobi
 */
public abstract class AbstractOptimizer implements IOptimizer {

	protected Logger logger;

	protected Map<String, IBufferPlacementStrategy> bufferPlacementStrategies = new HashMap<String, IBufferPlacementStrategy>();

	protected IPlanOptimizer planOptimizer;

	protected IQueryOptimizer queryOptimizer;

	protected IPlanMigrationStrategie planMigrationStrategie;

	// protected IBufferPlacementStrategy bufferPlacementStrategy;

	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();

	public void activate() {
		this.logger = LoggerFactory.getLogger(AbstractOptimizer.class);
		this.logger.trace("Create Executor.");
	}

	public void bindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		String bpN = bufferPlacementStrategy.getName();
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.put(bpN, bufferPlacementStrategy);
		}
	}

	public void unbindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.remove(bufferPlacementStrategy
					.getName());
		}
	}

	public void bindPlanOptimizer(IPlanOptimizer planOptimizer) {
		this.planOptimizer = planOptimizer;
	}

	public void unbindPlanOptimizer(IPlanOptimizer planOptimizer) {
		if (this.planOptimizer == planOptimizer) {
			this.planOptimizer = null;
		}
	}

	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
	}

	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		if (this.queryOptimizer == queryOptimizer) {
			this.queryOptimizer = null;
		}
	}

	public void bindPlanMigrationStrategie(
			IPlanMigrationStrategie planMigrationStrategie) {
		this.planMigrationStrategie = planMigrationStrategie;
	}

	public void unbindPlanMigrationStrategie(
			IPlanMigrationStrategie planMigrationStrategie) {
		if (this.planMigrationStrategie == planMigrationStrategie) {
			this.planMigrationStrategie = null;
		}
	}

	public String getInfoString(Object object, String label) {
		String infos = AppEnv.LINE_SEPERATOR + label + ": ";
		if (object != null) {
			infos += object.getClass();
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	protected void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
		}
	}

	@Override
	public IEditableExecutionPlan preStartOptimization(IQuery queryToStart,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		return preQueryRemoveOptimization(sender, removedQuery, executionPlan,
				new OptimizeParameter(parameters));
	};

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		return preQueryAddOptimization(sender, newQueries,
				new OptimizeParameter(parameters));
	};

	@Override
	public IExecutionPlan reoptimize(IQuery sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return null;
	}

	@Override
	public IExecutionPlan reoptimize(IPlan sender,
			IEditableExecutionPlan executionPlan) {
		return null;
	}

	@Override
	public IEditableExecutionPlan preStopOptimization(IQuery queryToStop,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException {
		return null;
	}

	@Override
	public String getInfos() {
		String infos = "<Optimizer class=\"" + this + "\"> ";

		infos += "<BufferPlacementStrategy>";
		for (String bufferPlacementStrategy : this.bufferPlacementStrategies
				.keySet()) {
			infos += getInfoString(bufferPlacementStrategy,
					"BufferPlacementStrategy comp.: ");
		}
		infos += "</BufferPlacementStrategy>";

		infos += getInfoString(this.planMigrationStrategie,
				"PlanMigrationStrategie");
		infos += getInfoString(this.planOptimizer, "PlanOptimizer");
		infos += getInfoString(this.queryOptimizer, "QueryOptimizer");

		infos += AppEnv.LINE_SEPERATOR + "</Optimizer> ";

		return infos;
	}

	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		return this.bufferPlacementStrategies.keySet();
	}

	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		return this.bufferPlacementStrategies.get(strategy);
	}

	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.add(errorEventListener);
	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
}
