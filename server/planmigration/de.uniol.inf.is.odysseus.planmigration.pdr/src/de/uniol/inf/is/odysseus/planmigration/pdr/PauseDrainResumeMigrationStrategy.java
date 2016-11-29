/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.pdr;

import de.uniol.inf.is.odysseus.planmigration.AbstractPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmigration.IPlanMigrationStrategy;

/**
 * @author Dennis Nowak
 *
 */
public class PauseDrainResumeMigrationStrategy extends AbstractPlanMigrationStrategy {
	
	private static final String NAME = "PauseDrainResumeMigrationStrategy";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public IPlanMigrationStrategy getNewInstance() {
		return new PauseDrainResumeMigrationStrategy();
	}

}
