package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.noplanmigrationstrategie;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;

public class NoPlanMigrationStrategie implements IPlanMigrationStrategie {

	@Override
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender,
			IEditableExecutionPlan newExecutionPlan) {
		return newExecutionPlan;
	}
}
