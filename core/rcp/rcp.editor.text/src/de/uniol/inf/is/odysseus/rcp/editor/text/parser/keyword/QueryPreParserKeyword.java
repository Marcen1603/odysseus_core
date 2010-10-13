package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryBuildConfigurationRegistry;

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
			if (!executor.getSupportedQueryParser().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = variables.get("TRANSCFG");
			if (transCfg == null)
				throw new QueryTextParseException("TransformationConfiguration not set");
			if (QueryBuildConfigurationRegistry.getInstance().getQueryBuildConfiguration(transCfg) == null)
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

	private void executeQuery(String parserID, String transCfg, String queryText) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put(IQueryConstants.PARSER_PARAMETER_ID, parserID);
		map.put(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID, transCfg);
		map.put(IQueryConstants.QUERY_PARAMETER_ID, queryText);
		
		ICommandService cS = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command cmd = cS.getCommand(IQueryConstants.ADD_QUERY_COMMAND_ID);
		ParameterizedCommand parCmd = ParameterizedCommand.generateCommand(cmd, map);
		handlerService.executeCommand(parCmd, null);

	}

}
