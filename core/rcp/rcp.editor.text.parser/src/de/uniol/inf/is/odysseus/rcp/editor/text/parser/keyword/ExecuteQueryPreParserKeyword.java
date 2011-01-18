package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class ExecuteQueryPreParserKeyword extends AbstractQueryPreParserKeyword {

	// Query selbst ausführen...
	@Override
	protected Object exec(String parserID, String transCfg, String queryText, User caller, IDataDictionary dd) throws PlanManagementException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		final List<IQueryBuildSetting<?>> cfg = ExecutorHandler.getExecutor().getQueryBuildConfiguration(transCfg);
		return ExecutorHandler.getExecutor().addQuery(queryText, parserID, caller, dd, cfg.toArray(new IQueryBuildSetting[0]) );
	}

}
