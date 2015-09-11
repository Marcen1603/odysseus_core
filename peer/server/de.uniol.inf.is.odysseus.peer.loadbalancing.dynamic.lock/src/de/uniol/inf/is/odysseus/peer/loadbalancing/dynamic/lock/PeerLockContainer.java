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

public class PeerLockContainer implements IMessageDeliveryFailedListener, IPeerCommunicatorListener {

	private boolean rollback = false;

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

	public PeerLockContainer(IPeerCommunicator communicator,IPeerDictionary peerDictionary, List<PeerID> peers, IPeerLockContainerListener callbackListener) {

		this.peerDictionary = peerDictionary;
		this.communicator = communicator;

		this.listeners = new ArrayList<IPeerLockContainerListener>();
		this.locks = new ConcurrentHashMap<PeerID, LOCK_STATE>();
		this.jobs = new ConcurrentHashMap<PeerID,RepeatingMessageSend>();
		

		listeners.add(callbackListener);

		for (PeerID peer : peers) {
			if (!locks.containsKey(peer)) {
				locks.put(peer, LOCK_STATE.unlocked);
			}
		}
	}

	private void clearJobs() {
		for(RepeatingMessageSend job : jobs.values()) {
			job.stopRunning();
			job.clearListeners();
		}
		jobs.clear();
	}
	
	public void requestLocks() {

		registerWithPeerCommunicator();

		for (PeerID peer : locks.keySet()) {
			if (locks.get(peer) == LOCK_STATE.unlocked) {
				locks.put(peer, LOCK_STATE.lock_requested);
				RepeatingMessageSend job = createLockRequest(peer);
				jobs.put(peer, job);
				job.start();
				LOG.debug("Sending Lock Request to Peer with ID {}",peerDictionary.getRemotePeerName(peer));
			}
		}

	}

	public void releaseLocks() {
		lockingPhaseFinished = true;
		for (PeerID peer : locks.keySet()) {
			if (locks.get(peer) != LOCK_STATE.unlocked) {
				locks.put(peer, LOCK_STATE.release_requested);
				RepeatingMessageSend job = createReleaseRequest(peer);
				jobs.put(peer, job);
				job.start();
				
				LOG.debug("Sending Release Lock Request to Peer with ID {}, current Lock status is {}",peerDictionary.getRemotePeerName(peer),locks.get(peer).toString());
			}
		}
	}

	private RepeatingMessageSend createLockRequest(PeerID peer) {
		RequestLockMessage message = new RequestLockMessage();
		RepeatingMessageSend job = new RepeatingMessageSend(communicator, message, peer);
		job.addListener(this);
		return job;
	}

	private RepeatingMessageSend createReleaseRequest(PeerID peer) {
		ReleaseLockMessage message = new ReleaseLockMessage();
		RepeatingMessageSend job = new RepeatingMessageSend(communicator, message, peer);
		job.addListener(this);
		return job;
	}

	@Override
	public void update(IMessage message, PeerID peerID) {
		// At least one Message failed to deliver.
		if (message instanceof RequestLockMessage) {
			LOG.error("Timeout while trying to lock Peer {}", peerDictionary.getRemotePeerName(peerID));
			synchronized(rollbackLock) {
				if (!rollback) {
					initiateRollback();
				}
			}
		}
		if (message instanceof ReleaseLockMessage) {
			// Timeout while releasing locks (maybe after an error).
			// Log error and go on (nothing we can do)
			LOG.error("Timeout while trying to release lock on Peer " + peerDictionary.getRemotePeerName(peerID));
			jobs.get(peerID).stopRunning();
			jobs.remove(peerID);
			locks.put(peerID, LOCK_STATE.timed_out);
			
			checkIfAllPeersUnlocked();
		}

	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {

		// Ignore if Peer is not known.
		if (!jobs.containsKey(senderPeer))
			return;
				
		if(!lockingPhaseFinished) {
			if (message instanceof LockGrantedMessage) {
				LOG.debug("Got Lock granted Message from Peer {}",peerDictionary.getRemotePeerName(senderPeer));
				if (locks.get(senderPeer) == LOCK_STATE.lock_requested) {
					locks.put(senderPeer, LOCK_STATE.locked);
					jobs.get(senderPeer).stopRunning();
					jobs.get(senderPeer).clearListeners();
					jobs.remove(senderPeer);
	
					// All peers locked?
					checkIfAllPeersLocked();
				}
			}
	
			if (message instanceof LockDeniedMessage) {
				if(locks.get(senderPeer) == LOCK_STATE.lock_requested) {
					locks.put(senderPeer, LOCK_STATE.unlocked);
					jobs.get(senderPeer).stopRunning();
					jobs.get(senderPeer).clearListeners();
					jobs.remove(senderPeer);
					synchronized (rollbackLock) {
						if (!rollback) {
							initiateRollback();
						}
					}
				}
			}
		}
		if(lockingPhaseFinished && !rleasingPhaseFinished) {
			
			if (message instanceof LockReleasedMessage) {
				if ((locks.get(senderPeer) == LOCK_STATE.release_requested) || (locks.get(senderPeer) == LOCK_STATE.locked)) {
					locks.put(senderPeer, LOCK_STATE.unlocked);
					jobs.get(senderPeer).stopRunning();
					jobs.get(senderPeer).clearListeners();
					jobs.remove(senderPeer);
					LOG.debug("Lock on peer {} released (Msg. received).",peerDictionary.getRemotePeerName(senderPeer));

					checkIfAllPeersUnlocked();
				}
				else {
					LOG.debug("Got LockReleaseMessage from Peer {} but LOCK_STATE is {}",peerDictionary.getRemotePeerName(senderPeer),locks.get(senderPeer));
				}
				
				
			}
			
			if(message instanceof LockNotReleasedMessage) {
				if (locks.get(senderPeer) == LOCK_STATE.release_requested) {
					LOG.error("Could not release Lock on Peer {}",peerDictionary.getRemotePeerName(senderPeer));
					locks.put(senderPeer, LOCK_STATE.timed_out);
					jobs.get(senderPeer).clearListeners();
					jobs.get(senderPeer).stopRunning();
					jobs.remove(senderPeer);
	
					checkIfAllPeersUnlocked();
				}
			
		}
		}

	}

	private void checkIfAllPeersLocked() {
		//If Peers already locked... This is a late message.
		if(lockingPhaseFinished) {
			return;
		}
		
		if (getNumberOfLockedPeers() == locks.size()) {
			if(!lockingPhaseFinished) {
				lockingPhaseFinished = true;
				LOG.debug("All affected Peers locked.");
				clearJobs();
				for (IPeerLockContainerListener listener : listeners) {
					listener.notifyLockingSuccessfull();
				}
			}
		}
	}

	private void checkIfAllPeersUnlocked() {
		// All Peers already unlocked?
		if(rleasingPhaseFinished) {
			return;
		}
		
		if (getNumberOfUnlockedPeers() == locks.size()) {
			if(!rleasingPhaseFinished) {
				rleasingPhaseFinished = true;
				LOG.debug("No more peers to unlock.");
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

	private int getNumberOfLockedPeers() {
		int i = 0;
		for (LOCK_STATE state : locks.values()) {
			if (state == LOCK_STATE.locked)
				i++;
		}
		return i;
	}

	private int getNumberOfUnlockedPeers() {
		int i = 0;
		for (LOCK_STATE state : locks.values()) {
			// Count timed_out as unlocked too, to avoid deadlock.
			if (state == LOCK_STATE.unlocked || state == LOCK_STATE.timed_out) {
				i++;
			}
		}
		return i;
	}

	private void initiateRollback() {
		// Stop all running Jobs.
		clearJobs();
		lockingPhaseFinished=true;
		LOG.error("Initiating Peer Lock Rollback.");
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
