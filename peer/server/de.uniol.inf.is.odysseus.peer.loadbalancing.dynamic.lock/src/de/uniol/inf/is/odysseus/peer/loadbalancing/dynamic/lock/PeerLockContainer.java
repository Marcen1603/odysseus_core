package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.common.RepeatingMessageSend;

/**
 * Coordinates Locking of Peers.
 * @author Carsten Cordes
 *
 */
public class PeerLockContainer implements IMessageDeliveryFailedListener, IPeerCommunicatorListener {

	private boolean rollback = false;

	private int lockingID;
	
	private static enum LOCK_STATE {
		unlocked, lock_requested, locked, release_requested, timed_out, blocked
	}

	private List<IPeerLockContainerListener> listeners;
	private ConcurrentHashMap<PeerID, LOCK_STATE> locks;
	private ConcurrentHashMap<PeerID, RepeatingMessageSend> jobs;
	

	private IPeerCommunicator communicator;
	private IPeerDictionary peerDictionary;
	
	
	private boolean lockingPhaseFinished = false;
	private boolean rleasingPhaseFinished = false;
	
	
	private Object rollbackLock = new Object();
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PeerLockContainer.class);

	/**
	 * Constructor
	 * @param communicator PeerCommunicator to use
	 * @param peerDictionary PeerDictionary to use
	 * @param peers list of Peers that should be locked
	 * @param callbackListener Callback that is notified of Locking success or failure
	 * @param lockingID Unique Locking ID to synchronized locking Process.
	 */
	public PeerLockContainer(IPeerCommunicator communicator,IPeerDictionary peerDictionary, List<PeerID> peers, IPeerLockContainerListener callbackListener, int lockingID) {

		this.peerDictionary = peerDictionary;
		this.communicator = communicator;

		this.listeners = new ArrayList<IPeerLockContainerListener>();
		this.locks = new ConcurrentHashMap<PeerID, LOCK_STATE>();
		this.jobs = new ConcurrentHashMap<PeerID,RepeatingMessageSend>();
		this.lockingID = lockingID;

		listeners.add(callbackListener);

		for (PeerID peer : peers) {
			if (!locks.containsKey(peer)) {
				locks.put(peer, LOCK_STATE.unlocked);
			}
		}
	}

	private void clearJobs() {
		for(RepeatingMessageSend job : jobs.values()) {

			job.clearListeners();
			job.stopRunning();
			
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("Locking ID {}",lockingID);
			LOG.debug("Clearing Jobs. Lock states:");
			for(PeerID peer : locks.keySet()) {
				LOG.debug("{} : {}",peer,locks.get(peer));
			}
		}
		
		jobs.clear();
	
	}
	
	/**
	 * start Requesting Locks
	 */
	public void requestLocks() {

		registerWithPeerCommunicator();

		for (PeerID peer : locks.keySet()) {
			if (locks.get(peer) == LOCK_STATE.unlocked) {
				locks.put(peer, LOCK_STATE.lock_requested);
				RepeatingMessageSend job = createLockRequest(peer);
				jobs.put(peer, job);
				job.start();
				LOG.debug("Sending Lock Request to Peer with ID {}",peerDictionary.getRemotePeerName(peer));
				LOG.debug("Locking ID {}",lockingID);
			}
		}

	}

	/**
	 * Start releasing Locks.
	 */
	public void releaseLocks() {
		lockingPhaseFinished = true;
		for (PeerID peer : locks.keySet()) {
			int counter = 0;
			if (locks.get(peer) != LOCK_STATE.blocked) {
				counter++;
				locks.put(peer, LOCK_STATE.release_requested);
				RepeatingMessageSend job = createReleaseRequest(peer);
				jobs.put(peer, job);
				job.start();
				
				LOG.debug("Sending Release Lock Request to Peer with ID {}, current Lock status is {}",peerDictionary.getRemotePeerName(peer),locks.get(peer).toString());
				LOG.debug("Locking ID {}",lockingID);
			}
			if(counter == 0) {
				LOG.debug("No Peers to unlock. Maybe all of them are blocked?");
				checkIfAllPeersUnlocked();
			}
		}
	}

	private RepeatingMessageSend createLockRequest(PeerID peer) {
		RequestLockMessage message = new RequestLockMessage(lockingID);
		RepeatingMessageSend job = new RepeatingMessageSend(communicator, message, peer);
		job.addListener(this);
		return job;
	}

	private RepeatingMessageSend createReleaseRequest(PeerID peer) {
		ReleaseLockMessage message = new ReleaseLockMessage(lockingID);
		RepeatingMessageSend job = new RepeatingMessageSend(communicator, message, peer);
		job.addListener(this);
		return job;
	}

	/**
	 * @see de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.common.IMessageDeliveryFailedListener#update(de.uniol.inf.is.odysseus.peer.communication.IMessage, net.jxta.peer.PeerID)
	 */
	@Override
	public void update(IMessage message, PeerID peerID) {
		// At least one Message failed to deliver.
		if (message instanceof RequestLockMessage) {
			LOG.error("Timeout while trying to lock Peer {} locking ID: {}", peerDictionary.getRemotePeerName(peerID), lockingID);
			synchronized(rollbackLock) {
				if (!rollback) {
					initiateRollback();
				}
			}
		}
		if (message instanceof ReleaseLockMessage) {
			// Timeout while releasing locks (maybe after an error).
			// Log error and go on (nothing we can do)
			LOG.error("Timeout while trying to release lock on Peer " + peerDictionary.getRemotePeerName(peerID) + " locking ID: " + lockingID); 
			jobs.get(peerID).stopRunning();
			jobs.remove(peerID);
			locks.put(peerID, LOCK_STATE.timed_out);
			
			checkIfAllPeersUnlocked();
		}

	}

	/**
	 * @see de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener#receivedMessage(de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator, net.jxta.peer.PeerID, de.uniol.inf.is.odysseus.peer.communication.IMessage)
	 */
	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {

		// Ignore if Peer is not known.
		if (!jobs.containsKey(senderPeer)) {
			LOG.warn("Received message from Peer which is not in Jobs Map: {}",peerDictionary.getRemotePeerName(senderPeer));
			return;
		}
		
		if(!lockingPhaseFinished) {
			if (message instanceof LockGrantedMessage) {
				if  (((LockGrantedMessage)message).getLockingID()!=lockingID) {

					LOG.debug("Got LockGrantedMessage from {}, but with wrong Locking ID: {}",peerDictionary.getRemotePeerName(senderPeer), lockingID);
					return;
				}
				
				LOG.debug("Got Lock granted Message from Peer {} locking ID is {}",peerDictionary.getRemotePeerName(senderPeer),lockingID);
				if (locks.get(senderPeer) == LOCK_STATE.lock_requested) {
					locks.put(senderPeer, LOCK_STATE.locked);
					jobs.get(senderPeer).stopRunning();
					jobs.get(senderPeer).clearListeners();
					jobs.remove(senderPeer);
	
					// All peers locked?
					checkIfAllPeersLocked();
				}
				return;
			}
	
			if (message instanceof LockDeniedMessage) {
				if  (((LockDeniedMessage)message).getLockingID()!=lockingID) {

					LOG.debug("Got LockDeniedMessage from {}, but with wrong Locking ID: {}",peerDictionary.getRemotePeerName(senderPeer), lockingID);
					return;
				}
				if(locks.get(senderPeer) == LOCK_STATE.lock_requested) {
					LOG.debug("Got Lock granted Message from Peer {} locking ID is {}",peerDictionary.getRemotePeerName(senderPeer),lockingID);
					if (locks.get(senderPeer) == LOCK_STATE.lock_requested) {
						locks.put(senderPeer, LOCK_STATE.blocked);
						jobs.get(senderPeer).stopRunning();
						jobs.get(senderPeer).clearListeners();
						jobs.remove(senderPeer);
		
						// All peers locked?
						checkIfAllPeersLocked();
					}
					return;
				}
				return;
			}
		}
		if(lockingPhaseFinished && !rleasingPhaseFinished) {
			
			if (message instanceof LockReleasedMessage) {
				if  (((LockReleasedMessage)message).getLockingID()!=lockingID) {
					LOG.debug("Got LockReleased from {}, but with wrong Locking ID: {}",peerDictionary.getRemotePeerName(senderPeer), ((LockReleasedMessage) message).getLockingID());
					return;
				}
				if ((locks.get(senderPeer) == LOCK_STATE.release_requested) || (locks.get(senderPeer) == LOCK_STATE.locked)) {
					locks.put(senderPeer, LOCK_STATE.unlocked);
					jobs.get(senderPeer).stopRunning();
					jobs.get(senderPeer).clearListeners();
					jobs.remove(senderPeer);
					LOG.debug("Lock on peer {} released (Msg. received). Locking ID is {}",peerDictionary.getRemotePeerName(senderPeer), lockingID);

					checkIfAllPeersUnlocked();
				}
				else {
					LOG.debug("Got LockReleaseMessage from Peer {} but LOCK_STATE is {}",peerDictionary.getRemotePeerName(senderPeer),locks.get(senderPeer));
				}
				return;
				
			}
			
			if(message instanceof LockNotReleasedMessage) {
				if  (((LockNotReleasedMessage)message).getLockingID()!=lockingID) {

					LOG.debug("Got LockNotReleased from {}, but with wrong Locking ID: {}",peerDictionary.getRemotePeerName(senderPeer), lockingID);
					return;
				}
				if (locks.get(senderPeer) == LOCK_STATE.release_requested  || (locks.get(senderPeer) == LOCK_STATE.locked)) {
					LOG.error("Could not release Lock on Peer {} lockingID: {}",peerDictionary.getRemotePeerName(senderPeer),lockingID);
					locks.put(senderPeer, LOCK_STATE.timed_out);
					jobs.get(senderPeer).clearListeners();
					jobs.get(senderPeer).stopRunning();
					jobs.remove(senderPeer);
	
					checkIfAllPeersUnlocked();
				}
			return;
		}
		}

	}

	private void checkIfAllPeersLocked() {
		//If Peers already locked... This is a late message.
		if(lockingPhaseFinished) {
			return;
		}
		
		if (getNumberOfLockingRequests() == 0) {
			if(!lockingPhaseFinished) {
				lockingPhaseFinished = true;
				if(checkIfLockingSuccessful()) {
					LOG.debug("All affected Peers locked. (Locking Id:{})",lockingID);
					clearJobs();
					for (IPeerLockContainerListener listener : listeners) {
						listener.notifyLockingSuccessfull();
					}
				}
				else {
					LOG.debug("Locking was not successful.");
					initiateRollback();
				}
			}
		}
	}

	private boolean checkIfLockingSuccessful() {
		int i=0;
		for(PeerID peer : locks.keySet()) {
			if(locks.get(peer) == LOCK_STATE.locked) {
				i++;
			}
		}
		
		if(i==locks.size()) {
			return true;
		} else {
			return false;
		}
		
	}

	private void checkIfAllPeersUnlocked() {
		// All Peers already unlocked?
		if(rleasingPhaseFinished) {
			return;
		}
		
		if (getNumberOfReleaseRequests() == 0) {
			if(!rleasingPhaseFinished) {
				rleasingPhaseFinished = true;
				LOG.debug("No more peers to unlock. (Locking ID: {})",lockingID);
				clearJobs();
				unregisterFromPeerCommunicator();
				for (IPeerLockContainerListener listener : listeners) {
					if (rollback) {
						listener.notifyLockingFailed();
					} else {
						listener.notifyReleasingFinished();
					}
					
				}
			}
		}
	}

	private int getNumberOfLockingRequests() {
		int i = 0;
		for (LOCK_STATE state : locks.values()) {
			if (state == LOCK_STATE.lock_requested)
				i++;
		}
		return i;
	}

	private int getNumberOfReleaseRequests() {
		int i = 0;
		for (LOCK_STATE state : locks.values()) {
			// Count timed_out as unlocked too, to avoid deadlock.
			if (state == LOCK_STATE.release_requested) {
				i++;
			}
		}
		return i;
	}

	private void initiateRollback() {
		// Stop all running Jobs.
		lockingPhaseFinished=true;
		clearJobs();
		LOG.error("Initiating Peer Lock Rollback. Locking ID {}",lockingID);
		rollback = true;
		releaseLocks();

	}

	private void registerWithPeerCommunicator() {
		LOG.info("Registering PeerLock Container");
		communicator.addListener(this, LockDeniedMessage.class);
		communicator.addListener(this, LockGrantedMessage.class);
		communicator.addListener(this, LockReleasedMessage.class);
		communicator.addListener(this, LockNotReleasedMessage.class);
	}

	private void unregisterFromPeerCommunicator() {
		LOG.info("Unregistering PeerLock Container");
		communicator.removeListener(this, LockReleasedMessage.class);
		communicator.removeListener(this, LockGrantedMessage.class);
		communicator.removeListener(this, LockDeniedMessage.class);
		communicator.removeListener(this, LockNotReleasedMessage.class);
	}

}
