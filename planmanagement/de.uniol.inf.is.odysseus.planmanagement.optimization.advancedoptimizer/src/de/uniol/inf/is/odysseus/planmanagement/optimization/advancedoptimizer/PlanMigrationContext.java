package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;

/**
 * Holds context data for a plan migration. Used by {@link AdvancedOptimizer} to
 * access data on migration finished callback.
 * 
 * @author Tobias Witt
 * 
 */
class PlanMigrationContext {

	private IPhysicalOperator root;
	private ILogicalOperator logicalPlan;
	private IEditableQuery query;

	public PlanMigrationContext(IEditableQuery query) {
		this.query = query;
	}

	IPhysicalOperator getRoot() {
		return root;
	}

	void setRoot(IPhysicalOperator root) {
		this.root = root;
	}

	ILogicalOperator getLogicalPlan() {
		return logicalPlan;
	}

	void setLogicalPlan(ILogicalOperator logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	IEditableQuery getQuery() {
		return query;
	}

	void setQuery(IEditableQuery query) {
		this.query = query;
	}

}
