/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.gpt;

import de.uniol.inf.is.odysseus.planmigration.AbstractPlanMigrationStrategy;

/**
 * @author Dennis Nowak
 *
 */
public class GeneralizedParallelTracksMigrationStrategy extends AbstractPlanMigrationStrategy {
	
	private static final String NAME = "GeneralizedParallelTracksMigrationStrategy";

	@Override
	public String getName() {
		return NAME;
	}

}
