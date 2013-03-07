package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

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
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.OutputPipeResolver;

public class QueryPartController implements IPlanModificationListener, PipeMsgListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartController.class);

	private static final String SHARED_QUERY_ID_TAG = "sharedQueryID";
	private static final String TYPE_TAG = "type";
	private static final String REMOVE_MSG_TYPE = "remove";
	private static final String START_MSG_TYPE = "start";
	private static final String STOP_MSG_TYPE = "stop";

	private static QueryPartController instance;

	private final Map<ID, OutputPipe> outputPipeMap = Maps.newHashMap();
	private final Map<ID, InputPipe> inputPipeMap = Maps.newHashMap();
	private final Map<Integer, ID> sharedQueryIDMap = Maps.newHashMap();

	private final List<OutputPipeResolver> runningResolvers = Lists.newArrayList();

	private IServerExecutor executor;
	private boolean inEvent;

	public static QueryPartController getInstance() {
		return instance;
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;

		stopResolvers(runningResolvers);
		closeOutputPipes(outputPipeMap.values());
		closeInputPipes(inputPipeMap.values());
	}

	public void registerAsMaster(Collection<ILogicalQuery> queries, final ID sharedQueryID) {
		Preconditions.checkNotNull(queries, "List of logical queries must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		for (ILogicalQuery query : queries) {
			sharedQueryIDMap.put(query.getID(), sharedQueryID);
		}

		final PipeAdvertisement adv = createPipeAdvertisement(sharedQueryID);

		OutputPipeResolver resolver = new OutputPipeResolver(adv) {
			@Override
			public void outputPipeResolved(OutputPipe outputPipe) {
				outputPipeMap.put(sharedQueryID, outputPipe);

				LOG.debug("Output pipe is {}", outputPipe);
				LOG.debug("for shared query id {}", sharedQueryID);
				LOG.debug("Pipeid of Advertisementid is {}", adv.getPipeID());

				runningResolvers.remove(this);
			}

			@Override
			public void outputPipeFailed() {
				LOG.debug("Could not get output pipe for shared query id {}", sharedQueryID);

				runningResolvers.remove(this);
			}
		};
		runningResolvers.add(resolver);
		resolver.start();

		LOG.debug("Registered sharedqueryID as master : {}", sharedQueryID);
	}

	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID) {
		Preconditions.checkNotNull(ids, "List of logical query ids must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		for (Integer id : ids) {
			sharedQueryIDMap.put(id, sharedQueryID);
		}

		PipeAdvertisement adv = createPipeAdvertisement(sharedQueryID);

		try {
			if (!inputPipeMap.containsKey(sharedQueryID)) {
				InputPipe inputPipe = P2PNewPlugIn.getPipeService().createInputPipe(adv, this);
				inputPipeMap.put(sharedQueryID, inputPipe);
				LOG.debug("Created new input pipe for shared query id {}", sharedQueryID);
				LOG.debug("Pipeid of Advertisementid is {}", adv.getPipeID());
			}

			LOG.debug("Registerd shared query id as slave : {}", sharedQueryID);
			LOG.debug("Local ids shared: {}", ids);
		} catch (IOException ex) {
			LOG.error("Could not create input pipe for {}", sharedQueryID, ex);
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

			IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

			int queryID = query.getID();
			ID sharedQueryID = sharedQueryIDMap.get(queryID);
			if (sharedQueryID == null) {
				return; // query was not shared
			}

			Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
			OutputPipe outputPipe = outputPipeMap.get(sharedQueryID);
			if (outputPipe != null) {
				if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
					LOG.debug("Got REMOVE-event for queryid={}", queryID);
					LOG.debug("Shared query id is {}", sharedQueryID);

					sendMessage(outputPipe, sharedQueryID, REMOVE_MSG_TYPE);

					outputPipe.close();
					outputPipeMap.remove(sharedQueryID);

					tryRemoveQueries(executor, ids, queryID);
					for (Integer id : ids) {
						if (id != queryID) {
							sharedQueryIDMap.remove(id);
						}
					}

				} else if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
					// ignore

				} else if (PlanModificationEventType.QUERY_START.equals(eventArgs.getEventType())) {
					LOG.debug("Got START-event for queryid={}", queryID);
					LOG.debug("Shared query id is {}", sharedQueryID);

					sendMessage(outputPipe, sharedQueryID, START_MSG_TYPE);
					tryStartQueries(executor, ids, queryID);

				} else if (PlanModificationEventType.QUERY_STOP.equals(eventArgs.getEventType())) {
					LOG.debug("Got STOP-event for queryid={}", queryID);
					LOG.debug("Shared query id is {}", sharedQueryID);

					sendMessage(outputPipe, sharedQueryID, STOP_MSG_TYPE);
					tryStopQueries(executor, ids, queryID);
				}
			}
		} finally {
			inEvent = false;
		}
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		Message msg = event.getMessage();
		MessageElement typeElement = msg.getMessageElement(TYPE_TAG);
		MessageElement sharedQueryIDElement = msg.getMessageElement(SHARED_QUERY_ID_TAG);

		String type = new String(typeElement.getBytes(false));
		String sharedQueryIDString = new String(sharedQueryIDElement.getBytes(false));
		ID sharedQueryID = convertToID(sharedQueryIDString);

		Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);

		if (sharedQueryID != null) {
			LOG.debug("Got message for shared query id {}", sharedQueryID);

			switch (type) {
			case REMOVE_MSG_TYPE:
				LOG.debug("Remove queries {}", ids);

				tryRemoveQueries(executor, ids, null);
				for (Integer id : ids) {
					sharedQueryIDMap.remove(id);
				}
				InputPipe inputPipe = inputPipeMap.remove(sharedQueryID);
				inputPipe.close();
				break;

			case START_MSG_TYPE:
				LOG.debug("Start queries {}", ids);

				tryStartQueries(executor, ids, null);
				break;

			case STOP_MSG_TYPE:
				LOG.debug("Stop queries {}", ids);

				tryStopQueries(executor, ids, null);
				break;

			default:
				LOG.error("Unknown message type {}", type);
				break;
			}
		}
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
		executor.addPlanModificationListener(this);

		LOG.debug("Bound ServerExecutor {}", exe);
	}

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			LOG.debug("Unbound ServerExecutor {}", exe);

			executor.removePlanModificationListener(this);
			executor = null;
		}
	}

	private static Collection<Integer> determineLocalIDs(Map<Integer, ID> sharedQueryIDMap, ID sharedQueryID) {
		List<Integer> ids = Lists.newArrayList();
		for (Integer id : sharedQueryIDMap.keySet().toArray(new Integer[0])) {
			ID sharedQueryID2 = sharedQueryIDMap.get(id);
			if (sharedQueryID2.equals(sharedQueryID)) {
				ids.add(id);
			}
		}
		return ids;
	}

	private static PipeAdvertisement createPipeAdvertisement(ID sharedQueryID) {
		PipeID pipeID = (PipeID) IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), sharedQueryID.toString().getBytes());

		PipeAdvertisement adv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		adv.setPipeID(pipeID);
		adv.setType(PipeService.PropagateType);
		return adv;
	}

	private static void sendMessage(OutputPipe outputPipe, ID sharedQueryID, String messageType) {
		Message msg = new Message();
		msg.addMessageElement(new StringMessageElement(TYPE_TAG, messageType, null));
		msg.addMessageElement(new StringMessageElement(SHARED_QUERY_ID_TAG, sharedQueryID.toString(), null));

		if (!outputPipe.isClosed()) {
			try {
				outputPipe.send(msg);
				LOG.debug("Send message of type {}", messageType);
			} catch (IOException ex) {
				LOG.error("Could not send message", ex);
			}
		} else {
			LOG.error("Could not send message since outputpipe is closed");
		}
	}

	private static void tryRemoveQueries(IExecutor executor, Collection<Integer> ids, Integer exceptionID) {
		for (Integer id : ids) {
			if (exceptionID == null || id != exceptionID) {
				try {
					executor.removeQuery(id, SessionManagementService.getActiveSession());
				} catch (PlanManagementException ex) {
					LOG.error("Could not stop query with id={}", id, ex);
				}
			}
		}
	}

	private static void tryStartQueries(IExecutor executor, Collection<Integer> ids, Integer exceptionID) {
		for (Integer id : ids) {
			if (exceptionID == null || id != exceptionID) {
				try {
					executor.startQuery(id, SessionManagementService.getActiveSession());
				} catch (PlanManagementException ex) {
					LOG.error("Could not start query with id={}", id, ex);
				}
			}
		}
	}

	private static void tryStopQueries(IExecutor executor, Collection<Integer> ids, Integer exceptionID) {
		for (Integer id : ids) {
			if (exceptionID == null || id != exceptionID) {
				try {
					executor.stopQuery(id, SessionManagementService.getActiveSession());
				} catch (PlanManagementException ex) {
					LOG.error("Could not stop query with id={}", id, ex);
				}
			}
		}
	}

	private static ID convertToID(String sharedQueryIDString) {
		try {
			return IDFactory.fromURI(new URI(sharedQueryIDString));
		} catch (URISyntaxException ex) {
			LOG.error("Could not convert string {} to id", sharedQueryIDString, ex);
			return null;
		}
	}

	private static void stopResolvers(List<OutputPipeResolver> runningResolvers) {
		for (OutputPipeResolver runningResolver : runningResolvers) {
			if (runningResolver.isAlive()) {
				runningResolver.stopRunning();
			}
		}
		runningResolvers.clear();
	}

	private static void closeOutputPipes(Collection<OutputPipe> outputPipes) {
		for (OutputPipe outputPipe : outputPipes) {
			if (outputPipe != null && !outputPipe.isClosed()) {
				outputPipe.close();
			}
		}
	}

	private static void closeInputPipes(Collection<InputPipe> inputPipes) {
		for (InputPipe inputPipe : inputPipes) {
			if (inputPipe != null) {
				inputPipe.close();
			}
		}
	}
}
