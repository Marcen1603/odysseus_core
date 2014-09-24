package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryPeerFailureDetector;

/**
 * The RecoveryPeerFailureDetector implements
 * {@link IRecoveryPeerFailureDetector}. It detects if a peer leaves the network
 * and initiates the recovery accordingly.
 * 
 * @author Simon Kuespert
 * 
 */
public class PeerFailureDetector implements IRecoveryPeerFailureDetector {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(PeerFailureDetector.class);

	private static IP2PDictionary p2pDictionary;

	private Collection<PeerID> savedPeerIDs;
	private volatile boolean detectionStarted = false;

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

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
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
		detectionStarted = true;

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (detectionStarted) {
					Collection<PeerID> peerIDs = p2pDictionary
							.getRemotePeerIDs();
					if (savedPeerIDs == null) {
						savedPeerIDs = peerIDs;
					} else {
						for (PeerID pid : savedPeerIDs) {
							if (!peerIDs.contains(pid)) {
								LOG.debug("Peer is not in list anymore");
								//TODO do something like send a message
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
		thread.setName("PeerFailureDetection");
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void stopPeerFailureDetection() {
		LOG.debug("Peer failure detection stopped");
		detectionStarted = false;

	}

}
