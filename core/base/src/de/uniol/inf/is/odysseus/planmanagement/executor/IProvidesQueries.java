package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public interface IProvidesQueries {
	/**
	 * Returns all currently registered queries in the system.
	 * 
	 * @return all currently registered queries in the system.
	 */
	public List<IQuery> getQueries();
}
