package de.uniol.inf.is.odysseus.peer.recovery;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

/**
 * A listener interface for recovery pre processors.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryPreprocessorListener {

	/**
	 * A recovery strategy has been set for given query.
	 * 
	 * @param name
	 *            The name of the recovery strategy.
	 * @param query
	 *            The given query.
	 */
	public void setRecoveryStrategy(String name, ILogicalQuery query);

}