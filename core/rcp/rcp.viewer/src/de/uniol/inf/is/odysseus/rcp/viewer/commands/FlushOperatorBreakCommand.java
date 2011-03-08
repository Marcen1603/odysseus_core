package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreak;
import de.uniol.inf.is.odysseus.rcp.viewer.opbreak.OperatorBreakManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class FlushOperatorBreakCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = window.getActivePage().getSelection();

		if (selection == null)
			return null;

		if (selection instanceof IStructuredSelection) {

			IStructuredSelection structSelection = (IStructuredSelection) selection;
			
			Object selObject = structSelection.getFirstElement();
			OperatorBreak ob = null;
			
			if( selObject instanceof IOdysseusNodeView) {
				IOdysseusNodeView node = (IOdysseusNodeView) selObject;
	
				IPhysicalOperator operator = node.getModelNode().getContent();
	
				for( OperatorBreak o : OperatorBreakManager.getInstance().getAll()) {
					if( o.getOperator() == operator ) {  
						ob = o;
						break;
					}
				}
				
			} else if( selObject instanceof OperatorBreak ) {
				ob = (OperatorBreak)selObject;
				
			}

			if( ob != null ) {
				ob.flush();
			}
			else
				System.out.println("No OperatorBreak found!");
			
		}
		
		return false;	}

}
