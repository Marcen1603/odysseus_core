package de.uniol.inf.is.odysseus.rcp.perspective.queries.impl;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import de.uniol.inf.is.odysseus.rcp.perspective.queries.IQueriesPerspectiveConstants;

public class QueriesPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 1.0f, editorArea);
		
		layout.addPerspectiveShortcut(IQueriesPerspectiveConstants.PERSPECTIVE_ID);
	}

}
