package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.noplanmigrationstrategie;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * This is the standard plan migration strategy. No migration is performed. The
 * new execution plan will be used unmodified.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoPlanMigrationStrategy implements IPlanMigrationStrategy {
	
	
	@Override
	public String getName() {
		return "No Migration Strategy";
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie#migratePlan(de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan migratePlan(IPlanMigratable sender,
			IExecutionPlan newExecutionPlan) {
		return newExecutionPlan;
	}

	@Override
	public void migrateQuery(IOptimizer sender,
			IQuery runningQuery, List<IPhysicalOperator> newPlanRoots) {
	}
}
