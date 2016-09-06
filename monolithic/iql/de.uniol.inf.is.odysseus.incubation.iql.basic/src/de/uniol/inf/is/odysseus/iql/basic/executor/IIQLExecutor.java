package de.uniol.inf.is.odysseus.iql.basic.executor;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;



public interface IIQLExecutor {
	public List<IExecutorCommand> parse(String text,IDataDictionary dd, ISession session, Context context) throws QueryParseException;
	
}
