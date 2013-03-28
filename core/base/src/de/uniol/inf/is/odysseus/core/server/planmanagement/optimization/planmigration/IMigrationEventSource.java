/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


/**
 * @author Merlin Wasmann
 *
 */
public interface IMigrationEventSource {
	
	public IPhysicalQuery getPhysicalQuery();
	
	public boolean hasPhysicalQuery();

	public void addMigrationListener(IMigrationListener listener);
	
	public void removeMigrationListener(IMigrationListener listener);
	
	public void fireMigrationFinishedEvent(IMigrationEventSource sender);
	
	public void fireMigrationFailedEvent(IMigrationEventSource sender, Throwable ex);
}
