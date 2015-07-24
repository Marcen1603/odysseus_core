package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public interface IQDLQuery {
	
	public Collection<ILogicalOperator> execute();
	
	public Map<String, List<Object>> getMetadata();
	
	public void addMetadata(String key, Object value);

	public void setDataDictionary(IDataDictionary dd);

	public void setSession(ISession session);

}
