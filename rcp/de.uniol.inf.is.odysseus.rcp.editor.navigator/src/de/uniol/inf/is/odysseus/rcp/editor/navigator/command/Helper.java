package de.uniol.inf.is.odysseus.rcp.editor.navigator.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.editor.navigator.INavigatorConstants;
import de.uniol.inf.is.odysseus.rcp.editor.navigator.view.NavigatorViewPart;

public class Helper {

	public static NavigatorViewPart getNavigatorViewPart(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		return (NavigatorViewPart) page.findView(INavigatorConstants.NAVIGATOR_VIEW_ID);
	}
	
	public static Object[] getSelection(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if( selection == null ) return null;
		
		if( selection instanceof IStructuredSelection ) {
			
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			return structSelection.toArray();
			
		}
		return null;
	}}
