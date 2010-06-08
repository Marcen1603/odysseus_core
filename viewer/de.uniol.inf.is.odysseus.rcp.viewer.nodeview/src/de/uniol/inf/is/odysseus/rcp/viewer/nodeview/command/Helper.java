package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.nodeview.NodeViewPart;

public class Helper {

	public static TreeViewer getTreeViewer(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		NodeViewPart view = (NodeViewPart) page.findView(NodeViewPart.VIEW_ID);
		return view.getTreeViewer();
	}
	
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
