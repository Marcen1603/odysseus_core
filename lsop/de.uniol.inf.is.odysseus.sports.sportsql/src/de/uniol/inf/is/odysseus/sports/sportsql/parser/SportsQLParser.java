package de.uniol.inf.is.odysseus.sports.sportsql.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.sports.sportsql.registry.SportsQLParserRegistry;

/**
 * This parser can parse SportsQL. 
 * E.g. { "statisticType": "global", "gameType": "soccer", "name": "gameTime" }
 * @author Tobias
 *
 */
public class SportsQLParser implements IQueryParser {

	@Override
	public String getLanguage() {
		return "SportsQL";
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user,
			IDataDictionary dd, Context context) throws QueryParseException {
		SportsQLQuery sportQLQuery = SportsQLParserRegistry
				.createSportsQLQuery(query);
		ISportsQLParser parser = SportsQLParserRegistry
				.getSportsQLParser(sportQLQuery);
		try {
			ILogicalQuery logicalQuery = parser.parse(user,sportQLQuery);
			IExecutorCommand exeCommand = new CreateQueryCommand(logicalQuery,user);
			List<IExecutorCommand> exeCommands = new ArrayList<IExecutorCommand>();
			exeCommands.add(exeCommand);
			return exeCommands;
		} catch (SportsQLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingDDCEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Map<String, List<String>> tokens = new HashMap<>();		
		return tokens;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		return new ArrayList<>();
	}

}
