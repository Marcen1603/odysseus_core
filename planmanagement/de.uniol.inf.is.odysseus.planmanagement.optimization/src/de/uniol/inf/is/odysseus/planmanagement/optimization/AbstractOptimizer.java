package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;

/**
 * AbstractOptimizer implements base optimization functions. It manages services
 * for query optimization, plan optimization, buffer placement and plan
 * migration.
 * 
 * @author Wolf Bauer, Jonas Jacobi
 */
/**
 * @author Wolf Bauer
 *
 */
public abstract class AbstractOptimizer implements IOptimizer {

	/**
	 * Logger for detailed informations.
	 */
	protected Logger logger = LoggerFactory.getLogger(AbstractOptimizer.class);

	/**
	 * Map of registered buffer placement strategies.
	 */
	protected Map<String, IBufferPlacementStrategy> bufferPlacementStrategies = new HashMap<String, IBufferPlacementStrategy>();

	/**
	 * Registered plan optimization service.
	 */
	protected IPlanOptimizer planOptimizer;

	/**
	 * Registered query optimization service.
	 */
	protected IQueryOptimizer queryOptimizer;

	/**
	 * Registered plan migration service.
	 */
	// protected IPlanMigrationStrategy planMigrationStrategy;
	private Map<String, IPlanMigrationStrategy> planMigrationStrategies = new HashMap<String, IPlanMigrationStrategy>();

	// protected IBufferPlacementStrategy bufferPlacementStrategy;

	/**
	 * List of error event listener. If an error occurs these objects should be informed.
	 */
	private ArrayList<IErrorEventListener> errorEventListener = new ArrayList<IErrorEventListener>();
	
	protected OptimizationConfiguration configuration = new OptimizationConfiguration();

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	public void activate() {
		// load logger
		this.logger.trace("Create Executor.");
	}

	/**
	 * Method to bind a {@link IBufferPlacementStrategy}. Used by OSGi.
	 * 
	 * @param bufferPlacementStrategy new {@link IBufferPlacementStrategy} service
	 */
	public void bindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		String bpN = bufferPlacementStrategy.getName();
		logger.debug("bindBufferPlacementStrategy "+bpN);
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.put(bpN, bufferPlacementStrategy);
		}
	}

	/**
	 * Method to unbind a {@link IBufferPlacementStrategy}. Used by OSGi.
	 * 
	 * @param bufferPlacementStrategy {@link IBufferPlacementStrategy} service to unbind
	 */
	public void unbindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		synchronized (this.bufferPlacementStrategies) {
			this.bufferPlacementStrategies.remove(bufferPlacementStrategy
					.getName());
		}
	}

	/**
	 * Method to bind a {@link IPlanOptimizer}. Used by OSGi.
	 * 
	 * @param planOptimizer new {@link IPlanOptimizer} service
	 */
	public void bindPlanOptimizer(IPlanOptimizer planOptimizer) {
		this.planOptimizer = planOptimizer;
	}

	/**
	 * Method to unbind a {@link IPlanOptimizer}. Used by OSGi.
	 * 
	 * @param planOptimizer {@link IPlanOptimizer} service to unbind
	 */
	public void unbindPlanOptimizer(IPlanOptimizer planOptimizer) {
		if (this.planOptimizer == planOptimizer) {
			this.planOptimizer = null;
		}
	}

	/**
	 * Method to bind a {@link IQueryOptimizer}. Used by OSGi.
	 * 
	 * @param queryOptimizer new {@link IQueryOptimizer} service
	 */
	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
	}

	/**
	 * Method to unbind a {@link IQueryOptimizer}. Used by OSGi.
	 * 
	 * @param queryOptimizer {@link IQueryOptimizer} service to unbind
	 */
	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		if (this.queryOptimizer == queryOptimizer) {
			this.queryOptimizer = null;
		}
	}

	/**
	 * Method to bind a {@link IPlanMigrationStrategy}. Used by OSGi.
	 * 
	 * @param planMigrationStrategy new {@link IPlanMigrationStrategy} service
	 */
	public void bindPlanMigrationStrategy(
			IPlanMigrationStrategy planMigrationStrategy) {
		logger.debug("Bind planmigration strategy "+planMigrationStrategy.getName());
		this.planMigrationStrategies.put(planMigrationStrategy.getName(), planMigrationStrategy);
	}

	/**
	 * Method to unbind a {@link IPlanMigrationStrategy}. Used by OSGi.
	 * 
	 * @param planMigrationStrategy {@link IPlanMigrationStrategy} service to unbind
	 */
	public void unbindPlanMigrationStrategy(
			IPlanMigrationStrategy planMigrationStrategy) {
		this.planMigrationStrategies.remove(planMigrationStrategy.getName());
	}

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + class name of object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(Object object, String label) {
		return getInfoString(object.toString(), label);
	}

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(String object, String label) {
		String infos = AppEnv.LINE_SEPARATOR + label + ": ";
		if (object != null) {
			infos += object;
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	/**
	 * Sends an {@link ErrorEvent} to all registered EventListenern
	 * 
	 * @param eventArgs {@link ErrorEvent} to send
	 */
	protected void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preStartOptimization(de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan)
	 */
	@Override
	public IEditableExecutionPlan preStartOptimization(IQuery queryToStart,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preQueryRemoveOptimization(de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable, de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter<?>[])
	 */
	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		return preQueryRemoveOptimization(sender, removedQuery, executionPlan,
				new OptimizeParameter(parameters));
	};

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preQueryAddOptimization(de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable, java.util.List, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter<?>[])
	 */
	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		return preQueryAddOptimization(sender, newQueries,
				new OptimizeParameter(parameters));
	};
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preQueryAddOptimization(de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable, java.util.List, de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter<?>[])
	 */
	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries,
			Set<String> rulesToUse,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		return preQueryAddOptimization(sender, newQueries,
				new OptimizeParameter(parameters), rulesToUse);
	};

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan)
	 */
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender, IEditableQuery query,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan)
	 */
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preStopOptimization(de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan)
	 */
	@Override
	public IEditableExecutionPlan preStopOptimization(IQuery queryToStop,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException {
		return execPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider#getInfos()
	 */
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

//		infos += getInfoString(this.planMigrationStrategy,
//				"PlanMigrationStrategie");
		infos += getInfoString(this.planOptimizer, "PlanOptimizer");
		infos += getInfoString(this.queryOptimizer, "QueryOptimizer");

		infos += AppEnv.LINE_SEPARATOR + "</Optimizer> ";

		return infos;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#getRegisteredBufferPlacementStrategies()
	 */
	@Override
	public Set<String> getRegisteredBufferPlacementStrategies() {
		return this.bufferPlacementStrategies.keySet();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#getBufferPlacementStrategy(java.lang.String)
	 */
	@Override
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy) {
		return this.bufferPlacementStrategies.get(strategy);
	}
	
	public Set<String> getRegisteredPlanMigrationStrategies(){
		return this.planMigrationStrategies.keySet();		
	}

	public IPlanMigrationStrategy getPlanMigrationStrategy(String strategy){
		return this.planMigrationStrategies.get(strategy);		
	}

	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler#addErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.add(errorEventListener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler#removeErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}
	
	@Override
	public OptimizationConfiguration getConfiguration() {
		return this.configuration;
	}
}
