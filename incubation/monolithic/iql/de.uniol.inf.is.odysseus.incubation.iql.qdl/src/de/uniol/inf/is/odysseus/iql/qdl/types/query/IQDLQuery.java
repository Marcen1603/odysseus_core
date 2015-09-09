package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;


public interface IQDLQuery {
	
	public Collection<IQDLOperator> execute();
	
	public List<IPair<String, Object>> getMetadata();
	
	public void setExecutor(IQDLQueryExecutor executor);
	
	public String getName();

}
