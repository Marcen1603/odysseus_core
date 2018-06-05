package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.querydistribute.activator.QueryDistributionPlugIn;

public class QueryPartReceiver implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartReceiver.class);

	private static QueryPartReceiver instance;

	private static IServerExecutor executor;
	private static ICompiler compiler;
	private static IOdysseusNodeCommunicator peerCommunicator;

	private final Object monitor = new Object();

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
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		peerCommunicator = serv;

		peerCommunicator.registerMessageType(AddQueryPartMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddAckMessage.class);
		peerCommunicator.registerMessageType(QueryPartAddFailMessage.class);
		peerCommunicator.registerMessageType(AbortQueryPartAddMessage.class);

		peerCommunicator.addListener(this, AddQueryPartMessage.class);
		peerCommunicator.addListener(this, AbortQueryPartAddMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, AddQueryPartMessage.class);
			peerCommunicator.removeListener(this, AbortQueryPartAddMessage.class);

			peerCommunicator.unregisterMessageType(AddQueryPartMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddAckMessage.class);
			peerCommunicator.unregisterMessageType(QueryPartAddFailMessage.class);
			peerCommunicator.unregisterMessageType(AbortQueryPartAddMessage.class);

			peerCommunicator = null;
		}
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		synchronized (monitor) {
			if (message instanceof AddQueryPartMessage) {
				LOG.debug("Received AddQueryPartMessage");

				AddQueryPartMessage addQueryPartMessage = (AddQueryPartMessage) message;
				try {
					addQueryPart(addQueryPartMessage, senderNode);
					sendQueryAddAck(senderNode, addQueryPartMessage);
				} catch (QueryDistributionException e) {
					LOG.error("Could not add query part", e);
					sendQueryAddFail(senderNode, addQueryPartMessage, e.getMessage());
				}
			} else if (message instanceof AbortQueryPartAddMessage) {
				AbortQueryPartAddMessage abortMessage = (AbortQueryPartAddMessage) message;
				QueryPartController.getInstance().removeSharedQuery(abortMessage.getSharedQueryID());
			}
		}
	}

	private static void sendQueryAddAck(IOdysseusNode senderNode, AddQueryPartMessage addQueryPartMessage) {
		QueryPartAddAckMessage ackMessage = new QueryPartAddAckMessage(addQueryPartMessage);
		try {
			peerCommunicator.send(senderNode, ackMessage);
		} catch (OdysseusNodeCommunicationException e) {
			LOG.error("Send query part add ack message failed", e);
		}
	}

	private static void sendQueryAddFail(IOdysseusNode senderNode, AddQueryPartMessage addQueryPartMessage, String message) {
		QueryPartAddFailMessage failMessage = new QueryPartAddFailMessage(addQueryPartMessage.getQueryPartID(), message);
		try {
			peerCommunicator.send(senderNode, failMessage);
		} catch (OdysseusNodeCommunicationException e) {
			LOG.error("Send query part add fail message failed", e);
		}
	}

	public void addQueryPart(AddQueryPartMessage message, IOdysseusNode senderNode) throws QueryDistributionException {
		LOG.debug("PQL statement to be executed: {}", message.getQueryText());

		try {
			callExecutor(message, senderNode);
		} catch (Throwable t) {
			throw new QueryDistributionException("Could not execute query: " + t.getMessage(), t);
		}
	}

	private void callExecutor(AddQueryPartMessage message, IOdysseusNode senderNode) {
		List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message);
		Collection<Integer> ids = executor.addQuery(message.getQueryText(), message.getParser(), QueryDistributionPlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);
		QueryPartController.getInstance().registerAsSlave(ids, message.getSharedQueryID(), senderNode);
	}

	public static QueryPartReceiver getInstance() {
		return instance;
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, AddQueryPartMessage message) {
		final IQueryBuildConfigurationTemplate qbc = executor.getQueryBuildConfiguration(message.getTransCfgName());
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}
}
