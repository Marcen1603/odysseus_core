package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public interface IDataAccess {

	public void setCacheQuery(DBQuery dbQuery);
	public void setPrefetchQuery(DBQuery dbQuery);
	public List<RelationalTuple<?>> executeBaseQuery(RelationalTuple<?> tuple);
	public List<RelationalTuple<?>> executeCacheQuery(RelationalTuple<?> tuple);
	public List<RelationalTuple<?>> executePrefetchQuery(RelationalTuple<?> tuple);
	public RelationalTuple<?> getRelevantParams(RelationalTuple<?> tuple);
	public List<String> getOutAttributeSchema(DBQuery dbQuery);
	
}
