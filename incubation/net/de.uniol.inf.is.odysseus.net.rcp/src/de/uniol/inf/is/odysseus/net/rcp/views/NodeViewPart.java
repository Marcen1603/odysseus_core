package de.uniol.inf.is.odysseus.net.rcp.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.net.util.RepeatingJobThread;

public class NodeViewPart extends ViewPart {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.NodeView";
	
	private static final int NODE_VIEW_UPDATE_INTERVAL_MILLIS = 3000;
	
	private NodeTableViewer nodeTableViewer;
	private RepeatingJobThread updater;
	
	@Override
	public void createPartControl(Composite parent) {
		nodeTableViewer = new NodeTableViewer(parent);
		
		updater = new RepeatingJobThread(NODE_VIEW_UPDATE_INTERVAL_MILLIS, "NodeView updater") {
			@Override
			public void doJob() {
				if( !nodeTableViewer.getTableViewer().getTable().isDisposed() ) {
					nodeTableViewer.refreshTableAsync();
				}
			}
		};
	}

	@Override
	public void setFocus() {
		nodeTableViewer.getTableViewer().getTable().setFocus();
	}
	
	@Override
	public void dispose() {
		nodeTableViewer.dispose();
		
		updater.stopRunning();
		updater = null;
	}

}
