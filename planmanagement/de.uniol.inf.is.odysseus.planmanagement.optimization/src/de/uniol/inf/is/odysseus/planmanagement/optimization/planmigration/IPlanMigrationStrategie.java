package de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;

public interface IPlanMigrationStrategie {
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender, IEditableExecutionPlan newExecutionPlan);
}
