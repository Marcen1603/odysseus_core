package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.noplanmigrationstrategie;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;

/**
 * This is the standard plan migration strategy. No migration is performed. The
 * new execution plan will be used unmodified.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoPlanMigrationStrategie implements IPlanMigrationStrategie {
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie#migratePlan(de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable, de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan)
	 */
	@Override
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender,
			IEditableExecutionPlan newExecutionPlan) {
		return newExecutionPlan;
	}

	@Override
	public void migrateQuery(IOptimizer sender,
			IEditableQuery runningQuery, IPhysicalOperator newPlanRoot) {
	}
}
