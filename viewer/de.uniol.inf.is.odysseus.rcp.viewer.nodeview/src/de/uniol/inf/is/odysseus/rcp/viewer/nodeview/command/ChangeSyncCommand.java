package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.nodeview.INodeViewConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.nodeview.impl.NodeViewPart;

public class ChangeSyncCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		NodeViewPart view = (NodeViewPart) page.findView(INodeViewConstants.NODEVIEW_ID);
		if( view != null )
			view.setSync(!view.getSync());
		return null;
	}

}
