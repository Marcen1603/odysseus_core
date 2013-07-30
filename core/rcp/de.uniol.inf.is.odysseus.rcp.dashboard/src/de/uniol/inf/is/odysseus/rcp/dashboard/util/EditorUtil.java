package de.uniol.inf.is.odysseus.rcp.dashboard.util;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;

public final class EditorUtil {

	private EditorUtil() {
		
	}
	
	public static boolean isActiveEditorDashboardEditor() {
		IEditorPart activeEditor = determineActiveEditor();
		return activeEditor instanceof DashboardEditor;
	}

	public static IEditorPart determineActiveEditor() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		return activeEditor;
	}
	
	public static Shell determineCurrentShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}
