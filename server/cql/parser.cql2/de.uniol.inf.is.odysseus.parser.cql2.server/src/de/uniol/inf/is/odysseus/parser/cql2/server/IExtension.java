package de.uniol.inf.is.odysseus.parser.cql2.server;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Command;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query;

public interface IExtension 
{
	public Object parse(Query command);
	public Object parse(Command command);
	
	public void setUser(ISession user);
	public void setDataDictionary(IDataDictionary dd);
	public void setCommands(List<IExecutorCommand> commands);
	public void setMetaAttribute(IMetaAttribute metaAttribute);
	public String getName();
}
