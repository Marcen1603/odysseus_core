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

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;

public class QueryPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(QueryTextParser parser, String parameter) throws QueryTextParseException {
		try {
			IAdvancedExecutor executor = ExecutorHandler.getExecutor();
			if (executor == null)
				throw new QueryTextParseException("No executor found");

			if (parameter.length() == 0)
				throw new QueryTextParseException("Encountered empty query");

			String parserID = parser.getVariable("PARSER");
			if (parserID == null)
				throw new QueryTextParseException("Parser not set");
			if (!executor.getSupportedQueryParser().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = parser.getVariable("TRANSCFG");
			if (transCfg == null)
				throw new QueryTextParseException("TransformationConfiguration not set");
			if (ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(transCfg) == null)
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not found");
			String userID = parser.getVariable("USER");
			User user = UserManagement.getInstance().login(userID, "");
			if( user == null ) 
				throw new QueryTextParseException("User " + userID + " not valid");
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown Exception during validation a query", ex);
		}
	}

	@Override
	public void execute(QueryTextParser parser, String parameter) throws QueryTextParseException {
		String parserID = parser.getVariable("PARSER");
		String transCfg = parser.getVariable("TRANSCFG");
		String userID = parser.getVariable("USER");

		User user = UserManagement.getInstance().login(userID, "");
		try {
			executeQuery(parserID, transCfg, parameter, user);
		} catch (Exception ex) {
			throw new QueryTextParseException("Error during executing query", ex);
		}
	}

	private void executeQuery(String parserID, String transCfg, String queryText, User user) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {
		parserID = parserID.trim();
		transCfg = transCfg.trim();
		queryText = queryText.trim();

		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put(IQueryConstants.PARSER_PARAMETER_ID, parserID);
		map.put(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID, transCfg);
		map.put(IQueryConstants.QUERY_PARAMETER_ID, queryText);
		map.put(IQueryConstants.USER_NAME_PARAMETER_ID, user.getUsername());
		map.put(IQueryConstants.USER_PASSWORD_PARAMETER_ID, user.getPassword());

		
		ICommandService cS = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command cmd = cS.getCommand(IQueryConstants.ADD_QUERY_COMMAND_ID);
		ParameterizedCommand parCmd = ParameterizedCommand.generateCommand(cmd, map);
		handlerService.executeCommand(parCmd, null);

	}

}
