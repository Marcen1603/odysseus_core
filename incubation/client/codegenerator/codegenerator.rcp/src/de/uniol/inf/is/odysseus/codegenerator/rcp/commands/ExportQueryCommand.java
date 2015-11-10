package de.uniol.inf.is.odysseus.codegenerator.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.codegenerator.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

/**
 * this command is executed when the user click in the gui of
 * "export query"
 * 
 * @author MarcPreuschaft
 *
 */
public class ExportQueryCommand extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//get all selected items (queries)
		List<Object> selections = SelectionProvider.getSelection(event);
		
		//create for every query a new QueryTransformationWindow window
		for (Object selection : selections) {
				int queryId = (Integer)selection;
		
				//create the codegeneration configuration gui
				QueryTransformationWindow window = new QueryTransformationWindow(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),queryId);
				//show the gui
				window.show();
				
					
		}
		return null;
	}

	
}
