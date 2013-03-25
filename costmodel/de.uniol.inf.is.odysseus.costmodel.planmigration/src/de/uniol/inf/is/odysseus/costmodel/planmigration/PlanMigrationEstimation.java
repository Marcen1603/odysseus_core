package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * Represents the estimation for a plan migration.
 * 
 * @author merlin (Nach Vorbild von Timo Michelsen)
 *
 */
public class PlanMigrationEstimation {

	private PlanMigration migration;
	private IPlanMigrationDetailCost detailCost = null;
	
	public PlanMigrationEstimation(PlanMigration migration) {
		if(migration == null) {
			throw new IllegalArgumentException("Planmigration is null");
		}
		
		this.migration = migration;
	}
	
	public IPlanMigrationDetailCost getDetailCost() {
		return this.detailCost;
	}
	
	public void setDetailCost(IPlanMigrationDetailCost detailCost) {
		this.detailCost = detailCost;
	}
	
	public PlanMigration getPlanMigration() {
		return this.migration;
	}
}
