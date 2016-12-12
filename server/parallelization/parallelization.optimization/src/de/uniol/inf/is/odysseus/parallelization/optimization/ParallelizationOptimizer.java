/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.AppendUniqueIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
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

	private static int parallelizationNumber;

	private static final Logger LOG = LoggerFactory.getLogger(ParallelizationOptimizer.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reoptimzeQuery(int queryId, ISession user) {
		IPhysicalQuery physicalQuery = StandardExecutor.getInstance().getExecutionPlan(user).getQueryById(queryId,
				user);
		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();

		// copy initial logical plan an rename it to avoid duplicate uniqu ids
		GenericGraphWalker walker0 = new GenericGraphWalker();
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<>(logicalQuery);
		walker0.prefixWalk(logicalQuery.getInitialLogicalPlan(), copyVisitor);
		logicalQuery.setLogicalPlan(copyVisitor.getResult(), false);
		walker0.clearVisited();
		AppendUniqueIdLogicalGraphVisitor<ILogicalOperator> appendVisitor = new AppendUniqueIdLogicalGraphVisitor(
				"_migrate" + parallelizationNumber);
		parallelizationNumber++;
		walker0.prefixWalk(logicalQuery.getLogicalPlan(), appendVisitor);

		// parallelize (change BuildConfiguration and perform preTransformation)
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
			LOG.error("Migration strategy \"" + migrationStrategy + "\" could not be loaded.", e);
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

}
