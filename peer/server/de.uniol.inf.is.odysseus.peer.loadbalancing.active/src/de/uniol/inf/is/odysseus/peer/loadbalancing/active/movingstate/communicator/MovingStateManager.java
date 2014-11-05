package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ActiveLoadBalancingActivator;
import net.jxta.id.IDFactory;

public class MovingStateManager {

	
	private static final Logger LOG = LoggerFactory
			.getLogger(MovingStateManager.class);
	private HashMap<String,MovingStateSender> senderMap;
	private HashMap<String,MovingStateReceiver> receiverMap;
	private static MovingStateManager instance;
	
	private MovingStateManager() {
		senderMap = new HashMap<String,MovingStateSender>();
		receiverMap = new HashMap<String,MovingStateReceiver>();
	}
	
	public static MovingStateManager getInstance() {
		if(instance==null) {
			instance = new MovingStateManager();
		}
		return instance;
	}
	
	public String addSender(String peerID) {
		
		String newPipeID = IDFactory.newPipeID(
				ActiveLoadBalancingActivator.getP2pNetworkManager().getLocalPeerGroupID()).toString();
		
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
	
	public boolean isReceiverPipeKnown(String pipeId) {
		return receiverMap.containsKey(pipeId);
	}
	
	public boolean isSenderPipeKnown(String pipeId) {
		return senderMap.containsKey(pipeId);
	}
	
	public void addReceiver(String peerID, String pipeID) {
		try {
			MovingStateReceiver receiver = new MovingStateReceiver(peerID,pipeID);
			receiverMap.put(pipeID, receiver);
			
		} catch (DataTransmissionException e) {
			LOG.error("Could not create MovingStateReceiver");
			e.printStackTrace();
		}
	}
	
	public MovingStateSender getSender(String pipeID) {
		return senderMap.get(pipeID);
	}

	public MovingStateReceiver getReceiver(String pipeId) {
		return receiverMap.get(pipeId);
	}
}
