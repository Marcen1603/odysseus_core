/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * @author Dennis Nowak
 *
 */
public interface IPlanMigrationStrategy {
	
	public String getName();
	
	public IPlanMigrationStrategy getNewInstance();
	
	public void migrateQuery(IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoot);

}
