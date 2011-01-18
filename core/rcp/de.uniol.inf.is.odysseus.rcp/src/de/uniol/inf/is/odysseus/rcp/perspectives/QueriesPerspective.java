package de.uniol.inf.is.odysseus.rcp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class QueriesPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.70f, editorArea);
	}

}
