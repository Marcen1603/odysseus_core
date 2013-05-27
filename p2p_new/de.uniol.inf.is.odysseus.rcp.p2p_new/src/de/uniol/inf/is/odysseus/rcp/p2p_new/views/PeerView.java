package de.uniol.inf.is.odysseus.rcp.p2p_new.views;

import java.util.List;

import net.jxta.peer.PeerID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.rcp.p2p_new.service.P2PDictionaryService;

public class PeerView extends ViewPart implements IP2PDictionaryListener {

	private static final String UNKNOWN_PEER_NAME = "<unknown>";

	private final List<PeerID> foundPeerIDs = Lists.newArrayList();

	private static PeerView instance;

	private Text text;

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		final IP2PDictionary p2pDictionary = P2PDictionaryService.get();
		p2pDictionary.addListener(this);
		refresh();

		setPartName("PeerView (" + p2pDictionary.getLocalPeerName() + ")");
		instance = this;
	}

	@Override
	public void dispose() {
		instance = null;

		P2PDictionaryService.get().removeListener(this);
		text.dispose();

		super.dispose();
	}
	
	public final void refresh() {
		final IP2PDictionary p2pDictionary = P2PDictionaryService.get();

		foundPeerIDs.clear();
		for (final PeerID peerID : p2pDictionary.getPeerIDs()) {
			peerAdded(p2pDictionary, peerID, p2pDictionary.getPeerName(peerID).get());
		}
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
							for (final PeerID peerID : foundPeerIDs) {
								final Optional<String> optPeerName = P2PDictionaryService.get().getPeerName(peerID);
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

	public static Optional<PeerView> getInstance() {
		return Optional.fromNullable(instance);
	}

	@Override
	public void sourceAdded(IP2PDictionary sender, SourceAdvertisement advertisement) {
		// do nothing
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender, SourceAdvertisement advertisement) {
		// do nothing
	}

	@Override
	public void sourceImported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing	
	}

	@Override
	public void sourceExported(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing	
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender, SourceAdvertisement advertisement, String sourceName) {
		// do nothing
	}

	@Override
	public void peerAdded(IP2PDictionary sender, PeerID id, String name) {
		synchronized (foundPeerIDs) {
			foundPeerIDs.add(id);
		}

		refreshText();
	}

	@Override
	public void peerRemoved(IP2PDictionary sender, PeerID id, String name) {
		synchronized (foundPeerIDs) {
			foundPeerIDs.remove(id);
		}

		refreshText();
	}

}
