package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;

/**
 * Preserves a loadBalancing Status in the initiating Peer to control LoadBalancing.
 * @author Carsten Cordes
 *
 */
public class MovingStateMasterStatus implements ILoadBalancingMasterStatus{
	
	private final String COMMUNCIATOR_NAME = "MovingState";
	
	public enum LB_PHASES {
		INITIATING,COPYING_QUERY,RELINKING_SENDERS,RELINKING_RECEIVERS,COPYING_STATES,COPYING_FINISHED,STOP_BUFFERING,FINISHED,FAILURE
	}
	
	private MovingStateMessageDispatcher messageDispatcher;
	private ArrayList<PeerID> upstreamPeers;
	
	public void setUpstreamPeers(ArrayList<PeerID> peers) {
		this.upstreamPeers = peers;
	}
	
	public ArrayList<PeerID> getUpstreamPeers() {
		return upstreamPeers;
	}
	
	public MovingStateMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}
	public void setMessageDispatcher(
			MovingStateMessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}
	
	public int getNumberOfUnfinishedTransmissions() {
		int counter = 0;
		for(String pipe : senderOperatorMapping.keySet()) {
			if(MovingStateManager.getInstance().getSender(pipe)!=null && !MovingStateManager.getInstance().getSender(pipe).isSuccessfullyTransmitted()) {
				counter++;
			}
		}
		return counter;
	}


	private LB_PHASES phase = LB_PHASES.INITIATING;
	
	private int processId;
	private int logicalQuery;
	private ILogicalQueryPart originalPart;
	private ILogicalQueryPart modifiedPart;
	private ConcurrentHashMap<String,IStatefulPO> senderOperatorMapping = new ConcurrentHashMap<String,IStatefulPO>();
	
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
	
	public Set<String> getAllSenderPipes() {
		return senderOperatorMapping.keySet();
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
	
	public void addSender(String pipe, IStatefulPO operator) {
		if(!senderOperatorMapping.containsKey(pipe)) {
			senderOperatorMapping.put(pipe, operator);
		}
	}
	public IStatefulPO getOperatorForSender(String pipe) {
		if(senderOperatorMapping.containsKey(pipe)) {
			return senderOperatorMapping.get(pipe);
		}
		return null;
	}
	
	@Override
	public String getCommunicationStrategy() {
		return COMMUNCIATOR_NAME;
	}
	
	

	
	
	
	
	
}
