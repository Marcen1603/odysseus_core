package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;


/**
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanMigrationDurationEstimator {

	public long estimateDuration(PlanMigration migration);
}
