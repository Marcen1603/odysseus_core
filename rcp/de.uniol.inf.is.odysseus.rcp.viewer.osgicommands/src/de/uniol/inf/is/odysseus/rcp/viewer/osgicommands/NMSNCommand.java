package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class NMSNCommand extends AbstractHandler implements IHandler {

	private final Logger logger = LoggerFactory.getLogger(NMSNCommand.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IAdvancedExecutor exec = Activator.getExecutor();
//		if( exec != null ) {
//			for (String s : q) {
//				try {
//					// TODO: User einfuegen, der diese Query ausf�hrt
//					User user = new User("TODO.SetUser");
//					 
//					exec.addQuery(s, "CQL", user, Activator.getTrafoConfigParam());
//				} catch (PlanManagementException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return null;
		
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		String[] queries = new String[4];
		queries[0] = "CREATE STREAM nexmark:person2 (timestamp STARTTIMESTAMP,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
		queries[1] = "CREATE STREAM nexmark:bid2 (timestamp STARTTIMESTAMP,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
		queries[2] = "CREATE STREAM nexmark:auction2 (timestamp STARTTIMESTAMP,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
		queries[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
		for( String q  : queries ) {
			try {
				Map<String,String> map = new HashMap<String, String>();
				map.put(IQueryConstants.PARSER_PARAMETER_ID, "CQL");
				map.put(IQueryConstants.QUERY_PARAMETER_ID, q);
				map.put(IQueryConstants.PARAMETER_TRANSFORMATION_CONFIGURATION_NAME_PARAMETER_ID, "Standard");
				
				ICommandService cS = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
				Command cmd = cS.getCommand(IQueryConstants.ADD_QUERY_COMMAND_ID);
				ParameterizedCommand parCmd = ParameterizedCommand.generateCommand(cmd, map);
				handlerService.executeCommand(parCmd, null);
				
			} catch( Exception ex ) {
				new ExceptionWindow(ex);
				logger.error("Cannot execute Command:", ex);
			}
		}
		return null;
	}

}
