package de.uniol.inf.is.odysseus.rcp.editor.navigator.command;

import org.eclipse.core.commands.ExecutionEvent;
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
}
