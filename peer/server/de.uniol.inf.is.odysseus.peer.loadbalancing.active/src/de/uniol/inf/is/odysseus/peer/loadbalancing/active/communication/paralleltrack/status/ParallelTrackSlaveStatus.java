package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackMessageDispatcher;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public class ParallelTrackSlaveStatus implements ILoadBalancingSlaveStatus{
	
	private final String COMMUNICATOR_NAME = "ParallelTrack";
	
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
	
	private Collection<Integer> installedQueries;
	
	private ConcurrentHashMap<String,String> replacedPipes;
	

	private final ParallelTrackMessageDispatcher messageDispatcher;
	

	private boolean registeredAsSlave = false;
	private boolean registeredAsMaster = false;
	public boolean isRegisteredAsMaster() {
		return registeredAsMaster;
	}

	private PeerID sharedQueryMasterPeer;
	private ID sharedQueryID;
	
	public boolean isRegisteredAsSlave() {
		return registeredAsSlave;
	}
	
	public PeerID getSharedQueryMaster() {
		return sharedQueryMasterPeer;
	}
	
	public ID sharedQueryID() {
		return sharedQueryID;
	}

	
	public void setRegisteredAsNewSlave(PeerID masterPeer, ID sharedQueryID) {
		this.registeredAsSlave = true;
		this.sharedQueryMasterPeer = masterPeer;
		this.sharedQueryID = sharedQueryID;
	}
	
	public void setRegisteredAsMaster(ID sharedQueryID) {
		this.registeredAsMaster = true;
		this.sharedQueryID = sharedQueryID;
	}
	
	public Collection<Integer> getInstalledQueries() {
		return installedQueries;
	}


	public void setInstalledQueries(Collection<Integer> installedQueries) {
		this.installedQueries = installedQueries;
	}
	
	public boolean isPipeKnown(String pipeId) {
		return replacedPipes.containsKey(pipeId);
	}
	
	public void addReplacedPipe(String newPipeId, String oldPipeId) {
		if(!replacedPipes.containsKey(newPipeId)) {
			replacedPipes.put(newPipeId,oldPipeId);
		}
	}


	public ConcurrentHashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}

	public ParallelTrackSlaveStatus(INVOLVEMENT_TYPES involvementType, LB_PHASES phase, PeerID initiatingPeer, int lbProcessId, ParallelTrackMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.replacedPipes = new ConcurrentHashMap<String,String>();
	}


	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}
	
	public LB_PHASES getPhase() {
		return phase;
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
	
	public ParallelTrackMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}


	@Override
	public String getCommunicatorName() {
		return COMMUNICATOR_NAME;
	}

}
