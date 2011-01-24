package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public final class SelectionProvider {

	private SelectionProvider() {}
	
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
