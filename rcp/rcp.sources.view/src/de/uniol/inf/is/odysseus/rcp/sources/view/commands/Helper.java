package de.uniol.inf.is.odysseus.rcp.sources.view.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.sources.view.ISourcesConstants;
import de.uniol.inf.is.odysseus.rcp.sources.view.part.SourcesViewPart;

public final class Helper {
	
	private Helper() {}
	
	public static SourcesViewPart getTreeViewer(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		return (SourcesViewPart) page.findView(ISourcesConstants.SOURCES_VIEW_ID);
	}
}
