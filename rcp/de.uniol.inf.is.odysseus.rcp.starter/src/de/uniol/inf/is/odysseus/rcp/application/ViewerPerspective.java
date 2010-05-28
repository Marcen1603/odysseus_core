package de.uniol.inf.is.odysseus.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class ViewerPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "de.uniol.inf.is.odysseus.rcp.application.perspective.viewer";
	
	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 1.0f, editorArea);
		
		layout.addPerspectiveShortcut(PERSPECTIVE_ID);
	}

}
