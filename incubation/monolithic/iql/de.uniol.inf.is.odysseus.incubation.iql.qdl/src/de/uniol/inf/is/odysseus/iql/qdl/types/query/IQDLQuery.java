package de.uniol.inf.is.odysseus.iql.qdl.types.query;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;


public interface IQDLQuery {
	
	public Collection<IQDLOperator> execute();
	
	public List<IPair<String, Object>> getMetadata();
	
	public void addMetadata(String key, Object value);

	public void setDataDictionary(IDataDictionary dd);

	public void setSession(ISession session);

}
