package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.status;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator.ParallelTrackMessageDispatcher;

/**
 * Preserves a loadBalancing Status in the initiating Peer to control LoadBalancing.
 * @author Carsten Cordes
 *
 */
public class ParallelTrackMasterStatus implements ILoadBalancingMasterStatus {
	
	private final String COMMUNICATOR_NAME = "ParallelTrack";
	
	
	
	public enum LB_PHASES {
		INITIATING,COPYING,RELINKING_SENDERS,RELINKING_RECEIVERS,SYNCHRONIZING,DELETING,FAILURE
	}
	
	private ParallelTrackMessageDispatcher messageDispatcher;
	
	public ParallelTrackMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}
	public void setMessageDispatcher(
			ParallelTrackMessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}


	private LB_PHASES phase = LB_PHASES.INITIATING;
	
	private int processId;
	private int logicalQuery;
	private ILogicalQueryPart originalPart;
	private ILogicalQueryPart modifiedPart;
	private HashMap<String,String> replacedPipes;
	
	private ArrayList<String> pipesToSync;
	
	public void setPipesToSync(ArrayList<String> pipesToSync) {
		this.pipesToSync = pipesToSync;
	}
	
	public synchronized void removePipeToSync(String pipeId) {
		if(this.pipesToSync.contains(pipeId)) {
				this.pipesToSync.remove(pipeId);
		}
	}
	
	public synchronized int getNumberOfPipesToSync() {
		return pipesToSync.size();
	}

	private HashMap <String,PeerID> peersForPipe;
	
	private PeerID volunteeringPeer;
	
	public HashMap<String, PeerID> getPeersForPipe() {
		return peersForPipe;
	}
	public void setPeersForPipe(HashMap<String, PeerID> peersForPipe) {
		this.peersForPipe = peersForPipe;
	}
	
	public LB_PHASES getPhase() {
		return phase;
	}
	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public int getLogicalQuery() {
		return logicalQuery;
	}
	public void setLogicalQuery(int logicalQuery) {
		this.logicalQuery = logicalQuery;
	}
	public ILogicalQueryPart getOriginalPart() {
		return originalPart;
	}
	public void setOriginalPart(ILogicalQueryPart originalPart) {
		this.originalPart = originalPart;
	}
	public ILogicalQueryPart getModifiedPart() {
		return modifiedPart;
	}
	public void setModifiedPart(ILogicalQueryPart modifiedPart) {
		this.modifiedPart = modifiedPart;
	}
	public PeerID getVolunteeringPeer() {
		return volunteeringPeer;
	}
	public void setVolunteeringPeer(PeerID volunteeringPeer) {
		this.volunteeringPeer = volunteeringPeer;
	}
	public HashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}
	public void setReplacedPipes(HashMap<String, String> replacedPipes) {
		this.replacedPipes = replacedPipes;
	}
	@Override
	public String getCommunicationStrategy() {
		return COMMUNICATOR_NAME;
	}
	
	

	
	
	
	
	
}
