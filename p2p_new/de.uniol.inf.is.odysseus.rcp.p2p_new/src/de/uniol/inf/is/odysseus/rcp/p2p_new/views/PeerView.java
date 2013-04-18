package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IPeerListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class PeerView extends ViewPart implements IPeerListener {

	private static final String UNKNOWN_PEER_NAME = "<unknown>";

	private Text text;

	private final List<String> foundPeerIDs = Lists.newArrayList();

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		final IPeerManager peerManager = RCPP2PNewPlugIn.getPeerManager();
		peerManager.addListener(this);
		for (final String peerID : RCPP2PNewPlugIn.getPeerManager().getRemotePeerIDs()) {
			onPeerFound(peerManager, peerID);
		}
		
		setPartName("PeerView (" + P2PNewPlugIn.getOwnPeerName() + ")");
	}

	@Override
	public void dispose() {
		RCPP2PNewPlugIn.getPeerManager().removeListener(this);
		text.dispose();

		super.dispose();
	}

	@Override
	public void onPeerFound(IPeerManager sender, String peerID) {
		synchronized (foundPeerIDs) {
			foundPeerIDs.add(peerID);
		}

		refreshText();
	}

	@Override
	public void onPeerLost(IPeerManager sender, String peerID) {
		synchronized (foundPeerIDs) {
			foundPeerIDs.remove(peerID);
		}

		refreshText();
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

	protected final void refreshText() {
		final Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!text.isDisposed()) {
						text.setRedraw(false);

						text.setText("");
						synchronized (foundPeerIDs) {
							for (final String peerID : foundPeerIDs) {
								final Optional<String> optPeerName = RCPP2PNewPlugIn.getPeerManager().getPeerName(peerID);
								if (optPeerName.isPresent()) {
									text.append(optPeerName.get());
								} else {
									text.append(UNKNOWN_PEER_NAME);
								}
								text.append(" [" + peerID + "]\n");
							}
						}

						text.setRedraw(true);
					}
				}
			});
		}
	}

}
