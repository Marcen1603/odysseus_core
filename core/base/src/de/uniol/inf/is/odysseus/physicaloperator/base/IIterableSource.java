package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;

/**
 * An iterator interface for plain sources or buffers.
 * While {@link #hasNext()} may block until data is available
 * for plain sources,
 * it is not allowed to block for buffers.
 * @author Jonas Jacobi
 */
public interface IIterableSource<T> extends ISource<T> {
	/**
	 * Get whether a call to transferNext() will be successful.
	 * @return true, if an element can be transfered. 
	 */
	public boolean hasNext();
	/**
	 * Call {@link #transfer(Object)} with the next element. May only be called
	 * if a call to hasNext() returns true.
	 */
	public void transferNext();
	
	/**
	 * Returns true, if ISource has all Input processed
	 */
	public boolean isDone();
	
	/**
	 * Activates this operator if it could be done.
	 * 
	 * @param operatorControl
	 *            Control which wants to activate this operator.
	 */
	public void activateRequest(IOperatorOwner operatorControl);

	/**
	 * Deactivates this operator if it could be done (e. g. no other query needs
	 * this one).
	 * 
	 * @param operatorControl
	 *            Control which wants to activate this operator.
	 */
	public void deactivateRequest(IOperatorOwner operatorControl);

	/**
	 * Checks if an control currently requests a deactivation of this operator.
	 * 
	 * @param operatorControl
	 *            Control for the check.
	 * @return TRUE: Control currently requests a deactivation of this operator.
	 *         FALSE: else
	 */
	public boolean deactivateRequestedBy(IOperatorOwner operatorControl);

	/**
	 * Indicates if this operator is currently active.
	 * 
	 * @return TRUE: operator is currently active. FALSE: else
	 */
	public boolean isActive();
	
	/**
	 * Indicates if the operator is currently blocked (i.e. does not produce elements)
	 * @return
	 */
	public boolean isBlocked();

	/**
	 * Block operator
	 */
	public void block();
	
	/**
	 * unblock operator
	 */
	public void unblock();
}

