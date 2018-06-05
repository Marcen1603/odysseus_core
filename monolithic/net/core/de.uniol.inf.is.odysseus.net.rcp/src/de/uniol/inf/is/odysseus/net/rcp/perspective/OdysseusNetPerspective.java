package de.uniol.inf.is.odysseus.net.rcp.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import de.uniol.inf.is.odysseus.net.rcp.views.ChatView;
import de.uniol.inf.is.odysseus.net.rcp.views.DistributedDataView;
import de.uniol.inf.is.odysseus.net.rcp.views.NodeViewPart;
import de.uniol.inf.is.odysseus.net.rcp.views.PingMapView;

public class OdysseusNetPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		// Top left: Project Explorer view and Bookmarks view placeholder
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);
		topLeft.addView(ProjectExplorer.VIEW_ID);

		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea);
		right.addView(IPageLayout.ID_OUTLINE);
		
		IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.33f, IPageLayout.ID_OUTLINE);
		bottomRight.addView(PingMapView.VIEW_ID);
		
		// Bottom right: Task List view
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.60f, editorArea);
		bottom.addView(NodeViewPart.VIEW_ID);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView("org.eclipse.pde.runtime.LogView");
		bottom.addView(ChatView.VIEW_ID);
		bottom.addView(DistributedDataView.VIEW_ID);
	}

}
