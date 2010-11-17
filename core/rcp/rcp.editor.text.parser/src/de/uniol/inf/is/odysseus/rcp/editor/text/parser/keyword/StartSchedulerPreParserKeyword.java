package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

public class StartSchedulerPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
	}

	@Override
	public void execute(Map<String, String> variables, String parameter)
			throws QueryTextParseException {
		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		try {
			executor.startExecution();
		} catch (PlanManagementException e) {
			throw new QueryTextParseException(e);
		}

	}

}
