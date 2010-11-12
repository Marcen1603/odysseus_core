package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPostOptimizationAction;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * 
 * @author Wolf Bauer, Tobias Witt, Marco Grawunder
 * 
 */
public class StandardOptimizer extends AbstractOptimizer {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(StandardOptimizer.class);
		}
		return _logger;
	}

	private Map<Integer, PlanMigrationContext> optimizationContext;
	private Queue<IQuery> pendingRequests;

	public static final long MONITORING_PERIOD = 30000;

	public StandardOptimizer() {
		this.optimizationContext = new HashMap<Integer, PlanMigrationContext>();
		this.pendingRequests = new LinkedList<IQuery>();		
	}

	@Override
	public IExecutionPlan optimize(IOptimizable sender, List<IQuery> queries,
			OptimizationConfiguration parameter)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IQuery query : queries) {
				this.queryOptimizer.optimizeQuery(sender, query, parameter);
				
				doPostOptimizationActions(query, parameter);
			}
			List<IQuery> newPlan = new ArrayList<IQuery>(sender.getQueries());
			newPlan.addAll(queries);

			IExecutionPlan newExecutionPlan = this.planOptimizer.optimizePlan(
					sender, parameter, newPlan);

			return newExecutionPlan;
		}
		return sender.getExecutionPlan();
	}

	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan beforeQueryRemove(
			T sender, IQuery removedQuery, IExecutionPlan executionPlan,
			OptimizationConfiguration parameter)
			throws QueryOptimizationException {
		ArrayList<IQuery> newPlan = new ArrayList<IQuery>(sender.getQueries());
		newPlan.remove(removedQuery);

		IExecutionPlan newExecutionPlan = this.planOptimizer.optimizePlan(
				sender, parameter, newPlan);

		return newExecutionPlan;
	}



	@Override
	public IExecutionPlan beforeQueryMigration(IOptimizable sender,
			OptimizationConfiguration parameter)
			throws QueryOptimizationException {
		ArrayList<IQuery> newPlan = new ArrayList<IQuery>(sender.getQueries());
		IExecutionPlan newExecutionPlan = this.planOptimizer.optimizePlan(
				sender, parameter, newPlan);
		return newExecutionPlan;
	}

	@Override
	public IExecutionPlan reoptimize(IOptimizable sender, IQuery query,
			IExecutionPlan executionPlan) throws QueryOptimizationException {

		if (query.containsCycles()) {
			throw new RuntimeException(
					"StandardOptimizer assumes acyclic trees, \n");
		}

		getLogger().info("Start reoptimize query ID " + query.getID());

		if (this.getRegisteredPlanMigrationStrategies().isEmpty()) {
			getLogger().warn(
					"No plan migration strategies available. Aborting.");
			return executionPlan;
		}

		// high system load test
		try {
			ISystemMonitor monitor = ((IExecutor) sender)
					.getDefaultSystemMonitor();
			double cpuLoad = monitor.getAverageCPULoad();
			double memLoad = monitor.getHeapMemoryUsage();
			if (cpuLoad >= this.configuration
					.getSettingRefuseOptimizationAtCpuLoad().getValue()) {
				queueRequest(query, "System CPU load is currently too high ("
						+ cpuLoad + ").");
				new LowSystemLoadWaiter(
						((IExecutor) sender)
								.newSystemMonitor(MONITORING_PERIOD),
						this, this.configuration
								.getSettingRefuseOptimizationAtCpuLoad()
								.getValue() * 0.8, this.configuration
								.getSettingRefuseOptimizationAtMemoryLoad()
								.getValue() * 0.8);
				return executionPlan;
			}
			if (memLoad >= this.configuration
					.getSettingRefuseOptimizationAtMemoryLoad().getValue()) {
				queueRequest(query,
						"System memory load is currently too high (" + memLoad
								+ ").");
				new LowSystemLoadWaiter(
						((IExecutor) sender)
								.newSystemMonitor(MONITORING_PERIOD),
						this, this.configuration
								.getSettingRefuseOptimizationAtCpuLoad()
								.getValue() * 0.8, this.configuration
								.getSettingRefuseOptimizationAtMemoryLoad()
								.getValue() * 0.8);
				return executionPlan;
			}
		} catch (NoSystemMonitorLoadedException e1) {
		}

		// optimization lock on query
		if (this.optimizationContext.containsKey(query.getID())) {
			getLogger().warn(
					"Aborted reoptimization. Query with ID " + query.getID()
							+ " is currently getting optimized.");
			return executionPlan;
		} else if (this.optimizationContext.size() >= this.configuration
				.getSettingMaxConcurrentOptimizations().getValue()) {
			queueRequest(query, "There are currently "
					+ this.optimizationContext.size()
					+ " optimizations running.");
			return executionPlan;
		}
		PlanMigrationContext context = new PlanMigrationContext(query);
		this.optimizationContext.put(query.getID(), context);

		try {
			// build alternative physical plans
			getLogger().debug("Building alternative plans.");
			Map<IPhysicalOperator, ILogicalOperator> alternatives = this.queryOptimizer
					.createAlternativePlans(sender, query,
							new OptimizationConfiguration(
									ParameterDoRewrite.TRUE));

			// pick out optimal plan by cost analysis
			List<IPhysicalOperator> candidates = this
					.getExecutionCostModel()
					.getCostCalculator()
					.pickBest(
							alternatives.keySet(),
							this.configuration
									.getSettingComparePlanCandidates()
									.getValue());
			if (candidates.isEmpty()) {
				getLogger().info(
						"No alternative plans for query ID " + query.getID());
				this.optimizationContext.remove(query.getID());
				return executionPlan;
			}

			// calculate migration overhead with every registered strategy
			List<PlanMigration> migrationCandidates = new ArrayList<PlanMigration>();
			for (IPhysicalOperator cPlan : candidates) {
				for (String strategy : this
						.getRegisteredPlanMigrationStrategies()) {
					migrationCandidates
							.add(new PlanMigration(query.getRoots().get(0),
									cPlan, getPlanMigrationStrategy(strategy)));
				}
			}
			// pick near optimal plan with acceptable migration cost
			PlanMigration optimalMigration = this.getMigrationCostModel()
					.getCostCalculator().pickBest(migrationCandidates);
			context.setRoot(optimalMigration.getNewPlan());
			context.setLogicalPlan(alternatives.get(optimalMigration
					.getNewPlan()));
			context.setSender(sender);

			// start migration to new plan
			getLogger().info(
					"Start migration to new physical plan (query ID "
							+ query.getID() + ")");
			ArrayList<IPhysicalOperator> listOfRoots = new ArrayList<IPhysicalOperator>();
			listOfRoots.add(optimalMigration.getNewPlan());
			optimalMigration.getStrategy().migrateQuery(this, query,
					listOfRoots);

			((IExecutor) sender).updateExecutionPlan();

			// wait for migration end callback
			getLogger().info(
					"Plan migration running (query ID " + query.getID() + ")");

		} catch (Exception e) {
			// this.optimizationContext.remove(query.getID());
			getLogger().warn(
					"Reoptimization failed. (query ID " + query.getID() + ")",
					e);
		}

		return executionPlan;
	}

	@Override
	public void handleFinishedMigration(IQuery query) {
		PlanMigrationContext context = this.optimizationContext.get(query
				.getID());

		try {
			// set new logical plan
			query.setLogicalPlan(context.getLogicalPlan());

			// update execution plan
			((IExecutor) context.getSender()).updateExecutionPlan();

			// reinstall metadata listener
			doPostOptimizationActions(query, null);

			// remove lock and context
			this.optimizationContext.remove(query.getID());
			getLogger().info(
					"Finished plan migration (query ID " + query.getID() + ")");

			// handle pending requests
			processPendingRequests();

		} catch (Exception e) {
			// this.optimizationContext.remove(sender.getID());
			getLogger().warn(
					"Reoptimization failed. (query ID " + query.getID() + ")",
					e);
		}
	}

	private void queueRequest(IQuery query, String reason) {
		getLogger().warn("Reoptimization request queued. " + reason);
		if (!this.pendingRequests.contains(query)) {
			this.pendingRequests.offer(query);
		}
	}

	@Override
	public IExecutionPlan reoptimize(IOptimizable sender,
			IExecutionPlan executionPlan) throws QueryOptimizationException {
		for (IQuery query : sender.getQueries()) {
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
