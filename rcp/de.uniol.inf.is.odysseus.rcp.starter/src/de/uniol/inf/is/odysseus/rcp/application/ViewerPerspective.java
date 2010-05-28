package de.uniol.inf.is.odysseus.rcp.application;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

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
