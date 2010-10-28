package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.IOptimizationSetting;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanExecutionCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.IPlanOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

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

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractOptimizer.class);
		}
		return _logger;
	}

	/**
	 * Map of registered buffer placement strategies.
	 */
	protected Map<String, IBufferPlacementStrategy> bufferPlacementStrategies = new HashMap<String, IBufferPlacementStrategy>();

	/**
	 * List of PostOptimizationActions
	 */
	private List<IPostOptimitzationAction> postOptimizationActions = new ArrayList<IPostOptimitzationAction>();
	
	/**
	 * Registered plan optimization service.
	 */
	protected IPlanOptimizer planOptimizer;

	/**
	 * Registered query optimization service.
	 */
	protected IQueryOptimizer queryOptimizer;

	/**
	 * Registered plan migration services.
	 */
	private Map<String, IPlanMigrationStrategy> planMigrationStrategies = new HashMap<String, IPlanMigrationStrategy>();

	private IPlanExecutionCostModel executionCostModel;
	private IPlanMigrationCostModel migrationCostModel;
	
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
		getLogger().trace("Create Executor.");
	}

	/**
	 * Method to bind a {@link IBufferPlacementStrategy}. Used by OSGi.
	 * 
	 * @param bufferPlacementStrategy new {@link IBufferPlacementStrategy} service
	 */
	public void bindBufferPlacementStrategy(
			IBufferPlacementStrategy bufferPlacementStrategy) {
		String bpN = bufferPlacementStrategy.getName();
		getLogger().debug("bindBufferPlacementStrategy "+bpN);
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

	public void bindPostOptimazationAction(
			IPostOptimitzationAction postOptimitzationAction) {
		getLogger().debug("bindPostOptimazationAction "+postOptimitzationAction);
		synchronized (this.postOptimizationActions) {
			this.postOptimizationActions.add(postOptimitzationAction);
		}
	}
	
	public void unBindPostOptimazationAction(
			IPostOptimitzationAction postOptimitzationAction) {
		getLogger().debug("unBindPostOptimazationAction "+postOptimitzationAction);
		synchronized (this.postOptimizationActions) {
			this.postOptimizationActions.remove(postOptimitzationAction);
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
		getLogger().debug("Bind planmigration strategy "+planMigrationStrategy.getName());
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

	public void bindExecutionCostModel(IPlanExecutionCostModel executionCostModel) {
		this.executionCostModel = executionCostModel;
	}
	
	public void unbindExecutionCostModel(IPlanExecutionCostModel executionCostModel) {
		this.executionCostModel = null;
	}
	
	public IPlanExecutionCostModel getExecutionCostModel() {
		return executionCostModel;
	}
	
	public void bindMigrationCostModel(IPlanMigrationCostModel migrationCostModel) {
		this.migrationCostModel = migrationCostModel;
	}
	
	public void unbindMigrationCostModel(IPlanMigrationCostModel migrationCostModel) {
		this.migrationCostModel = null;
	}
	
	public IPlanMigrationCostModel getMigrationCostModel() {
		return migrationCostModel;
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
	@Override
	public void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.errorEventOccured(eventArgs);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preStartOptimization(de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan beforeQueryStart(IQuery queryToStart,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preQueryRemoveOptimization(de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable, de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan, de.uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration.AbstractOptimizationSetting<?>[])
	 */
	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan beforeQueryRemove(
			T sender, IQuery removedQuery,
			IExecutionPlan executionPlan,
			IOptimizationSetting<?>... parameters)
			throws QueryOptimizationException {
		return beforeQueryRemove(sender, removedQuery, executionPlan,
				new OptimizationConfiguration(parameters));
	};

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preQueryAddOptimization(de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable, java.util.List, de.uniol.inf.is.odysseus.planmanagement.optimization.OptimizationConfiguration.AbstractOptimizationSetting<?>[])
	 */
	@Override
	public IExecutionPlan optimize(IOptimizable sender,
			List<IQuery> newQueries,
			IOptimizationSetting... parameters)
			throws QueryOptimizationException {
		return optimize(sender, newQueries,
				new OptimizationConfiguration(parameters));
	};
	
	protected void doPostOptimizationActions(IQuery query, OptimizationConfiguration parameter) {
		for (IPostOptimitzationAction action: postOptimizationActions){
			action.run(query, parameter);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender, IQuery query,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#reoptimize(de.uniol.inf.is.odysseus.planmanagement.plan.IPlan, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		return executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer#preStopOptimization(de.uniol.inf.is.odysseus.planmanagement.query.IQuery, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan beforeQueryStop(IQuery queryToStop,
			IExecutionPlan execPlan) throws QueryOptimizationException {
		return execPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IInfoProvider#getInfos()
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
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#addErrorEventListener(de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.add(errorEventListener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler#removeErrorEventListener(de.uniol.inf.is.odysseus.event.error.IErrorEventListener)
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
