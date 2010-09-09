package de.uniol.inf.is.odysseus.base.planmanagement.query;

/**
 * IQueryReoptimizeListener describes an object which processes reoptimization
 * request which are send by a query.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IQueryReoptimizeListener {
	/**
	 * Send a reoptimize request for a query.
	 * 
	 * @param sender
	 *            The query which sends the reoptimze request.
	 */
	public void reoptimize(IQuery sender);
}
