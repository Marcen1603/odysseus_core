package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Listener to be called, after adding a new query.
 * 
 * @author Michael Brand
 * 
 */
public interface IQueryAddedListener {

	/**
	 * The method is called if the executor adds a new query.
	 * {@link IExecutor#addQuery(String, String, ISession, String, de.uniol.inf.is.odysseus.core.collection.Context)}
	 */
	public void queryAddedEvent(String query, List<Integer> queryIds, String buildConfig,
			String parserID, ISession user);
}