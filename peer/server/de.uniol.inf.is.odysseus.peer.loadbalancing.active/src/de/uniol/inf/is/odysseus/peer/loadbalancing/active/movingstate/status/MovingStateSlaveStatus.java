package de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common.ILoadBalancingSlaveStatus;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.communicator.MovingStateMessageDispatcher;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.protocol.IStateReceivedListener;

/**
 * Implements a Slave Status for MovingState Strategy that holds all relevant
 * information.
 * 
 * @author Carsten Cordes
 *
 */
public class MovingStateSlaveStatus implements ILoadBalancingSlaveStatus,
		IStateReceivedListener {

	/***
	 * Name of the corresponding strategy
	 */
	private final String COMMUNICATOR_NAME = "MovingState";
	
	private ConcurrentHashMap<String,String> pipeMapping = new ConcurrentHashMap<String,String>();
	
	private ConcurrentHashMap<IPhysicalOperator,IPhysicalOperator> replacedToOriginalOperatorMapping = new ConcurrentHashMap<IPhysicalOperator,IPhysicalOperator>();
	

	private List<IStatefulPO> statefulOperatorList;
	
	
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
	private ArrayList<String> knownSenderPipes;
	
	private ArrayList<String> knownReceiverPipes;

	/**
	 * List of Pipes which are buffering
	 */
	private ArrayList<String> bufferedPipes;

	/***
	 * Mapping between PipeIDs for receivers and their corresponding Operators
	 */
	private ConcurrentHashMap<String, IStatefulPO> receiverOperatorMapping;

	/***
	 * Mapping between pipe IDs and their old PeerID
	 */
	private ConcurrentHashMap<String,String> pipeOldPeerMapping;
	
	/***
	 * Message Dispatcher for LoadBalancingProcess
	 */
	private final MovingStateMessageDispatcher messageDispatcher;

	

	private boolean registeredAsSlave = false;
	
	private boolean registeredAsMaster = false;
	
	
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
		WAITING_FOR_ADD, WAITING_FOR_MSG_RECEIVED, WAITING_FOR_COPY, WAITING_FOR_FINISH, ABORT
	}

	/***
	 * Holds current LoadBalancing Phase
	 */
	private LB_PHASES phase;
	
	
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
	
	public void addReplacedOperator(IPhysicalOperator original, IPhysicalOperator replacement) {
		this.replacedToOriginalOperatorMapping.put(replacement,original);
	}
	
	public IPhysicalOperator getOriginalOperator(IPhysicalOperator replacement) {
		return replacedToOriginalOperatorMapping.get(replacement);
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
	
	/**
	 * Adds a Pipe ID and its old Peer ID to status. Needed for rollback.
	 * @param pipeID PipeID of changed pipe
	 * @param peerID Old Peer ID of pipe.
	 */
	public void addChangedPipe(String pipeID, String peerID) {
		if(!pipeOldPeerMapping.containsKey(pipeID)) {
			pipeOldPeerMapping.put(pipeID,peerID);
		}
	}

	
	public List<IPhysicalOperator> getReplacedOperators() {
		return new ArrayList<IPhysicalOperator>(replacedToOriginalOperatorMapping.keySet());
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
	public MovingStateSlaveStatus(INVOLVEMENT_TYPES involvementType,
			LB_PHASES phase, PeerID initiatingPeer, int lbProcessId,
			MovingStateMessageDispatcher messageDispatcher) {
		this.phase = phase;
		this.involvementType = involvementType;
		this.initiatingPeer = initiatingPeer;
		this.lbProcessId = lbProcessId;
		this.messageDispatcher = messageDispatcher;
		this.knownSenderPipes = new ArrayList<String>();
		this.knownReceiverPipes = new ArrayList<String>();
		this.receiverOperatorMapping = new ConcurrentHashMap<String, IStatefulPO>();
		this.pipeOldPeerMapping = new ConcurrentHashMap<String,String>();
	}

	/***
	 * Add State-Receiver to receiver-Operator-Mapping
	 * 
	 * @param pipeId
	 *            PipeID of Receiver
	 * @param operator
	 *            Operator that belongs to receiver
	 */
	public void addReceiver(String pipeId, IStatefulPO operator) {
		if (!receiverOperatorMapping.containsKey(pipeId)) {
			receiverOperatorMapping.put(pipeId, operator);
		}
	}

	/***
	 * Get Stateful PO that belongs to (Statful-Receiver-)Pipe
	 * 
	 * @param pipeID
	 *            PipeID of receiver
	 * @return StatefulPO
	 */
	public IStatefulPO getStatefulPOforPipe(String pipeID) {
		if (receiverOperatorMapping.containsKey(pipeID)) {
			return receiverOperatorMapping.get(pipeID);
		}
		return null;
	}

	/***
	 * Add Pipe to known pipes
	 * 
	 * @param pipe
	 *            pipe-to-add
	 */
	public void addKnownReceiverPipe(String pipe) {
		if (!isReceiverPipeKnown(pipe)) {
			knownReceiverPipes.add(pipe);
		}
	}
	
	public void addKnownSenderPipe(String pipe) {
		if(!isSenderPipeKnown(pipe)) {
			knownSenderPipes.add(pipe);
		}
	}

	/***
	 * Looks if knownPipes contains a pipe
	 * 
	 * @param pipe
	 *            pipe to look for
	 * @return true if pipe is in list, false if not
	 */
	public boolean isReceiverPipeKnown(String pipe) {
		return knownReceiverPipes.contains(pipe);
	}
	
	public boolean isSenderPipeKnown(String pipe) {
		return knownSenderPipes.contains(pipe);
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
	public int getLbProcessId() {
		return lbProcessId;
	}

	/**
	 * Gets Peer ID of master peer
	 * 
	 * @return PeerID of Master Peer
	 */
	public PeerID getMasterPeer() {
		return initiatingPeer;
	}

	/***
	 * Returns Message Dispatcher
	 * 
	 * @return Message Dispatcher
	 */
	public MovingStateMessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	/***
	 * Returns Communicator Name
	 */
	@Override
	public String getCommunicatorName() {
		return COMMUNICATOR_NAME;
	}

	/***
	 * Called when state is received by StateReceiver
	 */
	@Override
	public void stateReceived(String pipe) {
		MovingStateHelper.injectState(this, pipe);

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

	public ConcurrentHashMap<String,String> getPipeOldPeerMapping() {
		return pipeOldPeerMapping;
	}

	public void setPipeOldPeerMapping(ConcurrentHashMap<String,String> pipeOldPeerMapping) {
		this.pipeOldPeerMapping = pipeOldPeerMapping;
	}

	public List<IStatefulPO> getStatefulOperatorList() {
		return statefulOperatorList;
	}
	
	public synchronized void setStatefulOperatorList(List<IStatefulPO> statefulOperatorList) {
		this.statefulOperatorList = statefulOperatorList;
	}
	

	public void addPipeMapping(String oldPipe, String newPipe) {
		this.pipeMapping.put(oldPipe, newPipe);
	}
	
	public String getNewPipeForOldPipe(String oldPipe) {
		return pipeMapping.get(oldPipe);
	}


}
