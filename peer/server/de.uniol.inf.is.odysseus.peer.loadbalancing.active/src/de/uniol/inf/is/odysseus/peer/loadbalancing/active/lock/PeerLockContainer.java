package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.IMessageDeliveryFailedListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.RepeatingMessageSend;

public class PeerLockContainer implements IMessageDeliveryFailedListener, IPeerCommunicatorListener {
	
	private boolean rollback = false;
	
	
	private static enum LOCK_STATE {
		unlocked,lock_requested,locked,release_requested,could_not_release,blocked
	}
	
	private List<IPeerLockContainerListener> listeners;
	private ConcurrentHashMap<PeerID,LOCK_STATE> locks;
	private ConcurrentHashMap<PeerID,RepeatingMessageSend> jobs;
	
	private IPeerCommunicator communicator;
	
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PeerLockContainer.class);
	
	
	public PeerLockContainer(IPeerCommunicator communicator,List<PeerID> peers, IPeerLockContainerListener callbackListener) {
		
		this.communicator = communicator;
		
		this.listeners = new ArrayList<IPeerLockContainerListener>();
		this.locks = new ConcurrentHashMap<PeerID,LOCK_STATE>();
		
		listeners.add(callbackListener);
		
		for(PeerID peer : peers) {
			if(!locks.containsKey(peer)) {
				locks.put(peer,LOCK_STATE.unlocked);
			}
		}
	}
	
	
	
	
	
	public void requestLocks() {
		if(jobs==null) {
			jobs = new ConcurrentHashMap<PeerID,RepeatingMessageSend>();
		}
		registerWithPeerCommunicator();
		
		for (PeerID peer : locks.keySet()) {
			if(locks.get(peer)==LOCK_STATE.unlocked) {
				locks.put(peer, LOCK_STATE.lock_requested);
				RepeatingMessageSend job = createLockRequest(peer);
				jobs.put(peer,job);
				job.start();
			}
		}
		
	}
	
	public void releaseLocks() {
		for (PeerID peer : locks.keySet()) {
			if(locks.get(peer)!=LOCK_STATE.unlocked) {
				locks.put(peer, LOCK_STATE.release_requested);
				RepeatingMessageSend job = createReleaseRequest(peer);
				jobs.put(peer, job);
				job.start();
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

	
	
	public void update(IMessage message, PeerID peerID) {
		//At least one Message failed to deliver.
		if(message instanceof RequestLockMessage) {
			
			
		}
		if(message instanceof ReleaseLockMessage) {
			//Timeout while releasing locks (maybe after an error).
			//Log error and go on (nothing we can do)
			LOG.error("Timeout while trying to release lock on Peer with PeerID " + peerID);
			jobs.get(peerID).stopRunning();
			jobs.remove(peerID);
			locks.put(peerID, LOCK_STATE.could_not_release);
			if(getNumberOfUnlockedPeers()==locks.size()) {
				LOG.debug("No more peers to unlock.");
				for(IPeerLockContainerListener listener : listeners) {
					if(rollback) {
						listener.notifyLockingFailed();
					}
					else {
						listener.notifyReleasingFinished();
					}
					unregisterFromPeerCommunicator();
				}
			}
		}
		
		
	}


	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		//Ignore if Peer is not known.
		if(!locks.containsKey(senderPeer))
			return;
		
		if(message instanceof LockGrantedMessage) {
			if(locks.get(senderPeer)==LOCK_STATE.lock_requested) {
				locks.put(senderPeer, LOCK_STATE.locked);
				jobs.get(senderPeer).stopRunning();
				jobs.remove(senderPeer);
				
				//All peers locked?
				if(getNumberOfLockedPeers()==locks.size()) {
					LOG.debug("All affected Peers locked.");
					for(IPeerLockContainerListener listener : listeners) {
						listener.notifyLockingSuccessfull();
					}
				}
			}
		}
		
		if(message instanceof LockDeniedMessage) {
			if(!rollback) {
				initiateRollback();
			}
		}
		
		if(message instanceof LockReleasedMessage) {
			if(locks.get(senderPeer)==LOCK_STATE.release_requested) {
				locks.put(senderPeer, LOCK_STATE.unlocked);
				jobs.get(senderPeer).stopRunning();
				jobs.remove(senderPeer);
				
				
				//All Peers unlocked?
				if(getNumberOfUnlockedPeers()==locks.size()) {
					LOG.debug("No more peers to unlock.");
					for(IPeerLockContainerListener listener : listeners) {
						listener.notifyReleasingFinished();
					}
				}
			}
		}
		
		if(message instanceof LockNotReleasedMessage) {
			if(locks.get(senderPeer)==LOCK_STATE.release_requested) {
				LOG.debug("Could not release lock on Peer id " + senderPeer);
				locks.put(senderPeer, LOCK_STATE.could_not_release);
				jobs.get(senderPeer).stopRunning();
				jobs.remove(senderPeer);
				
				
				//All Peers unlocked?
				if(getNumberOfUnlockedPeers()==locks.size()) {
					LOG.debug("No more peers to unlock.");
					for(IPeerLockContainerListener listener : listeners) {
						listener.notifyReleasingFinished();
					}
				}
			}
		}
		
	}
	
	private int getNumberOfLockedPeers() {
		int i=0;
		for(LOCK_STATE state: locks.values()) {
			if(state==LOCK_STATE.locked)
				i++;
		}
		return i;
	}
	
	private int getNumberOfUnlockedPeers() {
		int i=0;
		for(LOCK_STATE state:locks.values()) {
			//Count timed_out as unlocked too, to avoid deadlock.
			if(state==LOCK_STATE.unlocked || state==LOCK_STATE.could_not_release) {
				i++;
			}
		}
		return i;
	}
	
	
	private void initiateRollback() {
		//Stop all running Jobs.
		rollback = true;
		for (RepeatingMessageSend job : jobs.values()) {
			job.stopRunning();
		}
		jobs.clear();
		releaseLocks();
		
		
	}
	
	private void registerWithPeerCommunicator() {
		
		communicator.registerMessageType(ReleaseLockMessage.class);
		communicator.registerMessageType(RequestLockMessage.class);
		
		communicator.registerMessageType(LockDeniedMessage.class);
		communicator.addListener(this, LockDeniedMessage.class);
		
		communicator.registerMessageType(LockGrantedMessage.class);
		communicator.addListener(this,LockGrantedMessage.class);
		
		communicator.registerMessageType(LockReleasedMessage.class);
		communicator.addListener(this, LockReleasedMessage.class);
		
		communicator.registerMessageType(LockNotReleasedMessage.class);
		communicator.addListener(this, LockNotReleasedMessage.class);
	}
	
	private void unregisterFromPeerCommunicator() {
		
		communicator.registerMessageType(ReleaseLockMessage.class);
		communicator.registerMessageType(RequestLockMessage.class);
		
		communicator.removeListener(this, LockNotReleasedMessage.class);
		communicator.unregisterMessageType(LockNotReleasedMessage.class);
		
		communicator.removeListener(this, LockReleasedMessage.class);
		communicator.unregisterMessageType(LockReleasedMessage.class);
		
		communicator.removeListener(this, LockGrantedMessage.class);
		communicator.unregisterMessageType(LockGrantedMessage.class);
		
		communicator.removeListener(this, LockDeniedMessage.class);
		communicator.unregisterMessageType(LockDeniedMessage.class);
	}

}
