package de.uniol.inf.is.odysseus.codegenerator.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.codegenerator.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class ExportQueryCommand extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		List<Object> selections = SelectionProvider.getSelection(event);
		
		for (Object selection : selections) {
				int queryId = (Integer)selection;
		
				QueryTransformationWindow window = new QueryTransformationWindow(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),queryId);
				window.show();
				
					
		}
		return null;
	}

	
}
