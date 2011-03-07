package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreak;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreakManager;

public class RemoveOperatorBreakCommand  extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();

		if (selection == null)
			return null;

		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			
			Object selObject = structSelection.getFirstElement();
			OperatorBreak ob = (OperatorBreak)selObject;

			if( ob != null ) {
				if( ob.isBreaked() )
					ob.endBreak();
				OperatorBreakManager.getInstance().remove(ob);
			}
			else
				System.out.println("No OperatorBreak found!");
			
		}
		
		return false;
	}

}
