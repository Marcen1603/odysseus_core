package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

/**
 * A listener interface for recovery pre processors.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryPreprocessorListener {

	/**
	 * A recovery strategy has been set for given query parts.
	 * 
	 * @param name
	 *            The name of the recovery strategy.
	 * @param query
	 *            The given query.
	 */
	public void setRecoveryStrategy(String name, Collection<ILogicalQueryPart> queryParts);

}