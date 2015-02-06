package de.uniol.inf.is.odysseus.peer.rcp.views.peer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.peer.PeerID;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class PeerViewUsageContainer implements IPeerDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerViewUsageContainer.class);
	private static final long REFRESH_INTERVAL_MILLIS = 5000;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final List<PeerID> foundPeerIDs = Lists.newArrayList();
	private final Collection<PeerID> refreshing = Lists.newLinkedList();

	private TableViewer tableViewer;
	private RepeatingJobThread refresher;

	public final void init(TableViewer table) {
		Preconditions.checkNotNull(table, "Table must not be null!");

		this.tableViewer = table;
		
		RCPP2PNewPlugIn.getPeerDictionary().addListener(this);
		
		refresher = new RepeatingJobThread(REFRESH_INTERVAL_MILLIS, "Refresher of peer view resource usages") {
			@Override
			public void doJob() {
				refreshAllPeers();
			}
		};
		refresher.start();
	}
	
	public final void dispose() {
		if (refresher != null) {
			refresher.stopRunning();
			refresher = null;
		}

		RCPP2PNewPlugIn.getPeerDictionary().removeListener(this);
	}

	public Collection<PeerID> getPeerIDs() {
		synchronized (usageMap) {
			return Lists.newArrayList(usageMap.keySet());
		}
	}

	public int getPeerIDCount() {
		synchronized (usageMap) {
			return usageMap.size();
		}
	}

	Collection<PeerID> getFoundPeerIDsList() {
		return foundPeerIDs;
	}

	public Optional<IResourceUsage> determineResourceUsage(PeerID id) {
		synchronized (usageMap) {
			return Optional.fromNullable(usageMap.get(id));
		}
	}

	public void refreshAllPeers() {
		Collection<PeerID> foundPeerIDsCopy = null;
		synchronized (foundPeerIDs) {
			foundPeerIDs.clear();
			foundPeerIDs.addAll(RCPP2PNewPlugIn.getPeerDictionary().getRemotePeerIDs());
			foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
		}
		refreshTableAsync();

		for (final PeerID remotePeerID : foundPeerIDsCopy) {
			refreshPeer(remotePeerID);
		}

		for (PeerID peerID : usageMap.keySet().toArray(new PeerID[0])) {
			if (!foundPeerIDs.contains(peerID)) {
				usageMap.remove(peerID);
			}
		}
	}

	public void refreshPeer(final PeerID peer) {
		final IPeerResourceUsageManager usageManager = RCPP2PNewPlugIn.getPeerResourceUsageManager();

		synchronized (refreshing) {
			if (refreshing.contains(peer)) {
				return;
			}

			refreshing.add(peer);
		}

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Future<Optional<IResourceUsage>> futureUsage = usageManager.getRemoteResourceUsage(peer);

					try {

						Optional<IResourceUsage> optResourceUsage = futureUsage.get();
						if (optResourceUsage.isPresent()) {

							IResourceUsage resourceUsage = optResourceUsage.get();
							synchronized (usageMap) {
								usageMap.put(peer, resourceUsage);
							}
							refreshTableAsync();
						}

					} catch (InterruptedException | ExecutionException e) {
						LOG.error("Could not get resource usage", e);
					}

				} finally {
					synchronized (refreshing) {
						refreshing.remove(peer);
					}
				}
			}
		});

		t.setDaemon(true);
		t.setName("PeerView updater");
		t.start();
	}

	private void refreshTableAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (tableViewer) {
						if (!tableViewer.getTable().isDisposed()) {
							tableViewer.refresh();
						}
					}
				}
			});
		}
	}

	@Override
	public void peerAdded(PeerID peer) {
		if (foundPeerIDs.contains(peer)) {
			return;
		}

		synchronized (foundPeerIDs) {
			foundPeerIDs.add(peer);
		}

		refreshTableAsync();
		refreshPeer(peer);
	}

	@Override
	public void peerRemoved(PeerID peer) {
		synchronized (foundPeerIDs) {
			if (foundPeerIDs.contains(peer)) {
				foundPeerIDs.remove(foundPeerIDs);
				refreshTableAsync();
			}
		}
	}
}
