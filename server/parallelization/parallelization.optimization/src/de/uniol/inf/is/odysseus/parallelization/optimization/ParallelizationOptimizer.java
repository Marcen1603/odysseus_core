/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.AppendUniqueIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;
import de.uniol.inf.is.odysseus.planmigration.IMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.MigrationStrategyRegistry;
import de.uniol.inf.is.odysseus.planmigration.exception.MigrationException;

/**
 * @author Dennis Nowak
 *
 */
public class ParallelizationOptimizer {

	private static ParallelizationOptimizer instance;

	private static int parallelizationNumber;

	private static final Logger LOG = LoggerFactory.getLogger(ParallelizationOptimizer.class);

	private static final String MIGRATION_STRATEGY = "GeneralizedParallelTracksMigrationStrategy";

	private StandardExecutor executor;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reoptimizeQuery(IPhysicalQuery physicalQuery, ISession caller) {
		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();

		// copy initial logical plan an rename it to avoid duplicate uniqueids
		GenericGraphWalker walker0 = new GenericGraphWalker();
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<>(logicalQuery);
		walker0.prefixWalk(logicalQuery.getInitialLogicalPlan().getRoot(), copyVisitor);
		logicalQuery.setLogicalPlan(new LogicalPlan(copyVisitor.getResult()), false);
		walker0.clearVisited();
		AppendUniqueIdLogicalGraphVisitor<ILogicalOperator> appendVisitor = new AppendUniqueIdLogicalGraphVisitor(
				"_migrate" + parallelizationNumber);
		parallelizationNumber++;
		walker0.prefixWalk(logicalQuery.getLogicalPlan().getRoot(), appendVisitor);

		// remove sinks and make the operator which it is subscribed to the new
		// root
		// only works if the sink is subscribed to only one source
		ILogicalOperator top = logicalQuery.getLogicalPlan().getRoot();
		if (!(top instanceof TopAO)) {
			LOG.error("The top of the plan is not of Type TopAO");
			return;
		}
		if (top.getSubscribedToSource().size() != 1) {
			LOG.error("Top must be subscribed to exactly one operator.");
			return;
		}
		LogicalSubscription topSubscription = top.getSubscribedToSource().iterator().next();
		ILogicalOperator sinkToRemove = topSubscription.getSource();
		if (sinkToRemove.getSubscribedToSource().size() != 1) {
			LOG.error("The sink must be subscribed to  exactly one operator.");
			return;
		}
		if (sinkToRemove instanceof AbstractSenderAO) {
			AbstractSenderAO sink = ((AbstractSenderAO) sinkToRemove);
			Resource oldName = sink.getSinkname();
			Resource newName = new Resource(oldName.getUser(), oldName.getResourceName() + "_temp");
			sink.setSink(newName);
		}
		// LogicalSubscription sinkSubscription =
		// sinkToRemove.getSubscribedToSource().iterator().next();
		// ILogicalOperator newRoot = sinkSubscription.getTarget();
		// top.unsubscribeFromSource(topSubscription);
		// sinkToRemove.unsubscribeFromSource(sinkSubscription);
		// sinkToRemove.removeOwner(logicalQuery);
		// newRoot.subscribeSink(top, 0, 0, top.getOutputSchema());

		// parallelize (change BuildConfiguration and perform preTransformation)
		List<ILogicalQuery> logicalQueryList = new ArrayList<>();
		logicalQueryList.add(logicalQuery);
		executor.executePreTransformationHandlers(caller,
				executor.getBuildConfigForQuery(logicalQuery), logicalQueryList, Context.empty());
		// rewrite
		ParameterDoRewrite restruct = (new OptimizationConfiguration(
				executor.getBuildConfigForQuery(logicalQuery))).getParameterDoRewrite();
		if (restruct != null && restruct == ParameterDoRewrite.TRUE) {
			RewriteConfiguration rewriteConfig = (new OptimizationConfiguration(
					executor.getBuildConfigForQuery(logicalQuery))).getRewriteConfiguration();
			rewriteConfig
					.setQueryBuildConfiguration(executor.getBuildConfigForQuery(logicalQuery));
			logicalQuery
					.setLogicalPlan(
							executor.getCompiler().rewritePlan(logicalQuery.getLogicalPlan(),
									rewriteConfig, caller, executor.getDataDictionary(caller)),
							false);
			LOG.debug("Plan rewritten");
		} else {
			LOG.debug("Rewritung of plan disabled.");
		}
		// transform
		IPhysicalQuery newPhysicalQuery = executor.transform(logicalQuery,
				executor.getBuildConfigForQuery(logicalQuery).getTransformationConfiguration(),
				caller);
		// load migration strategy
		IMigrationStrategy planMigrationStrategy = null;
		try {
			planMigrationStrategy = MigrationStrategyRegistry.getPlanMigrationStrategyById(MIGRATION_STRATEGY);
		} catch (MigrationException e) {
			// TODO Auto-generated catch block
			LOG.error("Migration strategy \"" + MIGRATION_STRATEGY + "\" could not be loaded.", e);
			return;
		}
		// replace physical plan
		planMigrationStrategy.getNewInstance().migrateQuery(physicalQuery, newPhysicalQuery.getRoots());
		for (IPhysicalOperator op : newPhysicalQuery.getPhysicalChilds()) {
			op.removeOwner(newPhysicalQuery);
		}
		// physicaloperators get logicaloperators
	}

	public static ParallelizationOptimizer getInstance() {
		if (instance == null) {
			instance = new ParallelizationOptimizer();
		}
		return instance;
	}

	public void reoptimizeQuery(String queryName, ISession caller) {
		Resource queryResource = new Resource(caller.getUser(), queryName);
		IPhysicalQuery query = executor.getExecutionPlan(caller).getQueryByName(queryResource,
				caller);
		this.reoptimizeQuery(query, caller);
	}

	public void reoptimizeQuery(int queryId, ISession caller) {
		IPhysicalQuery query = executor.getExecutionPlan(caller).getQueryById(queryId, caller);
		this.reoptimizeQuery(query, caller);
	}
	
	public void setExecutor(IExecutor executor) {
		this.executor = (StandardExecutor) executor;
	}

}
