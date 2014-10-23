package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingBufferPO;

public class MovingStateSlaveStatus implements ILoadBalancingSlaveStatus {
	
	private final String COMMUNICATOR_NAME = "MovingState";
	
	
	
	public enum INVOLVEMENT_TYPES {
		VOLUNTEERING_PEER,PEER_WITH_SENDER_OR_RECEIVER
	}
	
	public enum LB_PHASES {
		WAITING_FOR_ADD, WAITING_FOR_MSG_RECEIVED,WAITING_FOR_COPY,WAITING_FOR_FINISH,ABORT
	}
	
	private LB_PHASES phase;
	
	private final INVOLVEMENT_TYPES involvementType;

	private final int lbProcessId;
	private final PeerID initiatingPeer;
	
	private Collection<Integer> installedQueries;
	
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String,JxtaSenderPO> replacedSenders;
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String,JxtaReceiverPO> replacedReceivers;
	
	@SuppressWarnings("rawtypes")
	private ArrayList<LoadBalancingBufferPO> installedBuffers;
	
	private final MovingStateMessageDispatcher messageDispatcher;
	
	public Collection<Integer> getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Collection<Integer> installedQueries) {
		this.installedQueries = installedQueries;
	}
	
	public boolean isPipeKnown(String pipeId) {
		return replacedSenders.containsKey(pipeId) || replacedReceivers.containsKey(pipeId);
	}
	
	@SuppressWarnings("rawtypes")
	public void storeReplacedSender(String newPipeId, JxtaSenderPO sender) {
		replacedSenders.put(newPipeId,sender);
	}
	
	@SuppressWarnings("rawtypes")
	public void storeReplacedReceiver(String newPipeId, JxtaReceiverPO receiver) {
		replacedReceivers.put(newPipeId,receiver);
	}
	
	@SuppressWarnings("rawtypes")
	public MovingStateSlaveStatus(INVOLVEMENT_TYPES involvementType, LB_PHASES phase, PeerID initiatingPeer, int lbProcessId, MovingStateMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.replacedReceivers = new ConcurrentHashMap<String,JxtaReceiverPO>();
		this.replacedSenders = new ConcurrentHashMap<String,JxtaSenderPO>();
		this.installedBuffers = new ArrayList<LoadBalancingBufferPO>();
	}


	public LB_PHASES getPhase() {
		return phase;
	}


	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}


	public INVOLVEMENT_TYPES getInvolvementType() {
		return involvementType;
	}

	public int getLbProcessId() {
		return lbProcessId;
	}

	public PeerID getMasterPeer() {
		return initiatingPeer;
	}
	
	public MovingStateMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}
	
	@SuppressWarnings("rawtypes")
	public void storeBuffer(LoadBalancingBufferPO buffer) {
		installedBuffers.add(buffer);
	}
	
	@SuppressWarnings("rawtypes")
	public List<LoadBalancingBufferPO> getInstalledBuffers() {
		return installedBuffers;
	}
	
	@SuppressWarnings("rawtypes")
	public JxtaSenderPO getReplacedSender(String newPipeID) {
		return replacedSenders.get(newPipeID);
	}

	@Override
	public String getCommunicatorName() {
		return COMMUNICATOR_NAME;
	}


}
