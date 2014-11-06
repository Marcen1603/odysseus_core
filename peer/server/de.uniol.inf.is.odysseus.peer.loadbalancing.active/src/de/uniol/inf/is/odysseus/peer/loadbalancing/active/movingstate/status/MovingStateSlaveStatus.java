package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol.IStateReceivedListener;

public class MovingStateSlaveStatus implements ILoadBalancingSlaveStatus, IStateReceivedListener {
	
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
	
	private ConcurrentHashMap<String, IStatefulPO> receiverOperatorMapping;
	
	
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
		this.receiverOperatorMapping = new ConcurrentHashMap<String,IStatefulPO>();
	}
	
	public void addReceiver(String pipeId, IStatefulPO operator) {
		if(!receiverOperatorMapping.containsKey(pipeId)) {
			receiverOperatorMapping.put(pipeId, operator);
		}
	}

	public IStatefulPO getStatefulPOforPipe(String pipeID) {
		if(receiverOperatorMapping.containsKey(pipeID)) {
			return receiverOperatorMapping.get(pipeID);
		}
		return null;
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


	@Override
	public void stateReceived(String pipe) {
		getMessageDispatcher().sendCopyStateFinished(this.getMasterPeer(), pipe);
	}


}
