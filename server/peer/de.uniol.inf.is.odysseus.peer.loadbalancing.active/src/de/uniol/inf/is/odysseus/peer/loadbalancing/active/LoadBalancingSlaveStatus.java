package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

public class LoadBalancingSlaveStatus {
	
	
	
	public enum INVOLVEMENT_TYPES {
		VOLUNTEERING_PEER,PEER_WITH_SENDER_OR_RECEIVER
	}
	
	public enum LB_PHASES {
		WAITING_FOR_ADD, WAITING_FOR_MSG_RECEIVED,WAITING_FOR_SYNC,ABORT
	}
	
	private LB_PHASES phase;
	
	private final INVOLVEMENT_TYPES involvementType;

	private final int lbProcessId;
	private final PeerID initiatingPeer;
	
	private Integer[] installedQueries;
	
	private ConcurrentHashMap<String,String> replacedPipes;
	

	private final LoadBalancingMessageDispatcher messageDispatcher;
	
	
	
	public Integer[] getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Integer[] installedQueries) {
		this.installedQueries = installedQueries;
	}
	
	public boolean isPipeKnown(String pipeId) {
		return replacedPipes.contains(pipeId);
	}
	
	public void addReplacedPipe(String newPipeId, String oldPipeId) {
		if(!replacedPipes.contains(newPipeId)) {
			replacedPipes.put(newPipeId,oldPipeId);
		}
	}


	public ConcurrentHashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}

	public LoadBalancingSlaveStatus(INVOLVEMENT_TYPES involvementType, LB_PHASES phase, PeerID initiatingPeer, int lbProcessId, LoadBalancingMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.replacedPipes = new ConcurrentHashMap<String,String>();
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

	public PeerID getInitiatingPeer() {
		return initiatingPeer;
	}
	
	public LoadBalancingMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

}
