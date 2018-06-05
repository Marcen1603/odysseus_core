package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.Operator;


public interface IQDLQuery {
	
	public Collection<Operator> execute();
	
	public List<IPair<String, Object>> getMetadata();
	
	public void setExecutor(IQDLQueryExecutor executor);
	
	public String getName();

}
