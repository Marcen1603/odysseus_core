package de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import de.uniol.inf.is.odysseus.rcp.viewer.view.IGraphEditorConstants;

public class GraphViewEditorOutlinePage extends ContentOutlinePage {

	private PhysicalGraphEditorInput input;

	public GraphViewEditorOutlinePage(PhysicalGraphEditorInput input) {
		this.input = input;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new GraphOutlineContentProvider());
		viewer.setLabelProvider(new GraphOutlineLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(input.getGraphView());

		MenuManager manager = new MenuManager(IGraphEditorConstants.OUTLINE_CONTEXT_MENU_ID, IGraphEditorConstants.OUTLINE_CONTEXT_MENU_ID);
		manager.setRemoveAllWhenShown(true);
		Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getTree().setMenu(menu);

		IPageSite site = getSite();
		site.registerContextMenu(IGraphEditorConstants.OUTLINE_CONTEXT_MENU_ID, manager, viewer);
	}
}
