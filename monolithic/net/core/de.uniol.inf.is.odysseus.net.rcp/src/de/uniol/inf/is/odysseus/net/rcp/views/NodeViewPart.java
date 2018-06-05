package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.Collection;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public class NodeViewPart extends ViewPart {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.NodeView";
	
	private static NodeViewPart instance = null; 
	
	private NodeTableViewer nodeTableViewer;
	private NodeViewUsageContainer container = new NodeViewUsageContainer();
	
	
	@Override
	public void createPartControl(Composite parent) {
		nodeTableViewer = new NodeTableViewer(parent, container);
		
		container.init(nodeTableViewer.getTableViewer());
		
		createContextMenu();
		
		instance = this;
	}
	
	private void createContextMenu() {
		// Contextmenu
		final MenuManager menuManager = new MenuManager();
		final Menu contextMenu = menuManager.createContextMenu(nodeTableViewer.getTableViewer().getTable());
		// Set the MenuManager
		nodeTableViewer.getTableViewer().getTable().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, nodeTableViewer.getTableViewer());
	}

	@Override
	public void setFocus() {
		nodeTableViewer.getTableViewer().getTable().setFocus();
	}
	
	@Override
	public void dispose() {
		container.dispose();
		
		nodeTableViewer.dispose();
		
		instance = null;
	}
	
	public static Optional<NodeViewPart> getInstance() {
		return Optional.fromNullable(instance);
	}
	
	public void clean() {
		container.cleanNodesList();
	}

	public void refresh() {
		nodeTableViewer.refreshTableAsync();
	}

	public Collection<IOdysseusNode> getSelectedNodes() {
		return nodeTableViewer.getSelectedNodes();
	}
}
