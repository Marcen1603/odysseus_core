package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.ActiveUser;

public class ExecuteQueryPreParserKeyword extends AbstractQueryPreParserKeyword {

	// Query selbst ausführen...
	protected Object exec(String parserID, String transCfg, String queryText) throws PlanManagementException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		User user = ActiveUser.getActiveUser();
		final List<IQueryBuildSetting<?>> cfg = ExecutorHandler.getExecutor().getQueryBuildConfiguration(transCfg);
		return ExecutorHandler.getExecutor().addQuery(queryText, parserID, user, cfg.toArray(new IQueryBuildSetting[0]) );
	}

}
