package de.uniol.inf.is.odysseus.rcp.util;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class ViewHelper {

	private ViewHelper() {}
	
	@SuppressWarnings("unchecked")
	public static <T extends IViewPart> T getView( String viewID ) {
		Assert.isNotNull(viewID, "viewID");
		
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewID);
		return (T) part;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IViewPart> T getView(String viewID, ExecutionEvent event) {
		Assert.isNotNull(viewID, "viewID");
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		return (T) page.findView(viewID);
	}
}
