package de.uniol.inf.is.odysseus.iql.basic.parser;

import java.util.List;

import org.eclipse.core.resources.IProject;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLFile;



public interface IIQLParser {
	public List<IExecutorCommand> parse(String text,IDataDictionary dd, ISession session, Context context) throws QueryParseException;
	
	public void parse(IQLFile file, IProject project);

}
