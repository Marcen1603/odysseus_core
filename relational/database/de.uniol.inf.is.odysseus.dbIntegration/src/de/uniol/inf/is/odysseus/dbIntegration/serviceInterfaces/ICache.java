package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface ICache {

	public DBResult getCachedData(RelationalTuple<?> streamParam, DBQuery dbQuery) throws CacheMissException;
	public void addData(DBResult result, DBQuery dbQuery);
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs, IDataAccess dal); 
	public void closeQuery(DBQuery dbQuery);
	
}
