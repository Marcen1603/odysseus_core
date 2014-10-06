package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Observable;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RecoveryP2PListener extends Observable implements
		IRecoveryP2PListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryP2PListener.class);

	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;

	private Collection<PeerID> savedPeerIDs;
	private volatile boolean detectionStarted = false;

	private static RecoveryP2PListener instance;

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
		instance = this;
		LOG.debug("PeerFailureDetector activated");
	}

	/**
	 * Called by OSGi on Bundle deactivation.
	 */
	public void deactivate() {
		instance = null;
		LOG.debug("PeerFailureDetector deactivated");
	}

	public static RecoveryP2PListener getInstance() {
		return instance;
	}

	@Override
	public void startPeerFailureDetection() {
		LOG.debug("Peer failure detection started");
		detectionStarted = true;

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (detectionStarted) {
					Collection<PeerID> peerIDs = p2pDictionary
							.getRemotePeerIDs();
					if (savedPeerIDs == null) {
						savedPeerIDs = peerIDs;
					} else {
						// Check, if we lost a peer
						for (PeerID pid : savedPeerIDs) {
							if (!peerIDs.contains(pid)) {
								LOG.debug("Peer is not in list anymore");
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
							this.wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}

		});

		if (p2pNetworkManager != null) {
			LOG.debug("No P2PNetworkmanager bound.");
			thread.setName("PeerFailureDetection_"
					+ p2pNetworkManager.getLocalPeerName() + "_"
					+ p2pNetworkManager.getLocalPeerID().toString());
			thread.setDaemon(true);
			thread.start();
		}

	}

	@Override
	public void stopPeerFailureDetection() {
		LOG.debug("Peer failure detection stopped");
		detectionStarted = false;

	}

}
