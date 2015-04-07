package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.inactivequery.communicator.InactiveQueryMessageDispatcher;

/**
 * Implements a Slave Status for MovingState Strategy that holds all relevant
 * information.
 * 
 * @author Carsten Cordes
 *
 */
public class InactiveQuerySlaveStatus implements ILoadBalancingSlaveStatus {

	/***
	 * Name of the corresponding strategy
	 */
	private final String COMMUNICATOR_NAME = "InactiveQuery";

	/**
	 * Constants for different types of peers.
	 * 
	 * @author Carsten Cordes
	 *
	 */
	public enum INVOLVEMENT_TYPES {
		VOLUNTEERING_PEER, PEER_WITH_SENDER_OR_RECEIVER
	}

	/**
	 * Constants for different (slave-side) LoadBalancing Phases.
	 * 
	 * @author Carsten Cordes
	 *
	 */
	public enum LB_PHASES {
		WAITING_FOR_ADD, WAITING_FOR_MSG_RECEIVED, WAITING_FOR_FINISH, ABORT
	}

	/***
	 * Holds current LoadBalancing Phase
	 */
	private LB_PHASES phase;
	
	/**
	 * Holds involvement Type of peer
	 */
	private final INVOLVEMENT_TYPES involvementType;

	/**
	 * LoadBalancing process id
	 */
	private final int lbProcessId;

	/**
	 * ID of Master Peer
	 */
	private final PeerID initiatingPeer;

	/***
	 * List of installed Query Ids
	 */
	private Collection<Integer> installedQueries;

	/**
	 * List of Pipes already processed
	 */
	private ArrayList<String> knownPipes;

	/***
	 * Mapping between pipe IDs and their old PeerID
	 */
	private ConcurrentHashMap<String,JxtaOperatorInformation> pipeInformationMapping;
	
	/***
	 * Message Dispatcher for LoadBalancingProcess
	 */
	private final InactiveQueryMessageDispatcher messageDispatcher;
	

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
	
	public void addChangedPipe(String pipeID, JxtaOperatorInformation information) {
		if(!pipeInformationMapping.containsKey(pipeID)) {
			pipeInformationMapping.put(pipeID, information);
		}
	}

	
	/***
	 * Sets List of installed Queries
	 * 
	 * @param installedQueries
	 *            List of installed Query IDs
	 */
	public void setInstalledQueries(Collection<Integer> installedQueries) {
		this.installedQueries = installedQueries;
	}

	/****
	 * Constructor
	 * 
	 * @param involvementType
	 *            Involvement Type of Peer
	 * @param phase
	 *            Load Balancing Phase
	 * @param initiatingPeer
	 *            Master Peer
	 * @param lbProcessId
	 *            LoadBalancing Process ID
	 * @param messageDispatcher
	 *            Message Dispatcher for LoadBalancing Process
	 */
	public InactiveQuerySlaveStatus(INVOLVEMENT_TYPES involvementType,
			LB_PHASES phase, PeerID initiatingPeer, int lbProcessId,
			InactiveQueryMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.knownPipes = new ArrayList<String>();
		this.pipeInformationMapping = new ConcurrentHashMap<String,JxtaOperatorInformation>();
	}

	/***
	 * Add Pipe to known pipes
	 * 
	 * @param pipe
	 *            pipe-to-add
	 */
	public void addKnownPipe(String pipe) {
		if (!isPipeKnown(pipe)) {
			knownPipes.add(pipe);
		}
	}

	/***
	 * Looks if knownPipes contains a pipe
	 * 
	 * @param pipe
	 *            pipe to look for
	 * @return true if pipe is in list, false if not
	 */
	public boolean isPipeKnown(String pipe) {
		return knownPipes.contains(pipe);
	}

	/**
	 * Returns current LoadBalancing Phase
	 * 
	 * @return LoadBalancing Phase
	 */
	public LB_PHASES getPhase() {
		return phase;
	}

	/**
	 * Sets loadbalancing Phase.
	 * 
	 * @param phase
	 *            LoadBalancing Phase to set.
	 */
	public void setPhase(LB_PHASES phase) {
		this.phase = phase;
	}

	/***
	 * Gets involvement type of peer
	 * 
	 * @return Involvement Type of peer
	 */
	public INVOLVEMENT_TYPES getInvolvementType() {
		return involvementType;
	}

	/***
	 * gets LoadBalancing Process Id
	 * 
	 * @return LoadBalancing Process Id
	 */
	@Override
	public int getLbProcessId() {
		return lbProcessId;
	}

	/**
	 * Gets Peer ID of master peer
	 * 
	 * @return PeerID of Master Peer
	 */
	@Override
	public PeerID getMasterPeer() {
		return initiatingPeer;
	}

	/***
	 * Returns Message Dispatcher
	 * 
	 * @return Message Dispatcher
	 */
	public InactiveQueryMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	/***
	 * Returns Communicator Name
	 */
	@Override
	public String getCommunicatorName() {
		return COMMUNICATOR_NAME;
	}

	public ConcurrentHashMap<String,JxtaOperatorInformation> getPipeInformationMapping() {
		return pipeInformationMapping;
	}

	public void setPipeInformationMapping(ConcurrentHashMap<String,JxtaOperatorInformation> pipeInformationMapping) {
		this.pipeInformationMapping = pipeInformationMapping;
	}

}
