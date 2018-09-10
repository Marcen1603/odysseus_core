package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.GraphViewEditor;

public class RefreshGraphEditor extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		IEditorPart activeEditor = page.getActiveEditor();
		if(activeEditor instanceof GraphViewEditor) {
			GraphViewEditor editor = (GraphViewEditor)activeEditor;
			editor.refresh();
		}
		
		return null;
	}

}
