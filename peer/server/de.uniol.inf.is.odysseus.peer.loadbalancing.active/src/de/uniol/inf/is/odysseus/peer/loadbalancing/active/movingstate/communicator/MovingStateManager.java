package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import de.uniol.inf.is.odysseus.peer.transmission.DataTransmissionException;

/**
 * Manages State-Senders and receivers (Singleton)
 * @author Carsten Cordes
 *
 */
public class MovingStateManager {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateManager.class);
	
	/**
	 * Stores senders
	 */
	private ConcurrentHashMap<String,MovingStateSender> senderMap;
	
	/***
	 * Stores receivers
	 */
	private ConcurrentHashMap<String,MovingStateReceiver> receiverMap;
	
	/***
	 * Instance variable
	 */
	private static MovingStateManager instance;
	
	/**
	 * Constructor -> use getInstance()
	 */
	private MovingStateManager() {
		senderMap = new ConcurrentHashMap<String,MovingStateSender>();
		receiverMap = new ConcurrentHashMap<String,MovingStateReceiver>();
	}
	
	/***
	 * Returns instance
	 * @return instance
	 */
	public synchronized static MovingStateManager getInstance() {
		if(instance==null) {
			instance = new MovingStateManager();
		}
		return instance;
	}
	
	/***
	 * Adds new sender to particular peer
	 * @param peerID Peer ID to which the sender should point
	 * @return pipeID of Sender.
	 */
	public synchronized String addSender(String peerID) {
		
		String newPipeID = IDFactory.newPipeID(
				OsgiServiceManager.getP2pNetworkManager().getLocalPeerGroupID()).toString();
		
		try {
			
			MovingStateSender sender = new MovingStateSender(peerID,newPipeID);
			senderMap.put(newPipeID, sender);
			LOG.debug("New State Sender with pipe ID {} created",newPipeID);
			return newPipeID;
			
		} catch (DataTransmissionException e) {
			LOG.error("Could not create MovingStateSender");
			e.printStackTrace();
			return "";
		}
		
	}
	
	/**
	 * Tries to find receiver by pipe
	 * @param pipeId pipeID to look for
	 * @return true is pipeID is key of receiverMap
	 */
	public boolean isReceiverPipeKnown(String pipeId) {
		return receiverMap.containsKey(pipeId);
	}
	
	/**
	 * Tries to find sender by pipe
	 * @param pipeId pipeID to look for
	 * @return true is pipeID is key of senderMap
	 */
	public boolean isSenderPipeKnown(String pipeId) {
		return senderMap.containsKey(pipeId);
	}
	
	/***
	 * Adds new receiver
	 * @param peerID PeerID with which the receiver should correspond
	 * @param pipeID pipe ID the receiver should use.
	 */
	public synchronized void addReceiver(String peerID, String pipeID) {
		try {
			receiverMap.putIfAbsent(pipeID, new MovingStateReceiver(peerID,pipeID));
			
		} catch (DataTransmissionException e) {
			LOG.error("Could not create MovingStateReceiver");
			e.printStackTrace();
		}
	}
	
	/***
	 * Returns sender for pipeID
	 * @param pipeID pipeID to look for
	 * @return MovingStateSender
	 */
	public MovingStateSender getSender(String pipeID) {
		return senderMap.get(pipeID);
	}

	/***
	 * Returns receiver for pipeID
	 * @param pipeId pipeId to look for
	 * @return MovingStateReceiver
	 */
	public MovingStateReceiver getReceiver(String pipeId) {
		return receiverMap.get(pipeId);
	}
}
