package de.uniol.inf.is.odysseus.net.rcp.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;

public class NodeViewPart extends ViewPart {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.NodeView";
	
	private static NodeViewPart instance = null; 
	
	private NodeTableViewer nodeTableViewer;
	private NodeViewUsageContainer container = new NodeViewUsageContainer();
	
	
	@Override
	public void createPartControl(Composite parent) {
		nodeTableViewer = new NodeTableViewer(parent, container);
		
		container.init(nodeTableViewer.getTableViewer());
		
		instance = this;
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

}
