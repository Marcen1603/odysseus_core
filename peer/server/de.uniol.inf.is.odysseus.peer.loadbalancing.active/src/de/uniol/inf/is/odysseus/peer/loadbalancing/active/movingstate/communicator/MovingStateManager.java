package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.OsgiServiceManager;
import net.jxta.id.IDFactory;

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
	private HashMap<String,MovingStateSender> senderMap;
	
	/***
	 * Stores receivers
	 */
	private HashMap<String,MovingStateReceiver> receiverMap;
	
	/***
	 * Instance variable
	 */
	private static MovingStateManager instance;
	
	/**
	 * Constructor -> use getInstance()
	 */
	private MovingStateManager() {
		senderMap = new HashMap<String,MovingStateSender>();
		receiverMap = new HashMap<String,MovingStateReceiver>();
	}
	
	/***
	 * Returns instance
	 * @return instance
	 */
	public static MovingStateManager getInstance() {
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
	public String addSender(String peerID) {
		
		String newPipeID = IDFactory.newPipeID(
				OsgiServiceManager.getP2pNetworkManager().getLocalPeerGroupID()).toString();
		
		try {
			MovingStateSender sender = new MovingStateSender(peerID,newPipeID);
			senderMap.put(newPipeID, sender);
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
	public void addReceiver(String peerID, String pipeID) {
		try {
			MovingStateReceiver receiver = new MovingStateReceiver(peerID,pipeID);
			receiverMap.put(pipeID, receiver);
			
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
