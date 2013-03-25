package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * Standard implementation of the plan migration detail costs
 * 
 * @author merlin
 *
 */
public class PlanMigrationDetailCost implements IPlanMigrationDetailCost {

	private final PlanMigration migration;
	private final double memCost;
	private final double cpuCost;
	private final long duration;
	
	public PlanMigrationDetailCost(PlanMigration migration, double memCost, double cpuCost, long duration) {
		this.migration = migration;
		this.memCost = memCost;
		this.cpuCost = cpuCost;
		this.duration = duration;
	}
	
	@Override
	public PlanMigration getPlanMigration() {
		return this.migration;
	}

	@Override
	public double getMemoryCost() {
		return this.memCost;
	}

	@Override
	public double getProcessorCost() {
		return this.cpuCost;
	}

	@Override
	public long getDuration() {
		return this.duration;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ mem = ").append(this.memCost).append(", cpu = ").append(this.cpuCost).append(", duration = ").append(this.duration).append(" }");
		return sb.toString();
	}

}
