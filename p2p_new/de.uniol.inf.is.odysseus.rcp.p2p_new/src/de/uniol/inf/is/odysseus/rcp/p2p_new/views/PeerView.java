package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IPeerListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.rcp.p2p_new.RCPP2PNewPlugIn;

public class PeerView extends ViewPart implements IPeerListener {

	private Text text;

	private final List<String> foundPeerIDs = Lists.newArrayList();

	@Override
	public void createPartControl(Composite parent) {
		RCPP2PNewPlugIn.getPeerManager().addListener(this);

		text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

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
								text.append(peerID + "\n");
							}
						}

						text.setRedraw(true);
					}
				}
			});
		}
	}

}
