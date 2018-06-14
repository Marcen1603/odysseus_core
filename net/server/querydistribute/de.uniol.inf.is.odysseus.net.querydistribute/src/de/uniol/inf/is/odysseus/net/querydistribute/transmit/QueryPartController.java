package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartController;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartControllerListener;
import de.uniol.inf.is.odysseus.net.querydistribute.activator.QueryDistributionPlugIn;

// TODO javaDoc M.B.
public class QueryPartController implements IPlanModificationListener, IOdysseusNodeCommunicatorListener, IQueryPartController {

	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartController.class);

	protected static final String SHARED_QUERY_ID_TAG = "sharedQueryID";
	protected static final String TYPE_TAG = "type";
	protected static final String REMOVE_MSG_TYPE = "remove";
	private static final Collection<IQueryPartControllerListener> LISTENERS = Lists.newArrayList();

	private static QueryPartController instance;
	private static IServerExecutor executor;
	private static IOdysseusNodeCommunicator nodeCommunicator;


	private final Map<Integer, UUID> sharedQueryIDMap = Maps.newHashMap();
	private final Map<UUID, Collection<IOdysseusNode>> nodesMap = Maps.newHashMap();
	private final Map<UUID, IOdysseusNode> masterIDMap = Maps.newHashMap();

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
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;

		nodeCommunicator.registerMessageType(RemoveQueryMessage.class);
		nodeCommunicator.registerMessageType(StartQueryMessage.class);
		nodeCommunicator.registerMessageType(StopQueryMessage.class);
		nodeCommunicator.addListener(this, RemoveQueryMessage.class);
		nodeCommunicator.addListener(this, StartQueryMessage.class);
		nodeCommunicator.addListener(this, StopQueryMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.removeListener(this, RemoveQueryMessage.class);
			nodeCommunicator.removeListener(this, StartQueryMessage.class);
			nodeCommunicator.removeListener(this, StopQueryMessage.class);
			nodeCommunicator.unregisterMessageType(RemoveQueryMessage.class);
			nodeCommunicator.unregisterMessageType(StartQueryMessage.class);
			nodeCommunicator.unregisterMessageType(StopQueryMessage.class);

			nodeCommunicator = null;
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
		LISTENERS.add(listener);
	}

	// called by OSGi-DS
	public static void unbindListener(IQueryPartControllerListener listener) {
		if (LISTENERS.contains(listener)) {
			LISTENERS.remove(listener);
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
				UUID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
				Collection<IOdysseusNode> nodes = nodesMap.get(sharedQueryID);

				if( nodes != null ) {
					for( IOdysseusNode node : nodes ) {
						StartQueryMessage msg = new StartQueryMessage(sharedQueryID);
						try {
							nodeCommunicator.send(node, msg);
						} catch (OdysseusNodeCommunicationException e) {
							LOG.error("Could not send start query message to {}", node ,e);
						}
					}
				}
			} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
				IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

				int queryID = query.getID();
				UUID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
				Collection<IOdysseusNode> nodes = nodesMap.get(sharedQueryID);

				if( nodes != null ) {
					for( IOdysseusNode node : nodes ) {
						StopQueryMessage msg = new StopQueryMessage(sharedQueryID);
						try {
							nodeCommunicator.send(node, msg);
						} catch (OdysseusNodeCommunicationException e) {
							LOG.error("Could not send stop query message to {}", node ,e);
						}
					}
				}


			} else if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
				IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

				int queryID = query.getID();
				UUID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}

				Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
				LOG.debug("Got REMOVE-event for queryid={}", queryID);
				LOG.debug("Shared query id is {}", sharedQueryID);
				Collection<IOdysseusNode> nodes = nodesMap.get(sharedQueryID);

				if (nodes != null) {
					for (IOdysseusNode node : nodes) {
						RemoveQueryMessage msg = new RemoveQueryMessage(sharedQueryID);
						try {
							nodeCommunicator.send(node, msg);
						} catch (OdysseusNodeCommunicationException e) {
							LOG.error("Could not send remove query message to {}", node ,e);
						}
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
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof RemoveQueryMessage) {
			RemoveQueryMessage removeMessage = (RemoveQueryMessage) message;

			removeSharedQuery(removeMessage.getSharedQueryID());
		} else if( message instanceof StartQueryMessage ) {
			StartQueryMessage startMessage = (StartQueryMessage) message;

			Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, startMessage.getSharedQueryID());

			for( Integer idToStart : ids ) {
				if( executor.getQueryState(idToStart, superUser) != QueryState.RUNNING ) {
					executor.startQuery(idToStart, QueryDistributionPlugIn.getActiveSession());
				}
			}
		} else if( message instanceof StopQueryMessage ) {
			StopQueryMessage stopMessage = (StopQueryMessage) message;

			Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, stopMessage.getSharedQueryID());

			for( Integer idToStop : ids ) {
				if( executor.getQueryState(idToStop, superUser) != QueryState.INACTIVE ) {
					executor.stopQuery(idToStop, QueryDistributionPlugIn.getActiveSession());
				}
			}
		}
	}

	public void removeSharedQuery(UUID sharedQueryID) {
		Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
		if (!ids.isEmpty()) {
			tryRemoveQueries(executor, ids, null);

			for (Integer id : ids) {
				sharedQueryIDMap.remove(id);
			}
		}
	}

	@Override
	public void registerAsMaster(ILogicalQuery query, int queryID, UUID sharedQueryID, Collection<IOdysseusNode> otherNodes) {
		Preconditions.checkNotNull(query, "Logical query must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(otherNodes, "otherNodes must not be null!");

		sharedQueryIDMap.put(queryID, sharedQueryID);
		nodesMap.put(sharedQueryID, otherNodes);

		LOG.debug("Registered sharedqueryID as master : {}", sharedQueryID);

		// notify listeners
		for (IQueryPartControllerListener listener : LISTENERS) {
			try {
				listener.afterRegisterAsMaster(query, queryID, sharedQueryID, otherNodes);
			} catch (Throwable t) {
				LOG.error("Error while calling afterRegisterAsMaster of a listener!");
			}
		}
	}

	@Override
	public void registerAsSlave(Collection<Integer> ids, UUID sharedQueryID, IOdysseusNode masterNode) {
		Preconditions.checkNotNull(ids, "List of logical query ids must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(masterNode, "masterNode must not be null!");

		for (Integer id : ids) {
			sharedQueryIDMap.put(id, sharedQueryID);
		}

		masterIDMap.put(sharedQueryID, masterNode);

		// notify listeners
		for (IQueryPartControllerListener listener : LISTENERS) {
			try {
				listener.afterRegisterAsSlave(ids, sharedQueryID, masterNode);
			} catch (Throwable t) {
				LOG.error("Error while calling afterRegisterAsSlave of a listener!");
			}
		}
	}

	@Override
	public UUID getSharedQueryID(int queryId) {
		Preconditions.checkNotNull(queryId);
		return this.sharedQueryIDMap.get(queryId);
	}

	@Override
	public Collection<Integer> getLocalIds(UUID sharedQueryId) {
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
		UUID sharedQueryID = sharedQueryIDMap.get(queryID);
		return nodesMap.containsKey(sharedQueryID);
	}

	public static QueryPartController getInstance() {
		return instance;
	}

	protected static Collection<Integer> determineLocalIDs(Map<Integer, UUID> sharedQueryIDMap, UUID sharedQueryID) {
		List<Integer> ids = Lists.newArrayList();
		for (Integer id : sharedQueryIDMap.keySet().toArray(new Integer[0])) {
			UUID sharedQueryID2 = sharedQueryIDMap.get(id);
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
					ISession session = QueryDistributionPlugIn.getActiveSession();

					executor.removeQuery(id, session);
				} catch (final PlanManagementException ex) {
					LOG.error("Could not remove query with id={}", id, ex);
				}
			}
		}
	}

	@Override
	public void unregisterAsMaster(UUID sharedQueryId) {
		Preconditions.checkNotNull(sharedQueryId, "Shared Query ID must not be null!");

		if (nodesMap.containsKey(sharedQueryId)) {
			nodesMap.remove(sharedQueryId);
		}
		for (IQueryPartControllerListener listener : LISTENERS) {
			try {
				listener.afterUnregisterAsMaster(sharedQueryId);
			} catch (Throwable t) {
				LOG.error("Error while calling afterUnregisterAsMaster of a listener!");
				continue;
			}
		}

	}

	@Override
	public void unregisterLocalQueriesFromSharedQuery(UUID sharedQueryID, Collection<Integer> toRemove) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(toRemove, "toRemove must not be null!");

		Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
		if (!ids.isEmpty()) {
			for (Integer id : ids) {
				if (toRemove.contains(id)) {
					// Only remove queries that should be removed (Node could
					// contain more than one local ID!)
					sharedQueryIDMap.remove(id);
				}
			}
			for (IQueryPartControllerListener listener : LISTENERS) {
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
	public Collection<IOdysseusNode> getOtherNodes(UUID sharedQueryId) {
		if (this.nodesMap.containsKey(sharedQueryId)) {
			return nodesMap.get(sharedQueryId);
		}
		return null;
	}

	@Override
	public IOdysseusNode getMasterForQuery(UUID sharedQueryID) {
		return this.masterIDMap.get(sharedQueryID);
	}

	@Override
	public IOdysseusNode addOtherNode(UUID sharedQueryID, IOdysseusNode node) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(node, "node must not be null!");

		if (this.nodesMap.containsKey(sharedQueryID)) {
			Collection<IOdysseusNode> otherNodes = nodesMap.get(sharedQueryID);
			if (!otherNodes.contains(node)) {
				otherNodes.add(node);
				nodesMap.put(sharedQueryID, otherNodes);
			}
		}

		return null;
	}

	@Override
	public boolean isSharedQueryKnown(UUID sharedQueryID) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		return sharedQueryIDMap.containsValue(sharedQueryID);
	}

	@Override
	public void removeOtherNode(UUID sharedQueryId, IOdysseusNode node) {
		Preconditions.checkNotNull(sharedQueryId, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(node, "node must not be null!");

		if (this.nodesMap.containsKey(sharedQueryId)) {
			Collection<IOdysseusNode> otherNodes = nodesMap.get(sharedQueryId);
			if (otherNodes.contains(node)) {
				otherNodes.remove(node);
				nodesMap.put(sharedQueryId, otherNodes);
			}
		}

	}

	@Override
	public void addLocalQueryToShared(UUID sharedQueryID, int localQueryID) {
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		this.sharedQueryIDMap.put(localQueryID, sharedQueryID);
	}

	@Override
	public Collection<UUID> getSharedQueryIds() {
		return this.nodesMap.keySet();
	}

}
