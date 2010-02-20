package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.Datarate;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanExecutionCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * 
 * @author Tobias Witt
 *
 */
public class AdvancedOptimizer extends AbstractOptimizer {
	
	private static final int MAX_CONCURRENT_OPTIMIZATIONS = 2;
	private static final int NUM_COMPARE_PLANCANDIDATES = 5;
	
	private IAdvancedExecutor executor;
	private IPlanExecutionCostModel executionCostModel;
	private IPlanMigrationCostModel migrationCostModel;
	private List<IPlanMigrationStrategie> planMigrationStrategies;
	
	private Map<Integer, PlanMigrationContext> optimizationContext;
	private List<IMonitoringData<?>> sourceDatarates;
	
	public AdvancedOptimizer() {
		this.optimizationContext = new HashMap<Integer, PlanMigrationContext>();
		this.sourceDatarates = new ArrayList<IMonitoringData<?>>();
		this.planMigrationStrategies = new ArrayList<IPlanMigrationStrategie>();
	}
	
	// FIXME: zyklische abhaengigkeit
	// <reference bind="bindExecutor" cardinality="1..1" interface="de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor" name="IAdvancedExecutor" policy="dynamic"/>
	// reoptimize bietet kein IQueryOptimizable bzw. executor, daher kommt IQueryOptimizer nicht an den compiler
	public void bindExecutor(IAdvancedExecutor executor){
		this.executor = executor;
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
	
	@Override
	public void bindPlanMigrationStrategie(IPlanMigrationStrategie planMigrationStrategie) {
		this.planMigrationStrategies.add(planMigrationStrategie);
	}
	
	@Override
	public void unbindPlanMigrationStrategie(IPlanMigrationStrategie planMigrationStrategie) {
		this.planMigrationStrategies.remove(planMigrationStrategie);
	}

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> queries, OptimizeParameter parameter)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IEditableQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter);
				updateMetadataListener(editableQuery);
			}

			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
		}
		return sender.getEditableExecutionPlan();
	}

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> queries, OptimizeParameter parameter, Set<String> rulesToUse)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IEditableQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter, rulesToUse);
				updateMetadataListener(editableQuery);
			}

			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
			
		}
		return sender.getEditableExecutionPlan();
	}

	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan, OptimizeParameter parameter)
			throws QueryOptimizationException {
		ArrayList<IEditableQuery> newPlan = new ArrayList<IEditableQuery>(
				sender.getRegisteredQueries());
		newPlan.remove(removedQuery);

		IEditableExecutionPlan newExecutionPlan = this.planOptimizer
				.optimizePlan(sender, parameter, newPlan);

		return this.planMigrationStrategie
				.migratePlan(sender, newExecutionPlan);
	}
	
	private void updateMetadataListener(IEditableQuery editableQuery) {
		AbstractTreeWalker.prefixWalk2(editableQuery.getRoot(), new InstallMetadataListenerVisitor(this));
	}
	
	@Override
	public IExecutionPlan reoptimize(IEditableQuery sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		this.logger.info("Start reoptimize query ID "+sender.getID());
		
		// optimization lock on query
		if (this.optimizationContext.containsKey(sender.getID())) {
			this.logger.warn("Aborted reoptimization. Query with ID "+sender.getID()+" is currently getting optimized.");
			return executionPlan;
		} else if (this.optimizationContext.size() >= MAX_CONCURRENT_OPTIMIZATIONS) {
			// TODO: evtl. spaeters triggern der Optimierungsanforderung
			this.logger.warn("Aborted reoptimization. There are currently "+this.optimizationContext.size()+" optimizations running.");
			return executionPlan;
		}
		PlanMigrationContext context = new PlanMigrationContext(sender);
		this.optimizationContext.put(sender.getID(), context);
		
		try {
			// build alternative physical plans
			Map<IPhysicalOperator,ILogicalOperator> alternatives = this.queryOptimizer.createAlternativePlans(
					(IQueryOptimizable)this.executor, sender, 
					new OptimizeParameter(ParameterDoRestruct.TRUE), null);
			
			// prepare metadata usage
			for (IMonitoringData<?> rates : this.sourceDatarates) {
				((Datarate)rates).run();
			}
			
			// pick out optimal plan by cost analysis
			List<IPhysicalOperator> candidates = this.executionCostModel.getCostCalculator().pickBest(
					alternatives.keySet(), NUM_COMPARE_PLANCANDIDATES);
			
			// calculate migration overhead with every registered strategy
			List<PlanMigration> migrationCandidates = new ArrayList<PlanMigration>();
			for (IPhysicalOperator cPlan : candidates) {
				for (IPlanMigrationStrategie strategy : this.planMigrationStrategies) {
					migrationCandidates.add(new PlanMigration(sender.getRoot(), cPlan, strategy));
				}
			}
			// pick near optimal plan with acceptable migration cost
			PlanMigration optimalMigration = this.migrationCostModel.getCostCalculator().pickBest(migrationCandidates);
			IPhysicalOperator newPlan = optimalMigration.getNewPlan();
			context.setRoot(newPlan);
			context.setLogicalPlan(alternatives.get(newPlan));
			
			// TODO: possibly need to drain buffers and remove them
			// stop scheduling
			sender.stop();
			
			// start migration to new plan 
			this.logger.info("Start migration to new physical plan (query ID "+sender.getID()+")");
			optimalMigration.getStrategy().migrateQuery(this, sender, newPlan);
			
			// wait for migration end callback
			this.logger.info("Plan migration running (query ID "+sender.getID()+")");
			
		} catch (Exception e) {
			//this.optimizationContext.remove(sender.getID());
			this.logger.warn("Reoptimization failed. (query ID "+sender.getID()+")",e);
		}
		
		return executionPlan;
	}

	@Override
	public void handleFinishedMigration(IEditableQuery query) {
		PlanMigrationContext context = this.optimizationContext.get(query.getID());

		try {
			// set new logical plan
			query.setLogicalPlan(context.getLogicalPlan());

			// reapply buffers to plan
			IBufferPlacementStrategy bufferPlacementStrategy = query
					.getBuildParameter().getBufferPlacementStrategy();
			if (bufferPlacementStrategy != null) {
				bufferPlacementStrategy.addBuffers(context.getRoot());
			}

			// set and initialize new physical plan
			query.initializePhysicalPlan(context.getRoot());

			// continue scheduling
			query.start();

			// remove lock and context
			this.optimizationContext.remove(query.getID());
			this.logger.info("Finished plan migration (query ID "
					+ query.getID() + ")");

		} catch (Exception e) {
			// this.optimizationContext.remove(sender.getID());
			this.logger.warn("Reoptimization failed. (query ID "
					+ query.getID() + ")", e);
		}
	}

	List<IMonitoringData<?>> getSourceDatarates() {
		return sourceDatarates;
	}
	
	

}
