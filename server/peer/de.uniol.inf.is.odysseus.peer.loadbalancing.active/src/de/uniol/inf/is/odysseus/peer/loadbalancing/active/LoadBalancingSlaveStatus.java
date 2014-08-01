package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;

public class LoadBalancingSlaveStatus {
	
	
	
	public enum INVOLVEMENT_TYPES {
		VOLUNTEERING_PEER,PEER_WITH_SENDER_OR_RECEIVER
	}
	
	private final INVOLVEMENT_TYPES involvementType;

	private final int lbProcessId;
	private final PeerID initiatingPeer;
	
	private Integer[] installedQueries;
	
	private ConcurrentHashMap<String,String> replacedPipes;
	
	public Integer[] getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Integer[] installedQueries) {
		this.installedQueries = installedQueries;
	}


	public ConcurrentHashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}


	public void setReplacedPipes(ConcurrentHashMap<String, String> replacedPipes) {
		this.replacedPipes = replacedPipes;
	}

	private final LoadBalancingMessageDispatcher messageDispatcher;
	
	
	public LoadBalancingSlaveStatus(INVOLVEMENT_TYPES involvementType, PeerID initiatingPeer, int lbProcessId, LoadBalancingMessageDispatcher messageDispatcher) {
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
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
