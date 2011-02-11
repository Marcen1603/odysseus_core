package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.SearchEditorInput;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.SearchEditorPart;

/**
 * Diese Klasse öffnet den Suchen-Editor
 * 
 * @author Stefanie Witzke
 *
 */
public class OpenSearchHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Holt die aktuelle View
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		try {
			// Öffnet den Suchen-Editor
			page.openEditor(new SearchEditorInput(), SearchEditorPart.ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
