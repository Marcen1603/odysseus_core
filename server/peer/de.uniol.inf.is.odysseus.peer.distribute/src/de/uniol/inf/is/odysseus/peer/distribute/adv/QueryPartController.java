package de.uniol.inf.is.odysseus.peer.distribute.adv;

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
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.util.OutputPipeResolver;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;

// TODO javaDoc M.B.
public class QueryPartController implements IPlanModificationListener, PipeMsgListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartController.class);

	protected static final String SHARED_QUERY_ID_TAG = "sharedQueryID";
	protected static final String TYPE_TAG = "type";
	protected static final String REMOVE_MSG_TYPE = "remove";

	private static QueryPartController instance;
	private static IServerExecutor executor;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PNetworkManager p2pNetworkManager;

	private final Map<ID, OutputPipe> outputPipeMap = Maps.newHashMap();
	private final Map<ID, InputPipe> inputPipeMap = Maps.newHashMap();
	private final Map<Integer, ID> sharedQueryIDMap = Maps.newHashMap();
	private final List<OutputPipeResolver> runningResolvers = Lists.newArrayList();

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
	public void activate() {
		executor.addPlanModificationListener(this);
		
		instance = this;
		LOG.debug("Query part controller activated");
	}
	
	// called by OSGi-DS
	public void deactivate() {
		executor.removePlanModificationListener(this);
		instance = null;

		stopResolvers(runningResolvers);
		closeOutputPipes(outputPipeMap.values());
		closeInputPipes(inputPipeMap.values());

		LOG.debug("Query part controller deactivated");
	}

	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		final Message msg = event.getMessage();
		final MessageElement typeElement = msg.getMessageElement(TYPE_TAG);
		final MessageElement sharedQueryIDElement = msg.getMessageElement(SHARED_QUERY_ID_TAG);

		final String type = new String(typeElement.getBytes(false));
		final String sharedQueryIDString = new String(sharedQueryIDElement.getBytes(false));
		final ID sharedQueryID = convertToID(sharedQueryIDString);

		final Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);

		if (!ids.isEmpty()) {
			LOG.debug("Got message for shared query id {}", sharedQueryID);
			LOG.debug("Local queries are {}", ids);

			switch (type) {
			case REMOVE_MSG_TYPE:
				LOG.debug("Remove queries {}", ids);

				tryRemoveQueries(executor, ids, null);
				for (final Integer id : ids) {
					sharedQueryIDMap.remove(id);
				}
				final InputPipe inputPipe = inputPipeMap.remove(sharedQueryID);
				inputPipe.close();
				break;
			default:
				LOG.error("Unknown message type {}", type);
				break;
			}
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

			if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) { 
				final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
	
				final int queryID = query.getID();
				final ID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
	
				final Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
				final OutputPipe outputPipe = outputPipeMap.get(sharedQueryID);
				if (outputPipe != null) {
					LOG.debug("Got REMOVE-event for queryid={}", queryID);
					LOG.debug("Shared query id is {}", sharedQueryID);

					sendMessage(outputPipe, sharedQueryID, REMOVE_MSG_TYPE);

					outputPipe.close();
					outputPipeMap.remove(sharedQueryID);

					// queryID is already removed here, thats the reason that
					// it is the exception here
					tryRemoveQueries(executor, ids, queryID);
					for (final Integer id : ids) {
						if (id != queryID) {
							sharedQueryIDMap.remove(id);
						}
					}

				} 
			}
		} finally {
			inEvent = false;
		}
	}

	public void registerAsMaster(ILogicalQuery query, final ID sharedQueryID) {
		Preconditions.checkNotNull(query, "Logical query must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");

		sharedQueryIDMap.put(query.getID(), sharedQueryID);

		final PipeAdvertisement adv = createPipeAdvertisement(sharedQueryID);

		final OutputPipeResolver resolver = new OutputPipeResolver(adv) {
			@Override
			public void outputPipeFailed() {
				LOG.debug("Could not get output pipe for shared query id {}", sharedQueryID);

				runningResolvers.remove(this);
			}

			@Override
			public void outputPipeResolved(OutputPipe outputPipe) {
				outputPipeMap.put(sharedQueryID, outputPipe);

				LOG.debug("Output pipe is {}", outputPipe);
				LOG.debug("for shared query id {}", sharedQueryID);
				LOG.debug("Pipeid of Advertisementid is {}", adv.getPipeID());

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

		for (final Integer id : ids) {
			sharedQueryIDMap.put(id, sharedQueryID);
		}

		try {
			if (!inputPipeMap.containsKey(sharedQueryID)) {
				final PipeAdvertisement adv = createPipeAdvertisement(sharedQueryID);
				final InputPipe inputPipe = jxtaServicesProvider.getPipeService().createInputPipe(adv, this);
				
				inputPipeMap.put(sharedQueryID, inputPipe);
				LOG.debug("Created new input pipe for shared query id {}", sharedQueryID);
				LOG.debug("Pipeid of Advertisementid is {}", adv.getPipeID());
			}

			LOG.debug("Registerd shared query id as slave : {}", sharedQueryID);
			LOG.debug("Local ids shared: {}", ids);
		} catch (final IOException ex) {
			LOG.error("Could not create input pipe for {}", sharedQueryID, ex);
		}
	}

	public static QueryPartController getInstance() {
		return instance;
	}

	private static void closeInputPipes(Collection<InputPipe> inputPipes) {
		for (final InputPipe inputPipe : inputPipes) {
			if (inputPipe != null) {
				inputPipe.close();
			}
		}
	}

	private static void closeOutputPipes(Collection<OutputPipe> outputPipes) {
		for (final OutputPipe outputPipe : outputPipes) {
			if (outputPipe != null && !outputPipe.isClosed()) {
				outputPipe.close();
			}
		}
	}

	protected static ID convertToID(String sharedQueryIDString) {
		try {
			return IDFactory.fromURI(new URI(sharedQueryIDString));
		} catch (final URISyntaxException ex) {
			LOG.error("Could not convert string {} to id", sharedQueryIDString, ex);
			return null;
		}
	}

	private static PipeAdvertisement createPipeAdvertisement(ID sharedQueryID) {
		final PipeID pipeID = IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID(), sharedQueryID.toString().getBytes());

		final PipeAdvertisement adv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		adv.setPipeID(pipeID);
		adv.setType(PipeService.PropagateType);
		return adv;
	}

	protected static Collection<Integer> determineLocalIDs(Map<Integer, ID> sharedQueryIDMap, ID sharedQueryID) {
		final List<Integer> ids = Lists.newArrayList();
		for (final Integer id : sharedQueryIDMap.keySet().toArray(new Integer[0])) {
			final ID sharedQueryID2 = sharedQueryIDMap.get(id);
			if (sharedQueryID2.equals(sharedQueryID)) {
				ids.add(id);
			}
		}
		return ids;
	}

	protected static void sendMessage(OutputPipe outputPipe, ID sharedQueryID, String messageType) {
		final Message msg = new Message();
		msg.addMessageElement(new StringMessageElement(TYPE_TAG, messageType, null));
		msg.addMessageElement(new StringMessageElement(SHARED_QUERY_ID_TAG, sharedQueryID.toString(), null));

		if (!outputPipe.isClosed()) {
			try {
				outputPipe.send(msg);
				LOG.debug("Send message of type {}", messageType);
			} catch (final IOException ex) {
				LOG.error("Could not send message", ex);
			}
		} else {
			LOG.error("Could not send message since outputpipe is closed");
		}
	}

	private static void stopResolvers(List<OutputPipeResolver> runningResolvers) {
		for (final OutputPipeResolver runningResolver : runningResolvers) {
			if (runningResolver.isAlive()) {
				runningResolver.stopRunning();
			}
		}
		runningResolvers.clear();
	}

	protected static void tryRemoveQueries(IExecutor executor, Collection<Integer> ids, Integer exceptionID) {
		for (final Integer id : ids) {
			if (exceptionID == null || id != exceptionID) {
				try {
					executor.removeQuery(id, PeerDistributePlugIn.getActiveSession());
				} catch (final PlanManagementException ex) {
					LOG.error("Could not remove query with id={}", id, ex);
				}
			}
		}
	}
}
