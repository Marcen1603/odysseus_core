package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class NMQNCommand extends AbstractHandler implements IHandler {

	private final Logger logger = LoggerFactory.getLogger(NMQNCommand.class);
	
	private static final String[] queries = {
			"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 [UNBOUNDED] AS b",
			"SELECT auction, price FROM nexmark:bid2 WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
			"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:person2 [UNBOUNDED] AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
			"SELECT AVG(q.final) FROM nexmark:category2 AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:bid2 [UNBOUNDED] AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
			"SELECT b.auction, b.price, b.bidder	FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME] AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME]) AS sub WHERE sub.max_price = b.price",
			"SELECT p.id, p.name, a.reserve FROM nexmark:person2 [SIZE 12 HOURS ADVANCE 1 TIME] AS p, nexmark:auction2 [SIZE 12 HOURS ADVANCE 1 TIME] AS a WHERE p.id = a.seller" };

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if( Activator.getExecutor() == null ) {
			StatusBarManager.getInstance().setMessage("No executor available");
			
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.OK);
		    box.setMessage("No executor available");
		    box.setText("Error");
		    box.open();
		    
			return null;
		}
		
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		boolean allOK = true;
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
				allOK = false;
			}
		}
		if( allOK ) 
			StatusBarManager.getInstance().setMessage("Nexmark Queries added successfully");
		else
			StatusBarManager.getInstance().setMessage("Errors during adding nexmark queries");

		return null;
	}

}
