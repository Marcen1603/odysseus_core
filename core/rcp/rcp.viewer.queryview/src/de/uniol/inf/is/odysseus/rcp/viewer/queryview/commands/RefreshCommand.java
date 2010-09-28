package de.uniol.inf.is.odysseus.rcp.viewer.queryview.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.viewer.queryview.IQueryViewConstants;
import de.uniol.inf.is.odysseus.rcp.viewer.queryview.view.QueryViewPart;

public class RefreshCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		QueryViewPart part = getQueryViewPart(event);
		
		if( part != null ) {
			part.refreshTable();
			return true;
		}
		
		return false;
	}

	private static QueryViewPart getQueryViewPart(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		return (QueryViewPart) page.findView(IQueryViewConstants.VIEW_ID);
	}
}
