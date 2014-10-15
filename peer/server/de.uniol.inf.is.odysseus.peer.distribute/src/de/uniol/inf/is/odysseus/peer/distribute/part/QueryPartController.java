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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
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
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.RemoveQueryMessage;

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

	private final Map<Integer, ID> sharedQueryIDMap = Maps.newHashMap();
	private final Map<ID, Collection<PeerID>> peerIDMap = Maps.newHashMap();
	private final Map<PeerIDSharedQueryIDPair, RepeatingMessageSend> senderMap = Maps.newConcurrentMap();

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
		peerCommunicator.addListener(this, RemoveQueryMessage.class);
		peerCommunicator.addListener(this, RemoveQueryAckMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, RemoveQueryMessage.class);
			peerCommunicator.removeListener(this, RemoveQueryAckMessage.class);
			peerCommunicator.unregisterMessageType(RemoveQueryMessage.class);
			peerCommunicator.unregisterMessageType(RemoveQueryAckMessage.class);

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

	@Override
	// called by executor
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (inEvent) {
			return; // avoid stack overflow
		}

		try {
			inEvent = true;

			if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
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
						senderMap.put(new PeerIDSharedQueryIDPair(peerID, sharedQueryID), sender);
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

			RemoveQueryAckMessage ackMessage = new RemoveQueryAckMessage( removeMessage.getSharedQueryID());
			try {
				peerCommunicator.send(senderPeer, ackMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send ack message", e);
			}

		} else if (message instanceof RemoveQueryAckMessage) {
			RemoveQueryAckMessage ackMessage = (RemoveQueryAckMessage) message;

			PeerIDSharedQueryIDPair pair = new PeerIDSharedQueryIDPair(senderPeer, ackMessage.getSharedQueryID());
			RepeatingMessageSend sender = senderMap.get(pair);
			if (sender != null) {
				sender.stopRunning();
				senderMap.remove(pair);
			}
		}
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

	public void registerAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers) {
		Preconditions.checkNotNull(query, "Logical query must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(otherPeers, "otherPeers must not be null!");

		sharedQueryIDMap.put(queryID, sharedQueryID);
		peerIDMap.put(sharedQueryID, otherPeers);

		LOG.debug("Registered sharedqueryID as master : {}", sharedQueryID);
	}

	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID) {
		Preconditions.checkNotNull(ids, "List of logical query ids must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		for (Integer id : ids) {
			sharedQueryIDMap.put(id, sharedQueryID);
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
		for(int id : this.sharedQueryIDMap.keySet()) {
			
			if(this.sharedQueryIDMap.get(id).equals(sharedQueryId)) {
				
				out.add(id);
				
			}
			
		}
		
		return out;
		
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
}
