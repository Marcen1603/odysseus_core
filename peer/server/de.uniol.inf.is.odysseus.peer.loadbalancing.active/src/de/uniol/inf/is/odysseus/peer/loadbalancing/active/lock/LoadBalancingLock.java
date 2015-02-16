package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;

public class LoadBalancingLock implements IPeerCommunicatorListener, ILoadBalancingLock{

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LoadBalancingLock.class);
	
	
	private boolean lock=false;
	
	private IPeerCommunicator communicator;
	private IPeerDictionary peerDictionary;
	private IP2PNetworkManager networkManager;
	
	private PeerID lockedForPeer=null;
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		
		if(message instanceof RequestLockMessage) {
			if(modifyLock(true,senderPeer)) {
				sendLockGranted(senderPeer);
			}
			else {
				sendLockDenied(senderPeer);
			}
		}
		
		if(message instanceof ReleaseLockMessage) {
			if(modifyLock(false,senderPeer)) {
				sendLockReleased(senderPeer);
			} else {
				sendLockNotReleased(senderPeer);
			}
		}
	}
	
	public boolean requestLocalLock() {
		PeerID localPeerID = networkManager.getLocalPeerID();
		return modifyLock(true,localPeerID);
	}
	
	public boolean releaseLocalLock() {
		PeerID localPeerID = networkManager.getLocalPeerID();
		return modifyLock(false,localPeerID);
	}
	
	private synchronized boolean modifyLock(boolean newLock,PeerID requestingPeer) {
		//Trying to lock
		if(newLock) {
			//Currently locked.
			if(lock) {
				if(requestingPeer==lockedForPeer || !isPeerKnown(lockedForPeer)) {
					lock = true;
					LOG.debug("Peer locked.");
					lockedForPeer = requestingPeer;
					return true;
				}
				return false;
			}
			//Currently unlocked.
			else {
				lock=true;
				lockedForPeer = requestingPeer;
				return true;
			}
		}
		//Trying to unlock.
		else {
			//Currently locked.
			if(lock) {
				if(requestingPeer==lockedForPeer || !isPeerKnown(lockedForPeer)) {
					LOG.debug("Lock released.");
					lock=false;
					lockedForPeer=null;
					return true;
				}
				return false;
			}
			//Currently unlocked.
			else {
				return true;
			}
		}
		
	}
	
	
	private void sendLockGranted(PeerID peerID) {
		LockGrantedMessage message = new LockGrantedMessage();
		try {
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockGranted message.");
		}
		
	}
	
	private void sendLockDenied(PeerID peerID) {
		LockDeniedMessage message = new LockDeniedMessage();
		try {
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockGranted message.");
		}
	}
	
	private void sendLockReleased(PeerID peerID) {
		LockReleasedMessage message = new LockReleasedMessage();
		try {
			communicator.send(peerID, message);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send LockReleased message.");
		}
	}
	
	private void sendLockNotReleased(PeerID peerID) {
		LockNotReleasedMessage message = new LockNotReleasedMessage();
		try {
			communicator.send(peerID,message);
		}
		catch (PeerCommunicationException e) {
			LOG.error("Could not send LockNotReleasedMessage.");
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
		communicator.removeListener(this, ReleaseLockMessage.class);
		communicator.unregisterMessageType(ReleaseLockMessage.class);
		
		communicator.removeListener(this, RequestLockMessage.class);
		communicator.unregisterMessageType(RequestLockMessage.class);
		
		communicator.unregisterMessageType(LockReleasedMessage.class);
		communicator.unregisterMessageType(LockGrantedMessage.class);
		communicator.unregisterMessageType(LockNotReleasedMessage.class);
		communicator.unregisterMessageType(LockDeniedMessage.class);
		
		if(this.communicator == communicator) {
			this.communicator = null;
		}
	}
	
	public void bindNetworkManager(IP2PNetworkManager networkManager) {
		this.networkManager = networkManager;
	}
	
	public void unbindNetworkManager(IP2PNetworkManager networkManager) {
		if(this.networkManager==networkManager) {
			this.networkManager = null;
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary dict) {
		this.peerDictionary = dict;
	}
	
	public void unbindPeerDictionary(IPeerDictionary dict) {
		if(this.peerDictionary == dict) {
			this.peerDictionary = null;
		}
	}
	
	private boolean isPeerKnown(PeerID peer) {
		if(peerDictionary.getRemotePeerIDs().contains(peer) || networkManager.getLocalPeerID()==peer) {
			return true;
		}
		LOG.debug("Peer ID " + lockedForPeer + " not known any more.");
		return false;
	}

}
