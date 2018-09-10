/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.gpt;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmigration.AbstractPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.IMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.exception.MigrationException;
import de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy.SimplePlanMigrationStrategy;

/**
 * @author Dennis Nowak
 *
 */
public class GeneralizedParallelTracksMigrationStrategy extends AbstractPlanMigrationStrategy {

	private static Logger LOG = LoggerFactory.getLogger(GeneralizedParallelTracksMigrationStrategy.class);

	private static final String NAME = "GeneralizedParallelTracksMigrationStrategy";

	private IServerExecutor executor;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IMigrationStrategy getNewInstance() {
		return new GeneralizedParallelTracksMigrationStrategy();
	}

	@Override
	public void migrateQuery(IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoot) {
		SimplePlanMigrationStrategy strategy = new SimplePlanMigrationStrategy();
		try {
			strategy.migrateQuery(executor, runningQuery, newPlanRoot);
		} catch (QueryOptimizationException | MigrationException e) {
			LOG.error("Migration has not been completed.", e);
		}
	}

	public void setExecutor(IExecutor executor) {
		this.executor = (IServerExecutor) executor;
	}

}
