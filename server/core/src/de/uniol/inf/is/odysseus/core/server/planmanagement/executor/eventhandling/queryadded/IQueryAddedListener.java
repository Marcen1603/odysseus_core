package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
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
	 */
	public void queryAddedEvent(String query, List<Integer> queryIds,
			QueryBuildConfiguration buildConfig, String parserID,
			ISession user, Context context);
}