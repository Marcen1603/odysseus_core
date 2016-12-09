/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.AppendUniqueIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reoptimzeQuery(int queryId, ISession user) {
		// change logicalPlan
		IPhysicalQuery physicalQuery = StandardExecutor.getInstance().getExecutionPlan(user).getQueryById(queryId,
				user);

		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
		AppendUniqueIdLogicalGraphVisitor<ILogicalOperator> appendVisitor = new AppendUniqueIdLogicalGraphVisitor(
				"_migrate");
		GenericGraphWalker walker0 = new GenericGraphWalker();
		walker0.prefixWalk(logicalQuery.getInitialLogicalPlan(), appendVisitor);

		// parallelize (change BuildConfiguration and perform preTransformation)
		logicalQuery.setLogicalPlan(logicalQuery.getInitialLogicalPlan(), false);
		List<ILogicalQuery> logicalQueryList = new ArrayList<>();
		logicalQueryList.add(logicalQuery);
		StandardExecutor.getInstance().executePreTransformationHandlers(user,
				StandardExecutor.getInstance().getBuildConfigForQuery(logicalQuery), logicalQueryList, Context.empty());
		// rewrite

		// transform
		IPhysicalQuery newPhysicalQuery = StandardExecutor.getInstance().transform(logicalQuery,
				StandardExecutor.getInstance().getBuildConfigForQuery(logicalQuery).getTransformationConfiguration(),
				user);
		// TODO select migration strategy
		String migrationStrategy = "GeneralizedParallelTracksMigrationStrategy";
		IMigrationStrategy planMigrationStrategy = null;
		try {
			planMigrationStrategy = MigrationStrategyRegistry.getPlanMigrationStrategyById(migrationStrategy);
		} catch (MigrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		// replace physical plan
		planMigrationStrategy.getNewInstance().migrateQuery(physicalQuery, newPhysicalQuery.getRoots());
		// update logical plan
		
		// physicaloperators get logicaloperators
	}

	public static ParallelizationOptimizer getInstance() {
		if (instance == null) {
			instance = new ParallelizationOptimizer();
		}
		return instance;
	}

}
