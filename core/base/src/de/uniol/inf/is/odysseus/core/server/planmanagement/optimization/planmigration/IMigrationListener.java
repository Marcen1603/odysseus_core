/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration;


/**
 * @author Merlin Wasmann
 *
 */
public interface IMigrationListener {

	public void migrationFinished(IMigrationEventSource sender);
	public void migrationFailed(IMigrationEventSource sender);
}
