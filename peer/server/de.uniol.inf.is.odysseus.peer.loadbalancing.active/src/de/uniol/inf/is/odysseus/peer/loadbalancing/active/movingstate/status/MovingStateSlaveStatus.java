package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.Collection;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;

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
	private ArrayList<String> knownPipes;
	
	
	private final MovingStateMessageDispatcher messageDispatcher;
	
	public Collection<Integer> getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Collection<Integer> installedQueries) {
		this.installedQueries = installedQueries;
	}
	
	
	public MovingStateSlaveStatus(INVOLVEMENT_TYPES involvementType, LB_PHASES phase, PeerID initiatingPeer, int lbProcessId, MovingStateMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.knownPipes = new ArrayList<String>();
	}

	public void addKnownPipe(String pipe) {
		if(!isPipeKnown(pipe)) {
			knownPipes.add(pipe);
		}
	}
	
	public boolean isPipeKnown(String pipe) {
		return knownPipes.contains(pipe);
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
	
	@Override
	public String getCommunicatorName() {
		return COMMUNICATOR_NAME;
	}


}
