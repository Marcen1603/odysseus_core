/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.gpt;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.exception.MigrationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.StandardExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy.SimplePlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.AbstractPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.IPlanMigrationStrategy;

/**
 * @author Dennis Nowak
 *
 */
public class GeneralizedParallelTracksMigrationStrategy extends AbstractPlanMigrationStrategy {
	
	private static final String NAME = "GeneralizedParallelTracksMigrationStrategy";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IPlanMigrationStrategy getNewInstance() {
		return new GeneralizedParallelTracksMigrationStrategy();
	}
	
	@Override
	public void migrateQuery(IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoot) {
		SimplePlanMigrationStrategy strategy = new SimplePlanMigrationStrategy();
		try {
			strategy.migrateQuery(StandardExecutor.getInstance(), runningQuery, newPlanRoot);
		} catch (QueryOptimizationException | MigrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
