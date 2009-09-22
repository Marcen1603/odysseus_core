package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes an object which could control the execution state operators (e. g. a query).
 * 
 * @author Wolf Bauer
 *
 */
public interface IOperatorControl extends IOperatorOwner {
	/**
	 * Start scheduling of child operators.
	 */
	public void start();
	
	/**
	 * Stop scheduling of child operators.
	 */
	public void stop();
	
	/**
	 * Indicates if the child operators are scheduled.
	 * 
	 * @return TRUE: The child operators are scheduled. FALSE: else
	 */
	public boolean isStarted();
}
