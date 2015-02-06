package de.uniol.inf.is.odysseus.peer.rcp.views.peer;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class PeerView extends ViewPart {

	private static PeerView instance;

	private final PeerViewUsageContainer usageContainer = new PeerViewUsageContainer();

	private PeerTableViewer peerTableViewer;

	public static Optional<PeerView> getInstance() {
		return Optional.fromNullable(instance);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		peerTableViewer = new PeerTableViewer(parent, usageContainer);
		usageContainer.init(peerTableViewer.getTableViewer());
		
		peerTableViewer.getTableViewer().getTable().addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				setPartName("Peers (" + usageContainer.getPeerIDCount() + ")");
			}
		});

		instance = this;
	}

	@Override
	public void dispose() {
		peerTableViewer.dispose();
		usageContainer.dispose();

		instance = null;

		super.dispose();
	}

	public Collection<PeerID> getSelectedPeerIDs() {
		IStructuredSelection selection = (IStructuredSelection) peerTableViewer.getTableViewer().getSelection();
		Collection<PeerID> peers = Lists.newArrayList();

		for (Object selectedObject : selection.toArray()) {
			peers.add((PeerID) selectedObject);
		}

		return peers;
	}

	@Override
	public void setFocus() {
		peerTableViewer.getTableViewer().getTable().setFocus();
	}

	public void refresh() {
		usageContainer.refreshAllPeers();
	}

	public void clear() {
		usageContainer.clear();
		
		usageContainer.refreshAllPeers();
	}
}
