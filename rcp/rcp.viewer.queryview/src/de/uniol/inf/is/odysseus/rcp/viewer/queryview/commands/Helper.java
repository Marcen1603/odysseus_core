package de.uniol.inf.is.odysseus.rcp.viewer.queryview.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class Helper {
	
	public static Object getSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if( selection == null ) return null;
		
		if( selection instanceof IStructuredSelection ) {
			
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			return structSelection.getFirstElement();
			
		}
		return null;
	}
}
