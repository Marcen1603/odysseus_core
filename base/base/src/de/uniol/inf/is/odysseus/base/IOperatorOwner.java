package de.uniol.inf.is.odysseus.base;

/**
 * Describes an object which could own an operator.
 * 
 * @author Wolf Bauer
 */
public interface IOperatorOwner {
	/**
	 * ID which identifies an owner. This ID should be unique.
	 * 
	 * @return ID which identifies an owner. This ID should be unique.
	 */
	public int getID();
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
	public boolean isRunning();
}
