package de.uniol.inf.is.odysseus.condition.conditionql.parser;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.conditionql.registry.ConditionQLParserRegistry;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This parser can parse ConditionQL
 * 
 * @author Tobias Brandt
 *
 */
public class ConditionQLParser implements IQueryParser {

	@Override
	public String getLanguage() {
		return "ConditionQL";
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute) throws QueryParseException {

		ConditionQLQuery conditionQLQuery = ConditionQLParserRegistry.createConditionQLQuery(query);
		IConditionQLParser parser = ConditionQLParserRegistry.getConditionQLParser(conditionQLQuery.getAlgorithm().getAlgorithm());

		ILogicalQuery logicalQuery = parser.parse(user, conditionQLQuery);
		IExecutorCommand exeCommand = new CreateQueryCommand(logicalQuery, user);
		List<IExecutorCommand> exeCommands = new ArrayList<IExecutorCommand>();
		exeCommands.add(exeCommand);
		return exeCommands;
	}

	@Override
	public List<IExecutorCommand> parse(Reader reader, ISession user, IDataDictionary dd, Context context,
			IMetaAttribute metaAttribute) throws QueryParseException {
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
