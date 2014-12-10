package de.uniol.inf.is.odysseus.peer.distribute.part;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.PeerDistributePlugIn;
import de.uniol.inf.is.odysseus.peer.distribute.message.AbortQueryPartAddAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.AbortQueryPartAddMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.AddQueryPartMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.QueryPartAddAckMessage;
import de.uniol.inf.is.odysseus.peer.distribute.message.QueryPartAddFailMessage;

public class QueryPartReceiver implements IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartReceiver.class);

	private static QueryPartReceiver instance;

	private static IServerExecutor executor;
	private static ICompiler compiler;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IPeerDictionary peerDictionary;
	private static IQueryPartController controller;

	private static Collection<Integer> ackedQueryPartIDs = Lists.newLinkedList();
	private static Map<Integer, String> failedQueryPartIDs = Maps.newConcurrentMap();

	public QueryPartReceiver() {
		instance = this;
	}

	// called by OSGi-DS
	public static void bindCompiler(ICompiler serv) {
		compiler = serv;
	}

	// called by OSGi-DS
	public static void unbindCompiler(ICompiler serv) {
		if (compiler == serv) {
			compiler = null;
		}
	}

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

		peerCommunicator.registerMessageType(AddQueryPartMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddAckMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddFailMessage.class);
		peerCommunicator.registerMessageType(AbortQueryPartAddMessage.class);
		peerCommunicator.registerMessageType(AbortQueryPartAddAckMessage.class);

		peerCommunicator.addListener(this, AddQueryPartMessage.class);
		peerCommunicator.addListener(this, AbortQueryPartAddMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, AddQueryPartMessage.class);
			peerCommunicator.removeListener(this, AbortQueryPartAddMessage.class);

			peerCommunicator.unregisterMessageType(AddQueryPartMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddAckMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddFailMessage.class);
			peerCommunicator.unregisterMessageType(AbortQueryPartAddMessage.class);
			peerCommunicator.unregisterMessageType(AbortQueryPartAddAckMessage.class);

			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindQueryPartController(IQueryPartController serv) {
		controller = serv;
	}

	// called by OSGi-DS
	public static void unbindQueryPartController(IQueryPartController serv) {
		if (controller == serv) {
			controller = null;
		}
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof AddQueryPartMessage) {
			LOG.debug("Received AddQueryPartMessage");

			AddQueryPartMessage addQueryPartMessage = (AddQueryPartMessage) message;
			synchronized (ackedQueryPartIDs) {
				if (ackedQueryPartIDs.contains(addQueryPartMessage.getQueryPartID())) {
					sendQueryAddAck(senderPeer, addQueryPartMessage);
					return;
				}

				if (failedQueryPartIDs.containsKey(addQueryPartMessage.getQueryPartID())) {
					sendQueryAddFail(senderPeer, addQueryPartMessage, failedQueryPartIDs.get(addQueryPartMessage.getQueryPartID()));
					return;
				}

				try {
					addQueryPart(addQueryPartMessage);
					ackedQueryPartIDs.add(addQueryPartMessage.getQueryPartID());

					sendQueryAddAck(senderPeer, addQueryPartMessage);

				} catch (QueryDistributionException e) {

					LOG.error("Could not add query part", e);
					sendQueryAddFail(senderPeer, addQueryPartMessage, e.getMessage());
					failedQueryPartIDs.put(addQueryPartMessage.getQueryPartID(), e.getMessage());
				}
			}
		} else if (message instanceof AbortQueryPartAddMessage) {
			AbortQueryPartAddMessage abortMessage = (AbortQueryPartAddMessage) message;

			((QueryPartController)controller).removeSharedQuery(abortMessage.getSharedQueryID());

			sendAbortAck(senderPeer, abortMessage.getSharedQueryID());
		}
	}

	private static void sendQueryAddAck(PeerID senderPeer, AddQueryPartMessage addQueryPartMessage) {
		QueryPartAddAckMessage ackMessage = new QueryPartAddAckMessage(addQueryPartMessage);
		try {
			peerCommunicator.send(senderPeer, ackMessage);
		} catch (PeerCommunicationException e) {
			LOG.debug("Send query part add ack message failed", e);
		}
	}

	private static void sendQueryAddFail(PeerID senderPeer, AddQueryPartMessage addQueryPartMessage, String message) {
		QueryPartAddFailMessage failMessage = new QueryPartAddFailMessage(addQueryPartMessage.getQueryPartID(), message);
		try {
			peerCommunicator.send(senderPeer, failMessage);
		} catch (PeerCommunicationException e) {
			LOG.debug("Send query part add fail message failed", e);
		}
	}

	private static void sendAbortAck(PeerID senderPeer, ID sharedQueryID) {
		AbortQueryPartAddAckMessage ack = new AbortQueryPartAddAckMessage(sharedQueryID);
		try {
			peerCommunicator.send(senderPeer, ack);
		} catch (PeerCommunicationException e) {
			LOG.debug("Send abort query part add ack message failed", e);
		}
	}

	public void addQueryPart(AddQueryPartMessage message) throws QueryDistributionException {
		LOG.debug("PQL statement to be executed: {}", message.getPqlStatement());

		try {
			checkNeededSources(message);
			callExecutor(message);
		} catch (Throwable t) {
			throw new QueryDistributionException("Could not execute query: " + t.getMessage(), t);
		}
	}

	private void checkNeededSources(AddQueryPartMessage message) throws QueryDistributionException {
		ISession session = PeerDistributePlugIn.getActiveSession();
		System.err.println(message.getPqlStatement());
		List<IExecutorCommand> queries = compiler.translateQuery(message.getPqlStatement(), "PQL", session, getDataDictionary(), Context.empty());
		
		for (IExecutorCommand q : queries) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();

				List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);

				for (ILogicalOperator operator : operators) {
					if( operator instanceof StreamAO ) {
						StreamAO streamAO = (StreamAO)operator;
						
						String peerIDString = streamAO.getNode();
						String sourceName = streamAO.getStreamname().getResourceName();
						
						Optional<SourceAdvertisement> optSrcAdv = findSourceAdvertisement(sourceName, peerIDString);
						if( optSrcAdv.isPresent() ) {
							if( p2pDictionary.isImported(optSrcAdv.get())) {
								String importedName = p2pDictionary.getImportedSourceName(optSrcAdv.get()).get();
								Resource resource = new Resource(session.getUser(), importedName);
								
								if (getDataDictionary().containsViewOrStream(resource, session)) {
									streamAO.setSourceName(resource);
									
								} else {
									throw new QueryDistributionException("Source " + sourceName + " of peer " + peerDictionary.getRemotePeerName(peerIDString) + " (locally known as " + importedName + ") is not known in local data dictionary!");
								}
							} else {
								throw new QueryDistributionException("Source " + sourceName + " of peer " + peerDictionary.getRemotePeerName(peerIDString) + " is not imported!");
							}
						} else {
							throw new QueryDistributionException("Source " + sourceName + " of peer " + peerDictionary.getRemotePeerName(peerIDString) + " is not known!");
						}
					}
				}
			}
		}
	}

	private static Optional<SourceAdvertisement> findSourceAdvertisement(String sourceName, String peerIDString) {
		Collection<SourceAdvertisement> srcAdvs = p2pDictionary.getSources(sourceName);
		for( SourceAdvertisement srcAdv : srcAdvs ) {
			if( srcAdv.getPeerID().toString().equals(peerIDString)) {
				return Optional.of(srcAdv);
			}
		}
		return Optional.absent();
	}

	private void callExecutor(AddQueryPartMessage message) {
		List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message.getTransCfgName());
		Optional<ParameterTransformationConfiguration> params = getParameterTransformationConfiguration(configuration);
		if (params.isPresent()) {
			params.get().getValue().addTypes(Sets.newHashSet(message.getMetadataTypes()));
		}

		String script = buildOdysseusScriptText(message);
		Collection<Integer> ids = executor.addQuery(script, "OdysseusScript", PeerDistributePlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);
		
		((QueryPartController)controller).registerAsSlave(ids, message.getSharedQueryID());
	}

	private static String buildOdysseusScriptText(AddQueryPartMessage message) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("#PARSER PQL").append("\n");
		
		String queryName = message.getQueryName();
		if( !Strings.isNullOrEmpty(queryName)) {
			sb.append("#QNAME ").append(queryName).append("\n");
		}
		// TODO should not be set that way
		sb.append("#ADDQUERY").append("\n");
		sb.append(message.getPqlStatement()).append("\n");
		return sb.toString();
	}

	private static Optional<ParameterTransformationConfiguration> getParameterTransformationConfiguration(List<IQueryBuildSetting<?>> settings) {
		for (IQueryBuildSetting<?> s : settings) {
			if (s instanceof ParameterTransformationConfiguration) {
				return Optional.of((ParameterTransformationConfiguration) s);
			}
		}

		return Optional.absent();
	}

	public IDataDictionary getDataDictionary() {
		return DataDictionaryProvider.getDataDictionary(PeerDistributePlugIn.getActiveSession().getTenant());
	}

	public static QueryPartReceiver getInstance() {
		return instance;
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfigurationTemplate qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}
}
