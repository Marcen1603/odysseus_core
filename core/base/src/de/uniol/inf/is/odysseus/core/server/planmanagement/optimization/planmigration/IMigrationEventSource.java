/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration;


/**
 * @author Merlin Wasmann
 *
 */
public interface IMigrationEventSource {

	public void addMigrationListener(IMigrationListener listener);
	
	public void removeMigrationListener(IMigrationListener listener);
	
	public void fireMigrationFinishedEvent(IMigrationEventSource sender);
	
	public void fireMigrationFailedEvent(IMigrationEventSource sender);
}
