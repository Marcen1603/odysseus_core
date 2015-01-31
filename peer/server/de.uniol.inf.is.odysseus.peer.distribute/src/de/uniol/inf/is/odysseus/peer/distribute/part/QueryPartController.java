package de.uniol.inf.is.odysseus.peer.distribute.part;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.RepeatingMessageSend;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartControllerListener;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.StartQueryAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.StartQueryMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.StopQueryAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.StopQueryMessage;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

// TODO javaDoc M.B.
public class QueryPartController implements IPlanModificationListener, IPeerCommunicatorListener, IQueryPartController {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartController.class);

	protected static final String SHARED_QUERY_ID_TAG = "sharedQueryID";
	protected static final String TYPE_TAG = "type";
	protected static final String REMOVE_MSG_TYPE = "remove";

	private static QueryPartController instance;
	private static IServerExecutor executor;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;

	private static final Collection<IQueryPartControllerListener> listeners = Lists.newArrayList();

	private final Map<Integer, ID> sharedQueryIDMap = Maps.newHashMap();
	private final Map<ID, Collection<PeerID>> peerIDMap = Maps.newHashMap();
	private final Map<ID, PeerID> masterIDMap = Maps.newHashMap();

	private final Map<PeerIDSharedQueryIDPair, RepeatingMessageSend> queryRemoveSenderMap = Maps.newConcurrentMap();
	private final Map<PeerIDSharedQueryIDPair, RepeatingMessageSend> queryStartSenderMap = Maps.newConcurrentMap();
	private final Map<PeerIDSharedQueryIDPair, RepeatingMessageSend> queryStopSenderMap = Maps.newConcurrentMap();

	private boolean inEvent;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		peerCommunicator.registerMessageType(RemoveQueryMessage.class);
		peerCommunicator.registerMessageType(RemoveQueryAckMessage.class);
		peerCommunicator.registerMessageType(StartQueryMessage.class);
		peerCommunicator.registerMessageType(StopQueryMessage.class);
		peerCommunicator.registerMessageType(StartQueryAckMessage.class);
		peerCommunicator.registerMessageType(StopQueryAckMessage.class);
		peerCommunicator.addListener(this, RemoveQueryMessage.class);
		peerCommunicator.addListener(this, RemoveQueryAckMessage.class);
		peerCommunicator.addListener(this, StartQueryMessage.class);
		peerCommunicator.addListener(this, StopQueryMessage.class);
		peerCommunicator.addListener(this, StartQueryAckMessage.class);
		peerCommunicator.addListener(this, StopQueryAckMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, RemoveQueryMessage.class);
			peerCommunicator.removeListener(this, RemoveQueryAckMessage.class);
			peerCommunicator.removeListener(this, StartQueryMessage.class);
			peerCommunicator.removeListener(this, StopQueryMessage.class);
			peerCommunicator.removeListener(this, StartQueryAckMessage.class);
			peerCommunicator.removeListener(this, StopQueryAckMessage.class);
			peerCommunicator.unregisterMessageType(RemoveQueryMessage.class);
			peerCommunicator.unregisterMessageType(RemoveQueryAckMessage.class);
			peerCommunicator.unregisterMessageType(StartQueryMessage.class);
			peerCommunicator.unregisterMessageType(StopQueryMessage.class);
			peerCommunicator.unregisterMessageType(StartQueryAckMessage.class);
			peerCommunicator.unregisterMessageType(StopQueryAckMessage.class);

			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		executor.addPlanModificationListener(this);

		instance = this;
		LOG.debug("Query part controller activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		executor.removePlanModificationListener(this);
		instance = null;

		LOG.debug("Query part controller deactivated");
	}

	// called by OSGi-DS
	public static void bindListener(IQueryPartControllerListener listener) {
		listeners.add(listener);
	}

	// called by OSGi-DS
	public static void unbindListener(IQueryPartControllerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	@Override
	// called by executor
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (inEvent) {
			return; // avoid stack overflow
		}

		try {
			inEvent = true;
			
			if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
				IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

				int queryID = query.getID();
				ID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
				Collection<PeerID> peerIDs = peerIDMap.get(sharedQueryID);
				
				if( peerIDs != null ) {
					for( PeerID peerID : peerIDs ) {
						StartQueryMessage msg = new StartQueryMessage(sharedQueryID);
						RepeatingMessageSend sender = new RepeatingMessageSend(peerCommunicator, msg, peerID);
						queryStartSenderMap.put(new PeerIDSharedQueryIDPair(peerID, sharedQueryID), sender);
						sender.start();
					}
				}
			} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
				IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

				int queryID = query.getID();
				ID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
				Collection<PeerID> peerIDs = peerIDMap.get(sharedQueryID);
				
				if( peerIDs != null ) {
					for( PeerID peerID : peerIDs ) {
						StopQueryMessage msg = new StopQueryMessage(sharedQueryID);
						RepeatingMessageSend sender = new RepeatingMessageSend(peerCommunicator, msg, peerID);
						queryStopSenderMap.put(new PeerIDSharedQueryIDPair(peerID, sharedQueryID), sender);
						sender.start();
					}
				}
				
				
			} else if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
				IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

				int queryID = query.getID();
				ID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}

				Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
				LOG.debug("Got REMOVE-event for queryid={}", queryID);
				LOG.debug("Shared query id is {}", sharedQueryID);
				Collection<PeerID> peerIDs = peerIDMap.get(sharedQueryID);

				if (peerIDs != null) {
					for (PeerID peerID : peerIDs) {
						RemoveQueryMessage msg = new RemoveQueryMessage(sharedQueryID);
						RepeatingMessageSend sender = new RepeatingMessageSend(peerCommunicator, msg, peerID);
						queryRemoveSenderMap.put(new PeerIDSharedQueryIDPair(peerID, sharedQueryID), sender);
						sender.start();
					}
				}

				// queryID is already removed here, thats the reason why
				// it is the exception here
				tryRemoveQueries(executor, ids, queryID);
				for (final Integer id : ids) {
					if (id != queryID) {
						sharedQueryIDMap.remove(id);
					}
				}
			}
		} finally {
			inEvent = false;
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof RemoveQueryMessage) {
			RemoveQueryMessage removeMessage = (RemoveQueryMessage) message;

			removeSharedQuery(removeMessage.getSharedQueryID());

			RemoveQueryAckMessage ackMessage = new RemoveQueryAckMessage(removeMessage.getSharedQueryID());
			try {
				peerCommunicator.send(senderPeer, ackMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send ack message", e);
			}

		} else if (message instanceof RemoveQueryAckMessage) {
			RemoveQueryAckMessage ackMessage = (RemoveQueryAckMessage) message;

			PeerIDSharedQueryIDPair pair = new PeerIDSharedQueryIDPair(senderPeer, ackMessage.getSharedQueryID());
			RepeatingMessageSend sender = queryRemoveSenderMap.get(pair);
			if (sender != null) {
				sender.stopRunning();
				queryRemoveSenderMap.remove(pair);
			}
		} 
		
		else if( message instanceof StartQueryMessage ) {
			StartQueryMessage startMessage = (StartQueryMessage) message;
			StartQueryAckMessage responseMessage = new StartQueryAckMessage(startMessage.getSharedQueryID());
			try {
				peerCommunicator.send(senderPeer, responseMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send start query ack message", e);
			}
			
			Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, startMessage.getSharedQueryID());
			Collection<Integer> idsToStart = determineQueriesToChange(ids);
			
			for( Integer idToStart : idsToStart ) {
				if( executor.getQueryState(idToStart) != QueryState.RUNNING ) {
					executor.startQuery(idToStart, PeerDistributePlugIn.getActiveSession());
				}
			}
		} else if( message instanceof StartQueryAckMessage ) {
			StartQueryAckMessage ackMessage = (StartQueryAckMessage) message;
			
			PeerIDSharedQueryIDPair pair = new PeerIDSharedQueryIDPair(senderPeer, ackMessage.getSharedQueryID());
			RepeatingMessageSend sender = queryStartSenderMap.get(pair);
			if (sender != null) {
				sender.stopRunning();
				queryStartSenderMap.remove(pair);
			}
		}
		
		else if( message instanceof StopQueryMessage ) {
			StopQueryMessage stopMessage = (StopQueryMessage) message;
			StopQueryAckMessage responseMessage = new StopQueryAckMessage(stopMessage.getSharedQueryID());
			try {
				peerCommunicator.send(senderPeer, responseMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send stop query ack message", e);
			}
			
			Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, stopMessage.getSharedQueryID());
			Collection<Integer> idsToStop = determineQueriesToChange(ids);
			
			for( Integer idToStop : idsToStop ) {
				if( executor.getQueryState(idToStop) != QueryState.INACTIVE ) {
					executor.stopQuery(idToStop, PeerDistributePlugIn.getActiveSession());
				}
			}
		} else if( message instanceof StopQueryAckMessage ) {
			StopQueryAckMessage ackMessage = (StopQueryAckMessage) message;
			
			PeerIDSharedQueryIDPair pair = new PeerIDSharedQueryIDPair(senderPeer, ackMessage.getSharedQueryID());
			RepeatingMessageSend sender = queryStopSenderMap.get(pair);
			if (sender != null) {
				sender.stopRunning();
				queryStopSenderMap.remove(pair);
			}
		}
	}

	private static Collection<Integer> determineQueriesToChange(Collection<Integer> ids) {
		Collection<Integer> idsToChange = Lists.newArrayList();
		for( Integer id : ids ) {
			ILogicalQuery logicalQuery = executor.getLogicalQueryById(id, PeerDistributePlugIn.getActiveSession());
			if( logicalQuery != null ) {
				Collection<ILogicalOperator> operators = LogicalQueryHelper.getAllOperators(logicalQuery.getLogicalPlan());
				for( ILogicalOperator operator : operators ) {
					if( !(operator instanceof JxtaReceiverAO) && operator.getSubscriptions().size() == 0 ) {
						if( !idsToChange.contains(id)) {
							idsToChange.add(id);
						}
					}
				}
			}
		}
		return idsToChange;
	}

	public void removeSharedQuery(ID sharedQueryID) {
		Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
		if (!ids.isEmpty()) {
			tryRemoveQueries(executor, ids, null);

			for (Integer id : ids) {
				sharedQueryIDMap.remove(id);
			}
		}
	}

	@Override
	public void registerAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers) {
		Preconditions.checkNotNull(query, "Logical query must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(otherPeers, "otherPeers must not be null!");

		sharedQueryIDMap.put(queryID, sharedQueryID);
		peerIDMap.put(sharedQueryID, otherPeers);

		LOG.debug("Registered sharedqueryID as master : {}", sharedQueryID);

		// notify listeners
		for (IQueryPartControllerListener listener : listeners) {
			try {
				listener.afterRegisterAsMaster(query, queryID, sharedQueryID, otherPeers);
			} catch (Throwable t) {
				LOG.error("Error while calling afterRegisterAsMaster of a listener!");
			}
		}
	}

	@Override
	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID, PeerID masterPeer) {
		Preconditions.checkNotNull(ids, "List of logical query ids must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(masterPeer, "masterPeerID must not be null!");

		for (Integer id : ids) {
			sharedQueryIDMap.put(id, sharedQueryID);
		}

		masterIDMap.put(sharedQueryID, masterPeer);

		// notify listeners
		for (IQueryPartControllerListener listener : listeners) {
			try {
				listener.afterRegisterAsSlave(ids, sharedQueryID, masterPeer);
			} catch (Throwable t) {
				LOG.error("Error while calling afterRegisterAsSlave of a listener!");
			}
		}
	}

	@Override
	public ID getSharedQueryID(int queryId) {
		Preconditions.checkNotNull(queryId);
		return this.sharedQueryIDMap.get(queryId);
	}

	@Override
	public Collection<Integer> getLocalIds(ID sharedQueryId) {
		Preconditions.checkNotNull(sharedQueryId);

		Collection<Integer> out = Lists.newArrayList();
		for (int id : this.sharedQueryIDMap.keySet()) {
			if (this.sharedQueryIDMap.get(id).equals(sharedQueryId)) {
				out.add(id);
			}
		}

		return out;
	}

	@Override
	public boolean isMasterForQuery(int queryID) {
		if (!sharedQueryIDMap.containsKey(queryID)) {
			return false;
		}
		ID sharedQueryID = sharedQueryIDMap.get(queryID);
		return peerIDMap.containsKey(sharedQueryID);
	}

	public static QueryPartController getInstance() {
		return instance;
	}

	protected static ID convertToID(String sharedQueryIDString) {
		try {
			return IDFactory.fromURI(new URI(sharedQueryIDString));
		} catch (final URISyntaxException ex) {
			LOG.error("Could not convert string {} to id", sharedQueryIDString, ex);
			return null;
		}
	}

	protected static Collection<Integer> determineLocalIDs(Map<Integer, ID> sharedQueryIDMap, ID sharedQueryID) {
		List<Integer> ids = Lists.newArrayList();
		for (Integer id : sharedQueryIDMap.keySet().toArray(new Integer[0])) {
			ID sharedQueryID2 = sharedQueryIDMap.get(id);
			if (sharedQueryID2.equals(sharedQueryID)) {
				ids.add(id);
			}
		}
		return ids;
	}

	protected static void tryRemoveQueries(IExecutor executor, Collection<Integer> ids, Integer exceptionID) {
		for (final Integer id : ids) {
			if (exceptionID == null || id != exceptionID) {
				try {
					ISession session = PeerDistributePlugIn.getActiveSession();

					executor.removeQuery(id, session);
				} catch (final PlanManagementException ex) {
					LOG.error("Could not remove query with id={}", id, ex);
				}
			}
		}
	}

	@Override
	public void unregisterAsMaster(ID sharedQueryId) {
		Preconditions.checkNotNull(sharedQueryId, "Shared Query ID must not be null!");

		if (peerIDMap.containsKey(sharedQueryId)) {
			peerIDMap.remove(sharedQueryId);
		}
		for (IQueryPartControllerListener listener : listeners) {
			try {
				listener.afterUnregisterAsMaster(sharedQueryId);
			} catch (Throwable t) {
				LOG.error("Error while calling afterUnregisterAsMaster of a listener!");
				continue;
			}
		}

	}

	@Override
	public void unregisterLocalQueriesFromSharedQuery(ID sharedQueryID, Collection<Integer> toRemove) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(toRemove, "toRemove must not be null!");

		Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
		if (!ids.isEmpty()) {
			for (Integer id : ids) {
				if (toRemove.contains(id)) {
					// Only remove queries that should be removed (Peer could
					// contain more than one local ID!)
					sharedQueryIDMap.remove(id);
				}
			}
			for (IQueryPartControllerListener listener : listeners) {
				try {
					listener.afterUnregisterAsSlave(sharedQueryID, ids);

				} catch (Throwable t) {
					LOG.error("Error while calling afterUnregisterAsMaster of a listener!");
					continue;
				}
			}
		}

	}

	@Override
	public Collection<PeerID> getOtherPeers(ID sharedQueryId) {
		if (this.peerIDMap.containsKey(sharedQueryId)) {
			return peerIDMap.get(sharedQueryId);
		}
		return null;
	}

	@Override
	public PeerID getMasterForQuery(ID sharedQueryID) {
		return this.masterIDMap.get(sharedQueryID);
	}

	@Override
	public PeerID addOtherPeer(ID sharedQueryID, PeerID peerID) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(peerID, "peerID must not be null!");

		if (this.peerIDMap.containsKey(sharedQueryID)) {
			Collection<PeerID> otherPeers = peerIDMap.get(sharedQueryID);
			if (!otherPeers.contains(peerID)) {
				otherPeers.add(peerID);
				peerIDMap.put(sharedQueryID, otherPeers);
			}
		}

		return null;
	}

	@Override
	public boolean isSharedQueryKnown(ID sharedQueryID) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		return sharedQueryIDMap.containsValue(sharedQueryID);
	}

	@Override
	public void removeOtherPeer(ID sharedQueryId, PeerID peerID) {
		Preconditions.checkNotNull(sharedQueryId, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(peerID, "peerID must not be null!");

		if (this.peerIDMap.containsKey(sharedQueryId)) {
			Collection<PeerID> otherPeers = peerIDMap.get(sharedQueryId);
			if (otherPeers.contains(peerID)) {
				otherPeers.remove(peerID);
				peerIDMap.put(sharedQueryId, otherPeers);
			}
		}

	}

	@Override
	public void addLocalQueryToShared(ID sharedQueryID, int localQueryID) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		this.sharedQueryIDMap.put(localQueryID, sharedQueryID);
	}

	@Override
	public Collection<ID> getSharedQueryIds() {
		return this.peerIDMap.keySet();
	}

}
