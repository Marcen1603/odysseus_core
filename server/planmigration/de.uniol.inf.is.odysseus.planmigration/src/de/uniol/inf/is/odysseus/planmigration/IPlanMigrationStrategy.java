/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration;

/**
 * @author Dennis Nowak
 *
 */
public interface IPlanMigrationStrategy {
	
	public String getName();
	
	public IPlanMigrationStrategy getNewInstance();

}
