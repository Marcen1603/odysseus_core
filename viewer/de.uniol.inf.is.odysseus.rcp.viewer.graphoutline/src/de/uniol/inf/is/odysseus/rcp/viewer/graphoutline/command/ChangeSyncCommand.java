package de.uniol.inf.is.odysseus.rcp.viewer.graphoutline.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.graphoutline.IGraphOutlineConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.graphoutline.impl.GraphOutlinePart;

public class ChangeSyncCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		GraphOutlinePart view = (GraphOutlinePart) page.findView(IGraphOutlineConstants.NODEVIEW_ID);
		if( view != null )
			view.setSync(!view.getSync());
		return null;
	}

}
