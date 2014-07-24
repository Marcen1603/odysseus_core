package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SportsQLParser implements IQueryParser {

	@Override
	public String getLanguage() {
		return "SportsQL";
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user,
			IDataDictionary dd, Context context) throws QueryParseException {
//		IExecutorCommand exeCommand;
//		ISportsQLParser parser = SportsQLParserRegistry
//				.getSportsQLParser(query);
//		ILogicalQuery logicalQuery = parser.parse(sportsQL);
		return null;
	}

	@Override
	public List<IExecutorCommand> parse(Reader reader, ISession user,
			IDataDictionary dd, Context context) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		// TODO Auto-generated method stub
		return null;
	}

}
