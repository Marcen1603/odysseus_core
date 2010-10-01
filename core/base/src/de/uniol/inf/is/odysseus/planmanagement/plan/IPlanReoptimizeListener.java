package de.uniol.inf.is.odysseus.planmanagement.plan;

/**
 * IQueryReoptimizeListener describes an object which processes reoptimization
 * request which are send by a global plan.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanReoptimizeListener {
	/**
	 * Send a reoptimize request for a global plan.
	 * 
	 * @param sender
	 *            The global plan which sends the reoptimze request.
	 */
	public void reoptimizeRequest(IPlan sender);
}
