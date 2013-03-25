/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionResourceMonitor {

	public ICost<PlanMigration> getPlanMigrationCost(PlanMigration migration);

	public ICost<IPhysicalOperator> getPlanExecutionCost(IPhysicalQuery query);
}
