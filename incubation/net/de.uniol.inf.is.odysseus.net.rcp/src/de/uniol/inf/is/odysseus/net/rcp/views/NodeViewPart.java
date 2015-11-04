package de.uniol.inf.is.odysseus.net.rcp.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class NodeViewPart extends ViewPart {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.NodeView";
	
	private NodeTableViewer nodeTableViewer;
	private NodeViewUsageContainer container = new NodeViewUsageContainer();
	
	@Override
	public void createPartControl(Composite parent) {
		nodeTableViewer = new NodeTableViewer(parent, container);
		
		container.init(nodeTableViewer.getTableViewer());
	}

	@Override
	public void setFocus() {
		nodeTableViewer.getTableViewer().getTable().setFocus();
	}
	
	@Override
	public void dispose() {
		container.dispose();
		
		nodeTableViewer.dispose();
	}

}
