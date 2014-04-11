package de.uniol.inf.is.odysseus.peer.distribute.part;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
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

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if( message instanceof AddQueryPartMessage ) {
			LOG.debug("Received AddQueryPartMessage");

			AddQueryPartMessage addQueryPartMessage = (AddQueryPartMessage) message;

			if (ackedQueryPartIDs.contains(addQueryPartMessage.getQueryPartID())) {
				sendQueryAddAck(senderPeer, addQueryPartMessage);
				return;
			}

			if (failedQueryPartIDs.containsKey(addQueryPartMessage.getQueryPartID())) {
				sendQueryAddFail(senderPeer, addQueryPartMessage, failedQueryPartIDs.get(addQueryPartMessage.getQueryPartID()));
				return;
			}

			try {
				QueryPartReceiver.getInstance().addQueryPart(addQueryPartMessage);

				sendQueryAddAck(senderPeer, addQueryPartMessage);
				ackedQueryPartIDs.add(addQueryPartMessage.getQueryPartID());

			} catch (QueryDistributionException e) {

				sendQueryAddFail(senderPeer, addQueryPartMessage, e.getMessage());
				failedQueryPartIDs.put(addQueryPartMessage.getQueryPartID(), e.getMessage());
			}
		} else if ( message instanceof AbortQueryPartAddMessage ) {
			AbortQueryPartAddMessage abortMessage = (AbortQueryPartAddMessage) message;

			QueryPartController.getInstance().removeSharedQuery(abortMessage.getSharedQueryID());

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
		List<String> neededSources = determineNeededSources(message);

		if (neededSources.isEmpty()) {
			try {
				LOG.debug("All source available. Calling executor.");
				callExecutor(message);
			} catch (Throwable t) {
				throw new QueryDistributionException("Could not execute query: " + t.getMessage());
			}
		} else {
			throw new QueryDistributionException("Not all sources are available: " + neededSources);
		}
	}

	private List<String> determineNeededSources(AddQueryPartMessage message) {
		List<String> neededSources = Lists.newArrayList();
		ISession session = PeerDistributePlugIn.getActiveSession();

		List<IExecutorCommand> queries = compiler.translateQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), getDataDictionary(), Context.empty());
		for (IExecutorCommand q : queries) {

			if (q instanceof CreateQueryCommand) {
				ILogicalQuery query = ((CreateQueryCommand) q).getQuery();

				List<ILogicalOperator> operators = Lists.newArrayList();
				RestructHelper.collectOperators(query.getLogicalPlan(), operators);

				for (ILogicalOperator operator : operators) {

					if (!(operator instanceof AbstractAccessAO)) {
						continue;
					}
					String source = ((AbstractAccessAO) operator).getName();

					// TODO not a good solution to concatenate user name and
					// source name
					if (getDataDictionary().containsViewOrStream(session.getUser().getName() + "." + source, session)) {
						break;
					}

					neededSources.add(source);
					LOG.debug("Source {} needed for query {}", source, message.getPqlStatement());
				}
			}

		}
		return neededSources;
	}

	private void callExecutor(AddQueryPartMessage message) {
		List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message.getTransCfgName());
		Collection<Integer> ids = executor.addQuery(message.getPqlStatement(), "PQL", PeerDistributePlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);

		QueryPartController.getInstance().registerAsSlave(ids, message.getSharedQueryID());
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
