package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status;

import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingMasterStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryMessageDispatcher;

/**
 * Preserves a loadBalancing Status in the initiating Peer to control
 * LoadBalancing.
 * 
 * @author Carsten Cordes
 *
 */
public class InactiveQueryMasterStatus implements ILoadBalancingMasterStatus {

	/***
	 * Name of Communicator
	 */
	private final String COMMUNCIATOR_NAME = "InactiveQuery";

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
	 * Different LoadBalancing Phases in MovingState Protocol
	 * 
	 * @author Carsten Cordes
	 *
	 */
	public enum LB_PHASES {
		INITIATING, COPYING_QUERY, RELINKING_SENDERS, RELINKING_RECEIVERS, FINISHED, FAILURE
	}

	/***
	 * Message Dispatcher
	 */
	private InactiveQueryMessageDispatcher messageDispatcher;

	/***
	 * List of identifies upstream Peers
	 */
	private ArrayList<PeerID> upstreamPeers;

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
	public InactiveQueryMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	/**
	 * Sets Message Dispatcher
	 * 
	 * @param messageDispatcher
	 *            message Dispatcher
	 */
	public void setMessageDispatcher(
			InactiveQueryMessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}

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

	/***
	 * Pipes that need to be synchronized between different peers
	 */
	private ArrayList<String> pipesToSync;

	/***
	 * Sets pipesToSyns
	 * 
	 * @param pipesToSync
	 *            List of Pipes
	 */
	public void setPipesToSync(ArrayList<String> pipesToSync) {
		this.pipesToSync = pipesToSync;
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
	 * Get Name of Communcation Strategy
	 */
	@Override
	public String getCommunicationStrategy() {
		return COMMUNCIATOR_NAME;
	}

}
