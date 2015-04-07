package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
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
	private PeerID volunteeringPeer = null;
	
	private Collection<Integer> installedQueries;
	

	@SuppressWarnings("rawtypes")
	private List<JxtaSenderPO> obsoleteReceiverList = new ArrayList<JxtaSenderPO>();
	
	private ConcurrentHashMap<String,String> replacedSenderPipes;
	private ConcurrentHashMap<String,String> replacedReceiverPipes;
	

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
	
	public boolean isSenderPipeKnown(String pipeId) {
		return replacedSenderPipes.values().contains(pipeId);
	}
	
	public boolean isReceiverPipeKnown(String pipeId) {
		return replacedReceiverPipes.values().contains(pipeId);
	}
	
	public void addReplacedReceiverPipe(String newPipeId, String oldPipeId) {
		if(!replacedReceiverPipes.containsKey(oldPipeId)) {
			replacedReceiverPipes.put(oldPipeId, newPipeId);
		}
	}
	

	public void addReplacedSenderPipe(String newPipeId, String oldPipeId) {
		if(!replacedSenderPipes.containsKey(oldPipeId)) {
			replacedSenderPipes.put(oldPipeId, newPipeId);
		}
	}
	
	public ConcurrentHashMap<String,String> getReplacedSenderPipes() {
		return replacedSenderPipes;
	}
	
	public ConcurrentHashMap<String,String> getReplacedReceiverPipes() {
		return replacedReceiverPipes;
	}

	
	

	public ParallelTrackSlaveStatus(INVOLVEMENT_TYPES involvementType, LB_PHASES phase, PeerID initiatingPeer, int lbProcessId, ParallelTrackMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.replacedSenderPipes = new ConcurrentHashMap<String,String>();
		this.replacedReceiverPipes = new ConcurrentHashMap<String,String>();
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

	@Override
	public int getLbProcessId() {
		return lbProcessId;
	}
	

	@SuppressWarnings("rawtypes")
	public synchronized void pushSenderToRemoveList(JxtaSenderPO sender) {
		if(!obsoleteReceiverList.contains(sender)) {
			this.obsoleteReceiverList.add(sender);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public synchronized JxtaSenderPO popSenderfromRemoveList(String pipeId) {
		Iterator<JxtaSenderPO> iterator = obsoleteReceiverList.iterator();
		while(iterator.hasNext()) {
			JxtaSenderPO nextSender = iterator.next();
			if(nextSender.getPipeIDString().equals(pipeId)) {
				return nextSender;
			}
		}
		return null;
	}
	

	@Override
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

	public PeerID getVolunteeringPeer() {
		return volunteeringPeer;
	}

	public void setVolunteeringPeer(PeerID volunteeringPeer) {
		this.volunteeringPeer = volunteeringPeer;
	}

}
