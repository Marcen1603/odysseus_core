package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanExecutionCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;


/**
 * The {@link StandardOptimizer} installs monitoring points in physical plans.
 * On reoptimize requests, the optimizer initiates a plan migration to a more
 * effient plan.
 * 
 * @author Tobias Witt
 * 
 */
public class StandardOptimizer extends AbstractOptimizer {
	
	Logger logger = null;
	
	private IPlanExecutionCostModel executionCostModel;
	private IPlanMigrationCostModel migrationCostModel;
	
	private Map<Integer, PlanMigrationContext> optimizationContext;
	private Queue<IQuery> pendingRequests;
	
	public static final long MONITORING_PERIOD = 30000;
	
	public StandardOptimizer() {
		logger = LoggerFactory.getLogger(StandardOptimizer.class);
		this.optimizationContext = new HashMap<Integer, PlanMigrationContext>();
//		this.planMigrationStrategies = new ArrayList<IPlanMigrationStrategy>();
		this.pendingRequests = new LinkedList<IQuery>();
	}
	
	public void bindExecutionCostModel(IPlanExecutionCostModel executionCostModel) {
		this.executionCostModel = executionCostModel;
	}
	
	public void unbindExecutionCostModel(IPlanExecutionCostModel executionCostModel) {
		this.executionCostModel = null;
	}
	
	public void bindMigrationCostModel(IPlanMigrationCostModel migrationCostModel) {
		this.migrationCostModel = migrationCostModel;
	}
	
	public void unbindMigrationCostModel(IPlanMigrationCostModel migrationCostModel) {
		this.migrationCostModel = null;
	}
	
//	@Override
//	public void bindPlanMigrationStrategy(IPlanMigrationStrategy planMigrationStrategy) {
//		logger.debug("Bind planmigration strategy "+planMigrationStrategy);
//		this.planMigrationStrategies.add(planMigrationStrategy);
//		super.bindPlanMigrationStrategy(planMigrationStrategy);
//	}
	
//	@Override
//	public void unbindPlanMigrationStrategy(IPlanMigrationStrategy planMigrationStrategy) {
//		this.planMigrationStrategies.remove(planMigrationStrategy);
//		super.unbindPlanMigrationStrategy(planMigrationStrategy);
//	}
	
//	@Override
//	public void bindBufferPlacementStrategy(
//			IBufferPlacementStrategy bufferPlacementStrategy) {
//		super.bindBufferPlacementStrategy(bufferPlacementStrategy);
//	}
//	
//	@Override
//	public void unbindBufferPlacementStrategy(
//			IBufferPlacementStrategy bufferPlacementStrategy) {
//		super.unbindBufferPlacementStrategy(bufferPlacementStrategy);
//	}

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IQuery> queries, OptimizeParameter parameter)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter);
				updateMetadataListener(editableQuery);
			}

			List<IQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return newExecutionPlan;
		}
		return sender.getEditableExecutionPlan();
	}

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IQuery> queries, OptimizeParameter parameter, Set<String> rulesToUse)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter, rulesToUse);
				updateMetadataListener(editableQuery);
			}

			List<IQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return newExecutionPlan;
			
		}
		return sender.getEditableExecutionPlan();
	}

	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IExecutionPlan executionPlan, OptimizeParameter parameter)
			throws QueryOptimizationException {
		ArrayList<IQuery> newPlan = new ArrayList<IQuery>(
				sender.getRegisteredQueries());
		newPlan.remove(removedQuery);

		IExecutionPlan newExecutionPlan = this.planOptimizer
				.optimizePlan(sender, parameter, newPlan);

		return newExecutionPlan;
	}
	
	private void updateMetadataListener(IQuery editableQuery) {
		AbstractTreeWalker.prefixWalk2(editableQuery.getRoot(), new InstallMetadataListenerVisitor());
	}
	
	@Override
	public IExecutionPlan preQueryMigrateOptimization(IOptimizable sender,
			OptimizeParameter parameter) throws QueryOptimizationException {
		ArrayList<IQuery> newPlan = new ArrayList<IQuery>(
				sender.getRegisteredQueries());
		IExecutionPlan newExecutionPlan = this.planOptimizer
			.optimizePlan(sender, parameter, newPlan);
		return newExecutionPlan;
	}
	
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender, IQuery query,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		this.logger.info("Start reoptimize query ID "+query.getID());
		
		if (this.getRegisteredPlanMigrationStrategies().isEmpty()) {
			this.logger.warn("No plan migration strategies available. Aborting.");
			return executionPlan;
		}
		
		// high system load test
		try {
			ISystemMonitor monitor = ((IAdvancedExecutor)sender).getDefaultSystemMonitor();
			double cpuLoad = monitor.getAverageCPULoad();
			double memLoad = monitor.getHeapMemoryUsage();
			if (cpuLoad >= this.configuration.getSettingRefuseOptimizationAtCpuLoad().getValue()) {
				queueRequest(query, "System CPU load is currently too high ("+cpuLoad+").");
				new LowSystemLoadWaiter(((IAdvancedExecutor)sender).newSystemMonitor(MONITORING_PERIOD),
						this, this.configuration.getSettingRefuseOptimizationAtCpuLoad().getValue()*0.8,
						this.configuration.getSettingRefuseOptimizationAtMemoryLoad().getValue()*0.8);
				return executionPlan;
			}
			if (memLoad >= this.configuration.getSettingRefuseOptimizationAtMemoryLoad().getValue()) {
				queueRequest(query, "System memory load is currently too high ("+memLoad+").");
				new LowSystemLoadWaiter(((IAdvancedExecutor)sender).newSystemMonitor(MONITORING_PERIOD),
						this, this.configuration.getSettingRefuseOptimizationAtCpuLoad().getValue()*0.8,
						this.configuration.getSettingRefuseOptimizationAtMemoryLoad().getValue()*0.8);
				return executionPlan;
			}
		} catch (NoSystemMonitorLoadedException e1) {}
		
		// optimization lock on query
		if (this.optimizationContext.containsKey(query.getID())) {
			this.logger.warn("Aborted reoptimization. Query with ID "+query.getID()+" is currently getting optimized.");
			return executionPlan;
		} else if (this.optimizationContext.size() >= this.configuration.getSettingMaxConcurrentOptimizations().getValue()) {
			queueRequest(query, "There are currently "+this.optimizationContext.size()+" optimizations running.");
			return executionPlan;
		}
		PlanMigrationContext context = new PlanMigrationContext(query);
		this.optimizationContext.put(query.getID(), context);
		
		try {
			// build alternative physical plans
			this.logger.debug("Building alternative plans.");
			Map<IPhysicalOperator,ILogicalOperator> alternatives = this.queryOptimizer.createAlternativePlans(
					sender, query, 
					new OptimizeParameter(ParameterDoRestruct.TRUE), null);
			
			// pick out optimal plan by cost analysis
			List<IPhysicalOperator> candidates = this.executionCostModel.getCostCalculator().pickBest(
					alternatives.keySet(), this.configuration.getSettingComparePlanCandidates().getValue());
			if (candidates.isEmpty()) {
				this.logger.info("No alternative plans for query ID "+query.getID());
				this.optimizationContext.remove(query.getID());
				return executionPlan;
			}
			
			// calculate migration overhead with every registered strategy
			List<PlanMigration> migrationCandidates = new ArrayList<PlanMigration>();
			for (IPhysicalOperator cPlan : candidates) {
				for (String strategy : this.getRegisteredPlanMigrationStrategies()) {
					migrationCandidates.add(new PlanMigration(query.getRoot(), cPlan, getPlanMigrationStrategy(strategy)));
				}
			}
			// pick near optimal plan with acceptable migration cost
			PlanMigration optimalMigration = this.migrationCostModel.getCostCalculator().pickBest(migrationCandidates);
			context.setRoot(optimalMigration.getNewPlan());
			context.setLogicalPlan(alternatives.get(optimalMigration.getNewPlan()));
			context.setSender(sender);
			
			// start migration to new plan 
			this.logger.info("Start migration to new physical plan (query ID "+query.getID()+")");
			optimalMigration.getStrategy().migrateQuery(this, query, optimalMigration.getNewPlan());
			
			((IAdvancedExecutor)sender).updateExecutionPlan();
			
			// wait for migration end callback
			this.logger.info("Plan migration running (query ID "+query.getID()+")");
			
		} catch (Exception e) {
			//this.optimizationContext.remove(query.getID());
			this.logger.warn("Reoptimization failed. (query ID "+query.getID()+")",e);
		}
		
		return executionPlan;
	}

	@Override
	public void handleFinishedMigration(IQuery query) {
		PlanMigrationContext context = this.optimizationContext.get(query.getID());

		try {
			// set new logical plan
			query.setLogicalPlan(context.getLogicalPlan());
			
			// update execution plan
			((IAdvancedExecutor)context.getSender()).updateExecutionPlan();
			
			// reinstall metadata listener
			updateMetadataListener(query);

			// remove lock and context
			this.optimizationContext.remove(query.getID());
			this.logger.info("Finished plan migration (query ID "
					+ query.getID() + ")");
			
			// handle pending requests
			processPendingRequests();

		} catch (Exception e) {
			// this.optimizationContext.remove(sender.getID());
			this.logger.warn("Reoptimization failed. (query ID "
					+ query.getID() + ")", e);
		}
	}
	
	private void queueRequest(IQuery query, String reason) {
		this.logger.warn("Reoptimization request queued. "+reason);
		if (!this.pendingRequests.contains(query)) {
			this.pendingRequests.offer(query);
		}
	}
	
	@Override
	public IExecutionPlan reoptimize(IOptimizable sender,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException {
		for (IQuery query : sender.getRegisteredQueries()) {
			reoptimize(sender, query, executionPlan);
		}
		return executionPlan;
	}
	
	void processPendingRequests() {
		if (!this.pendingRequests.isEmpty()) {
			this.pendingRequests.poll().reoptimize();
		}
	}

}
