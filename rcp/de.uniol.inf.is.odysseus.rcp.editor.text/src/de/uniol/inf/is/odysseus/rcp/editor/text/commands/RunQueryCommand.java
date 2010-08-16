package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryEntry;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryConstants;

public class RunQueryCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object obj = structuredSelection.getFirstElement();

			if (obj instanceof IFile) {

				try {
					// Datei öffnen
					IFile file = (IFile) obj;
					if (!file.isSynchronized(IResource.DEPTH_ZERO))
						file.refreshLocal(IResource.DEPTH_ZERO, null);

					// Datei einlesen
					ArrayList<String> lines = new ArrayList<String>();
					BufferedReader br = new BufferedReader( new InputStreamReader(file.getContents()));
					String line = br.readLine();
					while( line != null ) {
						lines.add(line);
						line = br.readLine();
					}
					br.close();
					
					QueryTextParser parser = new QueryTextParser();
					List<QueryEntry> queries = parser.parse(lines.toArray(new String[lines.size()]));
					
					// execute Queries
					for( QueryEntry entry : queries ) 
						executeQuery( entry.getParserID(), entry.getTransCfg(), entry.getQueryText());
					
					
				} catch (Exception ex) {
					ex.printStackTrace();
					new ExceptionWindow(ex);
				}
			}
		}
		return null;
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
