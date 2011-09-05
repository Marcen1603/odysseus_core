package de.uniol.inf.is.odysseus.rcp.viewer.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import de.uniol.inf.is.odysseus.rcp.viewer.editors.impl.GraphViewEditorOutlinePage;

public class RefreshGraphOutlineCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if( part instanceof ContentOutline ) {
			ContentOutline outline = (ContentOutline)part;
			IPage page = outline.getCurrentPage();
			
			if( page instanceof GraphViewEditorOutlinePage ) {
				GraphViewEditorOutlinePage graphPage = (GraphViewEditorOutlinePage)page;
				graphPage.refresh();
			}
		}
		
		return null;
	}

}
