package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Observable;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryP2PListener;

/**
 * The RecoveryPeerFailureDetector implements {@link IRecoveryP2PListener}. It
 * detects if a peer leaves the network and initiates the recovery accordingly.
 * 
 * @author Simon Kuespert & Tobias Brandt
 * 
 */
public class RecoveryP2PListener extends Observable implements IRecoveryP2PListener,
		IP2PNetworkListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryP2PListener.class);

	private final static int WAIT_TIME_MS = 1000;

	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;

	private Collection<PeerID> savedPeerIDs;
	private Thread failPeerListenerThread;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		if(p2pNetworkManager != null) {
			p2pNetworkManager.addListener(this);
		}
		LOG.debug("PeerFailureDetector activated");
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		LOG.debug("PeerFailureDetector deactivated");
	}

	@Override
	public void startPeerFailureDetection() {
		LOG.debug("Peer failure detection started");

		if (failPeerListenerThread != null) {
			// Stop old thread if it is already running
			failPeerListenerThread.interrupt();
			failPeerListenerThread = null;
			LOG.debug("Stopped already running peer failure detection.");
		}

		failPeerListenerThread = new Thread() {

			@Override
			public void run() {
				while (!isInterrupted()) {
					Collection<PeerID> peerIDs = p2pDictionary.getRemotePeerIDs();
					if (savedPeerIDs == null) {
						savedPeerIDs = peerIDs;
					} else {
						// Check, if we lost a peer
						for (PeerID pid : savedPeerIDs) {
							if (!peerIDs.contains(pid)) {
								LOG.debug("Lost peer");
								// We lost a peer, start recovery
								P2PNetworkNotification notification = new P2PNetworkNotification(
										P2PNetworkNotification.LOST_PEER, pid);
								setChanged();
								notifyObservers(notification);

							}
						}

						// Check, if there is a new peer
						for (PeerID pid : peerIDs) {
							if (!savedPeerIDs.contains(pid)) {
								// We found a new peer
								LOG.debug("Found new peer: {}", p2pDictionary.getRemotePeerName(pid));
								P2PNetworkNotification notification = new P2PNetworkNotification(
										P2PNetworkNotification.FOUND_PEER, pid);
								setChanged();
								notifyObservers(notification);
							}
						}

						savedPeerIDs = peerIDs;
					}

					synchronized (this) {
						try {
							this.wait(WAIT_TIME_MS);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};

		if (failPeerListenerThread != null && p2pNetworkManager.getLocalPeerName() != null) {
			failPeerListenerThread.setName("PeerFailureDetection_"
					+ p2pNetworkManager.getLocalPeerName() + "_"
					+ p2pNetworkManager.getLocalPeerID().toString());
			failPeerListenerThread.setDaemon(true);
			failPeerListenerThread.start();
		}

	}

	@Override
	public void stopPeerFailureDetection() {
		if (failPeerListenerThread != null) {
			failPeerListenerThread.interrupt();
			LOG.debug("Peer failure detection stopped");
		} else {
			LOG.debug("Tried to stop peer failure detection, but it was stopped already.");
		}
	}

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		// Start listening to the network as soon as it's started
		if (failPeerListenerThread != null && !failPeerListenerThread.isAlive()) {
			failPeerListenerThread.setName("PeerFailureDetection_"
					+ p2pNetworkManager.getLocalPeerName() + "_"
					+ p2pNetworkManager.getLocalPeerID().toString());
			failPeerListenerThread.setDaemon(true);
			failPeerListenerThread.start();
		}
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
		failPeerListenerThread.interrupt();
		failPeerListenerThread = null;

	}

}
