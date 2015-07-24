package de.uniol.inf.is.odysseus.iql.qdl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public class QDLQueryParser implements IQueryParser{

	@Override
	public String getLanguage() {
		return "QDL";
	}


	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		List<String> tokensList = new ArrayList<String>();
		tokens.put("TOKEN", tokensList);
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}


	@Override
	public List<IExecutorCommand> parse(String query, ISession user,
			IDataDictionary dd, Context context, IMetaAttribute metaAttribute,
			IServerExecutor executor) throws QueryParseException {
//		List<IExecutorCommand> commands = new ArrayList<IExecutorCommand>();		
//		Collection<ILogicalQuery> queries = QDLParser.parse(query);
//		for (ILogicalQuery logQuery : queries) {
//			CreateQueryCommand exeCommand = new CreateQueryCommand(logQuery,user);
//			commands.add(exeCommand);
//		}
//		return commands;
		
//		Injector injector = new QDLStandaloneSetup().createInjectorAndDoEMFRegistration();
//		IIQLParser parser = injector.getInstance(IIQLParser.class);
//		return parser.parse(query, dd, user, context);
		return null;
	}


}
