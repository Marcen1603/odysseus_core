package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;
import java.util.Observable;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

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
	
	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional.absent();
	
	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P network manager to unbind. <br />
	 * Must be not null.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {
			
			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	/**
	 * The P2P dictionary, if there is one bound.
	 */
	private static Optional<IP2PDictionary> cP2PDictionary = Optional.absent();
	
	/**
	 * Binds a P2P dictionary. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to bind. <br />
	 * Must be not null.
	 */
	public static void bindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		cP2PDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a P2P dictionary.", serv
				.getClass().getSimpleName());
		
	}

	/**
	 * Unbinds a P2P dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * @param serv The P2P dictionary to unbind. <br />
	 * Must be not null.
	 */
	public void unbindP2PDictionary(IP2PDictionary serv) {
		
		Preconditions.checkNotNull(serv);
		
		if (cP2PDictionary.isPresent() && cP2PDictionary.get() == serv) {
			
			this.stopPeerFailureDetection();
			cP2PDictionary = Optional.absent();
			LOG.debug("Unbound {} as a P2P dictionary.", serv
					.getClass().getSimpleName());
			
		}
		
	}
	
	private final static int WAIT_TIME_MS = 1000;
	
	private class FailPeerListenerThread extends Thread {
		
		private volatile boolean mActive = false;
		
		public void activeInterrupt() {
			
			this.mActive = false;
			
		}
		
		private void handlePeerLost(PeerID pid) {
			
			P2PNetworkNotification notification = new P2PNetworkNotification(
					P2PNetworkNotification.LOST_PEER, pid);
			setChanged();
			notifyObservers(notification);
			
		}
		
		private void handleNewPeer(PeerID pid) {
			
			P2PNetworkNotification notification = new P2PNetworkNotification(
					P2PNetworkNotification.FOUND_PEER, pid);
			setChanged();
			notifyObservers(notification);
			
		}
		
		@Override
		public void run() {
			
			// Preconditions
			if(!cP2PDictionary.isPresent()) {
				
				LOG.error("No P2P dictionary bound!");
				return;
				
			}
			
			this.mActive = true;
			
			while (this.mActive) {
				
				Collection<PeerID> peerIDs = cP2PDictionary.get().getRemotePeerIDs();
				
				if (savedPeerIDs == null) {
					
					savedPeerIDs = peerIDs;
					
				} else {
					
					// Check, if we lost a peer
					for (PeerID pid : savedPeerIDs) {
						
						if (!peerIDs.contains(pid)) {
							
							// We lost a peer, start recovery
							LOG.debug("Lost peer");
							this.handlePeerLost(pid);

						}
					}

					// Check, if there is a new peer
					for (PeerID pid : peerIDs) {
						
						if (!savedPeerIDs.contains(pid)) {
							
							// We found a new peer
							LOG.debug("Found new peer: {}", cP2PDictionary.get().getRemotePeerName(pid));
							this.handleNewPeer(pid);
							
						}
					}

					savedPeerIDs = peerIDs;
					
				}

				synchronized (this) {
					
					try {
						
						this.wait(WAIT_TIME_MS);
						
					} catch (InterruptedException e) {
						
						LOG.error("FailPeerListenerThread interrupted");
						e.printStackTrace();
						this.mActive = false;
						
					}
					
				}
					
			}
			
		}
		
	}

	private Collection<PeerID> savedPeerIDs;
	private FailPeerListenerThread failPeerListenerThread;

	/**
	 * Called by OSGi on Bundle activation.
	 */
	public void activate() {
		
		if(cP2PNetworkManager.isPresent()) {
			cP2PNetworkManager.get().addListener(this);
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
		
		// Preconditions
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		}
		
		LOG.debug("Peer failure detection started");

		if (failPeerListenerThread != null) {
			// Stop old thread if it is already running
			failPeerListenerThread.activeInterrupt();
			failPeerListenerThread = null;
			LOG.debug("Stopped already running peer failure detection.");
		}

		failPeerListenerThread = new FailPeerListenerThread();

		if (failPeerListenerThread != null && cP2PNetworkManager.get().getLocalPeerName() != null) {
			
			// Having several peers on the same machine started at the same time may cause timing problems
			int numTries = 0;			
			while(cP2PNetworkManager.get().getLocalPeerID() == null && numTries < 10) {
				
				synchronized (this) {
					
					try {
						
						this.wait(WAIT_TIME_MS);
						numTries++;
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						break;
						
					}
					
				}
				
			}
			
			if(cP2PNetworkManager.get().getLocalPeerID() == null) {
				
				LOG.error("Can not determine local peer ID!");
				return;
				
			}
			
			failPeerListenerThread.setName("PeerFailureDetection_"
					+ cP2PNetworkManager.get().getLocalPeerName() + "_"
					+ cP2PNetworkManager.get().getLocalPeerID().toString());
			failPeerListenerThread.setDaemon(true);
			failPeerListenerThread.start();
		}

	}

	@Override
	public void stopPeerFailureDetection() {
		if (failPeerListenerThread != null) {
			failPeerListenerThread.activeInterrupt();
			LOG.debug("Peer failure detection stopped");
		} else {
			LOG.debug("Tried to stop peer failure detection, but it was stopped already.");
		}
	}

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		
		// Preconditions
		if(!cP2PNetworkManager.isPresent()) {
			
			LOG.error("No P2P network manager bound!");
			return;
			
		}
		
		// Start listening to the network as soon as it's started
		if (failPeerListenerThread != null && !failPeerListenerThread.isAlive()) {
			failPeerListenerThread.setName("PeerFailureDetection_"
					+ cP2PNetworkManager.get().getLocalPeerName() + "_"
					+ cP2PNetworkManager.get().getLocalPeerID().toString());
			failPeerListenerThread.setDaemon(true);
			failPeerListenerThread.start();
		}
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
		
		failPeerListenerThread.activeInterrupt();
		failPeerListenerThread = null;

	}

}
