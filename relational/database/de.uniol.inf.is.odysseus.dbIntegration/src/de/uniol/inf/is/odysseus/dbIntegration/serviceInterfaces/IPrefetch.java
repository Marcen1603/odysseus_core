package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.PrefetchException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface IPrefetch {
	public List<RelationalTuple<?>> getCachedData(RelationalTuple<?> streamParam, DBQuery dbQuery) throws PrefetchException;
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs, IDataAccess dal); 
	public void closeQuery(DBQuery dbQuery);
}
