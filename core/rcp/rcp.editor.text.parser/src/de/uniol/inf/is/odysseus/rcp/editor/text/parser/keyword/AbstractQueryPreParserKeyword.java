package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

public abstract class AbstractQueryPreParserKeyword implements
		IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		try {
			IExecutor executor = ExecutorHandler.getExecutor();
			if (executor == null)
				throw new QueryTextParseException("No executor found");

			if (parameter.length() == 0)
				throw new QueryTextParseException("Encountered empty query");

			String parserID = variables.get("PARSER");
			if (parserID == null)
				throw new QueryTextParseException("Parser not set");
			if (!executor.getSupportedQueryParsers().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = variables.get("TRANSCFG");
			if (transCfg == null)
				throw new QueryTextParseException("TransformationConfiguration not set");
			if (executor.getQueryBuildConfiguration(transCfg) == null)
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not found");
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown Exception during validation a query", ex);
		}
	}

	@Override
	public Object execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		String parserID = variables.get("PARSER");
		String transCfg = variables.get("TRANSCFG");

		try {
			return exec(parserID, transCfg, parameter);
		} catch (Exception ex) {
			throw new QueryTextParseException("Error during executing query", ex);
		}
	}

	protected abstract Object exec(String parserID, String transCfg, String parameter) throws PlanManagementException;



}
