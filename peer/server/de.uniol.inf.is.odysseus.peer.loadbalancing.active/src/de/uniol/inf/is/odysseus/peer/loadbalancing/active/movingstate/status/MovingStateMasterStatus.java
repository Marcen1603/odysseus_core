package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateManager;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;

/**
 * Preserves a loadBalancing Status in the initiating Peer to control
 * LoadBalancing.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateMasterStatus implements ILoadBalancingMasterStatus {
	

	/***
	 * Name of Communicator
	 */
	private final String COMMUNCIATOR_NAME = "MovingState";

	/***
	 * Different LoadBalancing Phases in MovingState Protocol
	 * 
	 * @author Carsten Cordes
	 *
	 */
	public enum LB_PHASES {
		INITIATING, COPYING_QUERY, RELINKING_SENDERS, RELINKING_RECEIVERS, COPYING_STATES, COPYING_FINISHED, STOP_BUFFERING, FINISHED, FAILURE
	}

	/***
	 * Message Dispatcher
	 */
	private MovingStateMessageDispatcher messageDispatcher;

	/***
	 * List of identifies upstream Peers
	 */
	private ArrayList<PeerID> upstreamPeers;

	/**
	 * List of identifiers for downstream Peers.
	 */
	private ArrayList<PeerID> downstramPeers;

	

	/**
	 * Holds current LB Phase
	 */
	private LB_PHASES phase = LB_PHASES.INITIATING;

	/**
	 * Process id for current Loadbalancing Process
	 */
	private int processId;

	/**
	 * Holds Id of local Query
	 */
	private int logicalQuery;

	/**
	 * Holds local QueryPart
	 */
	private ILogicalQueryPart originalPart;

	/**
	 * Holds modified copy of local Query Part
	 */
	private ILogicalQueryPart modifiedPart;

	/**
	 * Map for Senders and the corresponding Operators
	 */
	private ConcurrentHashMap<String, IStatefulPO> senderOperatorMapping = new ConcurrentHashMap<String, IStatefulPO>();

	/***
	 * Pipes that need to be synchronized between different peers
	 */
	private ArrayList<String> pipesToSync;

	
	private HashMap<String,String> replacedPipes;
	
	/**
	 * List of Pipes which are buffering
	 */
	private ArrayList<String> bufferedPipes = new ArrayList<String>();
	
	
	private boolean isMaster;
	public boolean isMaster() {
		return isMaster;
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	public Collection<Integer> getLocalQueriesForSharedQuery() {
		return localQueriesForSharedQuery;
	}

	public Collection<PeerID> getOtherPeersForSharedQuery() {
		return otherPeersForSharedQuery;
	}

	

	private ID sharedQueryID;
	private Collection<Integer> localQueriesForSharedQuery;
	private Collection<PeerID>  otherPeersForSharedQuery;
	private PeerID sharedQueryMasterPeer;

	
	public void storeSharedQueryInformation(boolean isMaster, ID sharedQueryID,Collection<Integer> localQueriesForSharedQuery, Collection<PeerID> otherPeers) {
		this.isMaster = isMaster;
		this.sharedQueryID = sharedQueryID;
		this.localQueriesForSharedQuery = localQueriesForSharedQuery;
		this.otherPeersForSharedQuery = otherPeers;
	}
	
	

	public HashMap<String, String> getReplacedPipes() {
		return replacedPipes;
	}
	public void setReplacedPipes(HashMap<String, String> replacedPipes) {
		this.replacedPipes = replacedPipes;
	}
	
	private volatile boolean locked=false;
	
	/***
	 * Get a lock (used to coordinate different phases)
	 */
	public void lock() {
		locked = true;
	}
	
	/***
	 * Release lock
	 */
	public void unlock() {
		locked = false;
	}
	
	/***
	 * Tests if lock is set.
	 * @return True if locked
	 */
	public boolean isLocked() {
		return locked;
	}
	

	/***
	 * Gets List of buffered Pipes
	 * 
	 * @return List of buffered Pipes
	 */
	public ArrayList<String> getBufferedPipes() {
		return bufferedPipes;
	}

	/***
	 * Sets list of buffered Pipes
	 * 
	 * @param bufferedPipes
	 *            List of buffered Pipes
	 */
	public void setBufferedPipes(ArrayList<String> bufferedPipes) {
		this.bufferedPipes = bufferedPipes;
	}

	/**
	 * Adds a pipe to List of buffered Pipes
	 * 
	 * @param pipe
	 *            Pipe-to-add
	 */
	public synchronized void addBufferedPipe(String pipe) {
		if (bufferedPipes == null) {
			bufferedPipes = new ArrayList<String>();
		}
		if (!bufferedPipes.contains(pipe))
			bufferedPipes.add(pipe);
	}
	
	
	
	/**
	 * Sets list of upstream Peers
	 * 
	 * @param peers
	 *            List of Peers
	 */
	public void setUpstreamPeers(ArrayList<PeerID> peers) {
		this.upstreamPeers = peers;
	}
	
	

	/**
	 * Gets list of upstream Peers
	 * 
	 * @return List of upstream Peers
	 */
	public ArrayList<PeerID> getUpstreamPeers() {
		return upstreamPeers;
	}

	/***
	 * Gets Message Dispatcher
	 * 
	 * @return Message Dispatcher
	 */
	public MovingStateMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	/**
	 * Sets Message Dispatcher
	 * 
	 * @param messageDispatcher
	 *            message Dispatcher
	 */
	public void setMessageDispatcher(
			MovingStateMessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}

	/***
	 * Returns number of unfinished (State-)Transmissions
	 * 
	 * @return Number of unfinished State Transmissions
	 */
	public int getNumberOfUnfinishedTransmissions() {
		int counter = 0;
		for (String pipe : senderOperatorMapping.keySet()) {
			if (MovingStateManager.getInstance().getSender(pipe) != null
					&& !MovingStateManager.getInstance().getSender(pipe)
							.isSuccessfullyTransmitted()) {
				counter++;
			}
		}
		return counter;
	}
	
	
	/***
	 * Sets pipesToSyns
	 * 
	 * @param pipesToSync
	 *            List of Pipes
	 */
	public void setPipesToSync(ArrayList<String> pipesToSync) {
		this.pipesToSync = pipesToSync;
	}
	
	public ArrayList<String> getPipesToSync() {
		return pipesToSync;
	}

	/***
	 * Remove pipe from toSync List
	 * 
	 * @param pipeId
	 *            Pipe
	 */
	public synchronized void removePipeToSync(String pipeId) {
		if (this.pipesToSync.contains(pipeId)) {
			this.pipesToSync.remove(pipeId);
		}
	}

	/**
	 * Returns number of unprocessed Pipes
	 * 
	 * @return number of Pipes in pipesToSync (= unprocessed Pipes)
	 */
	public synchronized int getNumberOfPipesToSync() {
		return pipesToSync.size();
	}

	/**
	 * Mapping between Pipe and Peer IDs
	 */
	private HashMap<String, PeerID> peersForPipe;

	/**
	 * Peer ID for volunteering Slave Peer
	 */
	private PeerID volunteeringPeer;

	/***
	 * Gets peerForPipe
	 * 
	 * @return peersForPipe Hashmap
	 */
	public HashMap<String, PeerID> getPeersForPipe() {
		return peersForPipe;
	}

	/***
	 * Sets PeerForPipe Hashmap
	 * 
	 * @param peersForPipe
	 *            peersForPipe Hashmap
	 */
	public void setPeersForPipe(HashMap<String, PeerID> peersForPipe) {
		this.peersForPipe = peersForPipe;
	}

	/***
	 * Gets List of all SenderPipes
	 * 
	 * @return List of sender Pipes
	 */
	public Set<String> getAllSenderPipes() {
		return senderOperatorMapping.keySet();
	}

	/**
	 * Gets current LB Phase
	 * 
	 * @return LB Phase
	 */
	public LB_PHASES getPhase() {
		return phase;
	}

	/***
	 * Sets current LB Phase
	 * 
	 * @param phase
	 *            LB Phase
	 */
	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}

	/**
	 * Gets current LB Process Id
	 */
	public int getProcessId() {
		return processId;
	}

	/***
	 * Sets current LB Process Id
	 * 
	 * @param processId
	 *            Loadbalancing Process Id
	 */
	public void setProcessId(int processId) {
		this.processId = processId;
	}

	/***
	 * Gets id of Logical Query
	 */
	public int getLogicalQuery() {
		return logicalQuery;
	}

	/***
	 * Sets id of logical Query
	 * 
	 * @param logicalQuery
	 *            query Id
	 */
	public void setLogicalQuery(int logicalQuery) {
		this.logicalQuery = logicalQuery;
	}

	/***
	 * Gets original Query Part
	 * 
	 * @return original Query Part
	 */
	public ILogicalQueryPart getOriginalPart() {
		return originalPart;
	}

	/**
	 * Sets original Query Part
	 * 
	 * @param originalPart
	 *            QueryPart
	 */
	public void setOriginalPart(ILogicalQueryPart originalPart) {
		this.originalPart = originalPart;
	}

	/***
	 * Get modified Query Part
	 * 
	 * @return (modified) Query Part
	 */
	public ILogicalQueryPart getModifiedPart() {
		return modifiedPart;
	}

	/**
	 * Sets modified QueryPart
	 * 
	 * @param modifiedPart
	 *            (modified) Query Part
	 */
	public void setModifiedPart(ILogicalQueryPart modifiedPart) {
		this.modifiedPart = modifiedPart;
	}

	/**
	 * Gets PeerID of volunteering Peer
	 */
	public PeerID getVolunteeringPeer() {
		return volunteeringPeer;
	}

	/***
	 * Sets id of volunteering Peer
	 * 
	 * @param volunteeringPeer
	 *            Peer ID of volunteering peer
	 */
	public void setVolunteeringPeer(PeerID volunteeringPeer) {
		this.volunteeringPeer = volunteeringPeer;
	}

	/**
	 * Adds sender to sender Operator Mapping
	 * 
	 * @param pipe
	 *            String of Sender
	 * @param operator
	 *            Operator
	 */
	public void addSender(String pipe, IStatefulPO operator) {
			senderOperatorMapping.putIfAbsent(pipe, operator);
	}

	/**
	 * Get operator for sender with particular pipe
	 * 
	 * @param pipe
	 *            Pipe
	 * @return Operator that corresponds to pipe
	 */
	public IStatefulPO getOperatorForSender(String pipe) {
		if (senderOperatorMapping.containsKey(pipe)) {
			return senderOperatorMapping.get(pipe);
		}
		return null;
	}

	/**
	 * Get Name of Communcation Strategy
	 */
	@Override
	public String getCommunicationStrategy() {
		return COMMUNCIATOR_NAME;
	}

	public PeerID getSharedQueryMasterPeer() {
		return sharedQueryMasterPeer;
	}

	public void setSharedQueryMasterPeer(PeerID sharedQueryMasterPeer) {
		this.sharedQueryMasterPeer = sharedQueryMasterPeer;
	}

	public ArrayList<PeerID> getDownstramPeers() {
		return downstramPeers;
	}

	public void setDownstramPeers(ArrayList<PeerID> downstramPeers) {
		this.downstramPeers = downstramPeers;
	}

}
