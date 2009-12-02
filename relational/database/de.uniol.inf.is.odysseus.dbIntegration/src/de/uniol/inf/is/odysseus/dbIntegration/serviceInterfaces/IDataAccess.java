package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface IDataAccess {

	public void setCacheQuery(DBQuery dbQuery);
	public void setPrefetchQuery(DBQuery dbQuery);
	public DBResult executeCacheQuery(RelationalTuple tuple);
	public DBResult executePrefetchQuery(RelationalTuple tuple);
	
}
