/**
 * 
 */
package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * @author merlin
 *
 */
public interface IPlanMigrationDetailCost {

	public PlanMigration getPlanMigration();
	public double getMemoryCost();
	public double getProcessorCost();
	public long getDuration();
}
