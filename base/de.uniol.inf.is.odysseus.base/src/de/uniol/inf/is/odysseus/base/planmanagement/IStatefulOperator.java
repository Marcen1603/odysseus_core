package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes an operator which has a scheduling state. Such an opertaor can be
 * active or inactive. An inactive operator will not be scheduled.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IStatefulOperator {
	/**
	 * Activates this operator if it could be done.
	 * 
	 * @param operatorControl
	 *            Control which wants to activate this operator.
	 */
	public void activateRequest(IOperatorControl operatorControl);

	/**
	 * Deactivates this operator if it could be done (e. g. no other query needs
	 * this one).
	 * 
	 * @param operatorControl
	 *            Control which wants to activate this operator.
	 */
	public void deactivateRequest(IOperatorControl operatorControl);

	/**
	 * Checks if an control currently requests a deactivation of this operator.
	 * 
	 * @param operatorControl
	 *            Control for the check.
	 * @return TRUE: Control currently requests a deactivation of this operator.
	 *         FALSE: else
	 */
	public boolean deactivateRequestedBy(IOperatorControl operatorControl);

	/**
	 * Indicates if this operator is currently active.
	 * 
	 * @return TRUE: operator is currently active. FALSE: else
	 */
	public boolean isActive();
}
