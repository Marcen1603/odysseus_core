package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class QueryPreParserKeyword implements IPreParserKeyword {

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
	public void execute(Map<String, String> variables, String parameter) throws QueryTextParseException {
		String parserID = variables.get("PARSER");
		String transCfg = variables.get("TRANSCFG");

		try {
			executeQuery(parserID, transCfg, parameter);
		} catch (Exception ex) {
			throw new QueryTextParseException("Error during executing query", ex);
		}
	}

	// Query selbst ausführen...
	private void executeQuery(String parserID, String transCfg, String queryText) throws PlanManagementException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		User user = ActiveUser.getActiveUser();
		final List<IQueryBuildSetting<?>> cfg = ExecutorHandler.getExecutor().getQueryBuildConfiguration(transCfg);
		ExecutorHandler.getExecutor().addQuery(queryText, parserID, user, cfg.toArray(new IQueryBuildSetting[0]) );
	}

}
