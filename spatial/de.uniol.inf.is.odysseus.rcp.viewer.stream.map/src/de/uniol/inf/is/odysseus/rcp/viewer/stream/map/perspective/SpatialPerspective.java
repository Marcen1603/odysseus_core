package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class SpatialPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		
		String editorArea = layout.getEditorArea();

		// Top left: Project Explorer view and Bookmarks view placeholder
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.2f, editorArea);
		topLeft.addView(ProjectExplorer.VIEW_ID);

		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
		right.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapLayerView");
		right.addView(IPageLayout.ID_OUTLINE);
		
		// Bottom right: Task List view
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75f, editorArea);
		bottom.addView(OdysseusRCPPlugIn.QUERY_VIEW_ID);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView("org.eclipse.pde.runtime.LogView");

//		layout.addNewWizardShortcut();	
		
		
//		
//		//String editorArea = layout.getEditorArea();
//		String editorArea = "de.uniol.inf.is.odysseus.rcp.viewer.stream.map.MapEditor";
//
//		// Top left: Project Explorer view and Bookmarks view placeholder
//		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
//		topLeft.addView(ProjectExplorer.VIEW_ID);
//
//		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
//		right.addView(IPageLayout.ID_OUTLINE);
//		right.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapLayerView");
//		right.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapQueryFilesView");
//		right.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapConnectionView");
//		
//		// Bottom right: Task List view
//		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.60f, editorArea);
//		bottom.addView(OdysseusRCPPlugIn.QUERY_VIEW_ID);
//		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
//		bottom.addView("org.eclipse.pde.runtime.LogView");
//
//		// Wizards
//		layout.addNewWizardShortcut(OdysseusRCPPlugIn.WIZARD_PROJECT_ID);
//		
//		
//		
////		 layout.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.MapEditor", IPageLayout.TOP, IPageLayout.RATIO_MAX, IPageLayout.ID_EDITOR_AREA);
////		 layout.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapLayerView", IPageLayout.LEFT, IPageLayout.RATIO_MAX, IPageLayout.ID_OUTLINE);
////		 layout.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapQueryFilesView", IPageLayout.RIGHT, IPageLayout.RATIO_MAX, IPageLayout.ID_OUTLINE);
////		 layout.addView("de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views.MapConnectionView", IPageLayout.BOTTOM, IPageLayout.RATIO_MAX, IPageLayout.ID_OUTLINE);
//		 

	}

}
