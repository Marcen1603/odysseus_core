package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;

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
	private IPlanMigrationStrategy strategy;
	private IOptimizable sender;

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

	void setStrategie(IPlanMigrationStrategy strategy) {
		this.strategy = strategy;
	}

	IPlanMigrationStrategy getStrategy() {
		return strategy;
	}

	public void setSender(IOptimizable sender) {
		this.sender = sender;
	}

	public IOptimizable getSender() {
		return sender;
	}

}
