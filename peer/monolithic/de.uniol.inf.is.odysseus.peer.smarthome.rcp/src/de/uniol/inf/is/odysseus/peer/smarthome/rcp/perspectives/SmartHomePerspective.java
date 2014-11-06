package de.uniol.inf.is.odysseus.peer.smarthome.rcp.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

public class SmartHomePerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		
		// Top left: Project Explorer view and Bookmarks view placeholder
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
		topLeft.addView(ProjectExplorer.VIEW_ID);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
		right.addView(IPageLayout.ID_OUTLINE);
		
		// Bottom right: Task List view
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.60f, editorArea);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView("org.eclipse.pde.runtime.LogView");
		
		IFolderLayout top = layout.createFolder("top", IPageLayout.TOP, 0.25f, editorArea);
		top.addView(IPageLayout.ID_EDITOR_AREA);
		//top.addView("de.uniol.inf.is.odysseus.peer.smarthome.rcp.views.SmartDeviceConfigurationView");
		
		
		
	}

}
