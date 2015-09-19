package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class LoadBalancingLock implements IPeerCommunicatorListener, ILoadBalancingLock {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancingLock.class);

	private boolean lock = false;

	private IPeerCommunicator communicator;
	private IPeerDictionary peerDictionary;
	private IP2PNetworkManager networkManager;
	
	private Integer currentLockingId = 0;
	
	private final Object lockModificationLock = new Object();
	
	private PeerID lockedForPeer = null;
	
	private ArrayList<ILoadBalancingLockListener> listeners = Lists.newArrayList();
	
	private ConcurrentHashMap<PeerID,Integer> seenLockingIDs = new ConcurrentHashMap<PeerID,Integer>();

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {

		
		if (message instanceof RequestLockMessage) {
			LOG.debug("Got RequestLock message.");
			int lockingIDFromMessage = ((RequestLockMessage)message).getLockingID();
			if(!isMessageValid(senderPeer, lockingIDFromMessage)) {
				LOG.debug("Got invalid message from Peer {} Locking ID {}",senderPeer,lockingIDFromMessage);
				return;
			}
			
			if (modifyLock(true, senderPeer)) {
				sendLockGranted(senderPeer,lockingIDFromMessage);
			} else {
				sendLockDenied(senderPeer,lockingIDFromMessage);
			}
		}

		if (message instanceof ReleaseLockMessage) {
			int lockingIDFromMessage = ((ReleaseLockMessage)message).getLockingID();
			if(!isMessageValid(senderPeer,  lockingIDFromMessage)) {
				LOG.debug("Got invalid message from Peer {} Locking ID {}",senderPeer,lockingIDFromMessage);
				return;
			}
			
			LOG.debug("Got ReleaseLock message.");
			if (modifyLock(false, senderPeer)) {
				sendLockReleased(senderPeer,lockingIDFromMessage);
			} else {
				sendLockNotReleased(senderPeer,lockingIDFromMessage);
			}
		}
		notifyListeners();
	}

	@Override
	public boolean requestLocalLock() {
		PeerID localPeerID = networkManager.getLocalPeerID();
		boolean lockResult = modifyLock(true, localPeerID);
		notifyListeners();
		return lockResult;
	}

	@Override
	public boolean releaseLocalLock() {
		PeerID localPeerID = networkManager.getLocalPeerID();
		boolean lockResult =  modifyLock(false, localPeerID);
		if(lockResult==false) {
			LOG.debug("Locked for Peer {}",peerDictionary.getRemotePeerName(lockedForPeer));
		}
		notifyListeners();
		return lockResult;
	}

	private boolean modifyLock(boolean newLock, PeerID requestingPeer) {
		if(LOG.isDebugEnabled()) {
			if(newLock) {
				LOG.debug("Peer {} is requesting Lock",peerDictionary.getRemotePeerName(requestingPeer));
			}
			else {
				LOG.debug("Peer {} is requesting Release of Lock",peerDictionary.getRemotePeerName(requestingPeer));
			}
		}
		// Trying to lock
		if (newLock) {
			// Currently locked.
			if (lock) {
				if (requestingPeer == lockedForPeer) {
					LOG.debug("Still locked.");
					return true;
				}
				if(!isPeerKnown(lockedForPeer)) {
					synchronized(lockModificationLock) {
						lock = true;
						lockedForPeer = requestingPeer;
					}
					LOG.debug("Peer locked.");
					return true;
				}
				if(requestingPeer==networkManager.getLocalPeerID()) {
					LOG.debug("Could not lock for local Peer");
				}
				else {
					LOG.debug("Could not lock for Peer {}",requestingPeer);
				}
				if(lockedForPeer==networkManager.getLocalPeerID()) {
					LOG.debug("Still locked for local Peer.");
				}
				else {
					LOG.debug("Still locked for {}",peerDictionary.getRemotePeerName(lockedForPeer));
				}
				return false;
			}
			// Currently unlocked.
			synchronized(lockModificationLock) {
				lock = true;
				lockedForPeer = requestingPeer;
			}
			LOG.debug("Peer locked.");
			return true;
		}

		// Trying to unlock.
		// Currently locked.
		if (lock) {
			if (requestingPeer == lockedForPeer || !isPeerKnown(lockedForPeer)) {
				synchronized(lockModificationLock) {
					lock = false;
					lockedForPeer = null;
				}
				LOG.debug("Lock released.");
				return true;
			}
			
			if(LOG.isDebugEnabled()) {
				if(requestingPeer==networkManager.getLocalPeerID()) {
					LOG.debug("Could not unlock for local Peer");
				}
				else {
					LOG.debug("Could not unlock for Peer {}",requestingPeer);
				}
				if(lockedForPeer==networkManager.getLocalPeerID()) {
					LOG.debug("Still locked for local Peer.");
				}
				else {
					LOG.debug("Still locked for {}",peerDictionary.getRemotePeerName(lockedForPeer));
				}
			}
			
			return false;
		}
		// Currently unlocked.
		return true;
	}
	
	private void notifyListeners() {
		try {
			for(ILoadBalancingLockListener listener : listeners) {
				listener.notifyLockStatusChanged(isLocked());
			}
		}
		catch(Exception e) {
			LOG.error("Uncaught Exception when trying to notify listeners: {}",e.getMessage());
		}
	}

	private void sendLockGranted(PeerID peerID,int lockingID) {
		LockGrantedMessage message = new LockGrantedMessage(lockingID);
		try {
			LOG.debug("Sending Lock Granted to Peer {}",peerDictionary.getRemotePeerName(peerID));
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockGranted message: {}",e.getMessage());
		}

	}

	private void sendLockDenied(PeerID peerID,int lockingID) {
		LockDeniedMessage message = new LockDeniedMessage(lockingID);
		LOG.debug("Sending Lock Denied to Peer {}",peerDictionary.getRemotePeerName(peerID));
		try {
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockDenied message: {}",e.getMessage());
		}
	}

	private void sendLockReleased(PeerID peerID,int lockingID) {
		LockReleasedMessage message = new LockReleasedMessage(lockingID);
		try {
			LOG.debug("Sending LockReleased to Peer {}",peerDictionary.getRemotePeerName(peerID));
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockReleased message: {}",e.getMessage());
		}
	}

	private void sendLockNotReleased(PeerID peerID,int lockingID) {

		LOG.debug("Sending LockNotReleased to Peer {}",peerDictionary.getRemotePeerName(peerID));
		LockNotReleasedMessage message = new LockNotReleasedMessage(lockingID);
		try {
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockNotReleasedMessage: {}",e.getMessage());
		}
	}

	public void bindPeerCommunicator(IPeerCommunicator communicator) {
		this.communicator = communicator;
		communicator.registerMessageType(RequestLockMessage.class);
		communicator.addListener(this, RequestLockMessage.class);

		communicator.registerMessageType(ReleaseLockMessage.class);
		communicator.addListener(this, ReleaseLockMessage.class);

		communicator.registerMessageType(LockReleasedMessage.class);
		communicator.registerMessageType(LockGrantedMessage.class);
		communicator.registerMessageType(LockNotReleasedMessage.class);
		communicator.registerMessageType(LockDeniedMessage.class);

	}

	public void unbindPeerCommunicator(IPeerCommunicator communicator) {

		if (this.communicator == communicator) {
			communicator.removeListener(this, ReleaseLockMessage.class);
			communicator.unregisterMessageType(ReleaseLockMessage.class);
	
			communicator.removeListener(this, RequestLockMessage.class);
			communicator.unregisterMessageType(RequestLockMessage.class);
	
			communicator.unregisterMessageType(LockReleasedMessage.class);
			communicator.unregisterMessageType(LockGrantedMessage.class);
			communicator.unregisterMessageType(LockNotReleasedMessage.class);
			communicator.unregisterMessageType(LockDeniedMessage.class);

			this.communicator = null;
		}
	}

	public void bindNetworkManager(IP2PNetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void unbindNetworkManager(IP2PNetworkManager networkManager) {
		if (this.networkManager == networkManager) {
			this.networkManager = null;
		}
	}

	public void bindPeerDictionary(IPeerDictionary dict) {
		this.peerDictionary = dict;
	}

	public void unbindPeerDictionary(IPeerDictionary dict) {
		if (this.peerDictionary == dict) {
			this.peerDictionary = null;
		}
	}

	private boolean isPeerKnown(PeerID peer) {
		if (peerDictionary.getRemotePeerIDs().contains(peer) || networkManager.getLocalPeerID() == peer) {
			return true;
		}
		LOG.debug("Peer ID " + lockedForPeer + " not known any more.");
		return false;
	}

	@Override
	public boolean isLocked() {
		return lock;
	}
	
	@Override
	public String getLockingPeerID() {
		return lockedForPeer.toString();
	}

	@Override
	public void forceUnlock() {
		modifyLock(false,lockedForPeer);
		notifyListeners();
	}

	@Override
	public void addListener(ILoadBalancingLockListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
		
	}

	@Override
	public void removeListener(ILoadBalancingLockListener listener) {
		if(listeners.contains(listener)) {
			listeners.remove(listener);
		}
		
	}

	@Override
	public synchronized int getNewLockingId() {
		return currentLockingId++;
	}
	
	private boolean isMessageValid(PeerID peer, Integer lockingID) {
		if(!seenLockingIDs.contains(peer)){
			seenLockingIDs.put(peer, lockingID);
			return true;
		}
		
		Integer currentLockingIDForPeer =  seenLockingIDs.get(peer);
		

		if(lockingID == currentLockingIDForPeer) {
			return true;
		}
		
		if(lockingID < currentLockingIDForPeer) {
			return false;
		}
		
		if(lockingID > currentLockingIDForPeer) {
			seenLockingIDs.put(peer, lockingID);
			return true;
		}
		
		return true;
		
		
	}

}
