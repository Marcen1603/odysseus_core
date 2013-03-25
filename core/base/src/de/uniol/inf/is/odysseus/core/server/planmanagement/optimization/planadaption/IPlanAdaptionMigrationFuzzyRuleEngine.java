/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * 
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionMigrationFuzzyRuleEngine {

	/**
	 * Evaluate if a migration is good or not with the given costs.
	 * 
	 * @param costDifference Cost difference between the oldPlan and the newPlan.
	 * @param migrationCosts Cost of the migration of the oldPlan to the newPlan.
	 * @return
	 */
	public boolean evaluate(ICost<ILogicalOperator> costDifference, ICost<PlanMigration> migrationCosts);
}
