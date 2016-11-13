/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.exception;

/**
 * @author Dennis Nowak
 *
 */
public class PlanMigrationStrategyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6796529542743062647L;
	
	public PlanMigrationStrategyException(String msg) {
		super(msg);
	}
	
	public PlanMigrationStrategyException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
