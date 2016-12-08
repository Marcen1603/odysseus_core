/**
 * 
 */
package de.uniol.inf.is.odysseus.planmigration.exception;

/**
 * @author Dennis Nowak
 *
 */
public class MigrationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6796529542743062647L;
	
	public MigrationException(String msg) {
		super(msg);
	}
	
	public MigrationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
