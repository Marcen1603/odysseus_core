package de.uniol.inf.is.odysseus.net.querydistribute.transmit;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.distribution.QueryDistributionException;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
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
		LOG.debug("PQL statement to be executed: {}", message.getPqlStatement());

		try {
			callExecutor(message, senderNode);
		} catch (Throwable t) {
			throw new QueryDistributionException("Could not execute query: " + t.getMessage(), t);
		}
	}

	private void callExecutor(AddQueryPartMessage message, IOdysseusNode senderNode) {
		List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, message.getTransCfgName());
		Optional<ParameterTransformationConfiguration> params = getParameterTransformationConfiguration(configuration);
		if (params.isPresent()) {
			params.get().getValue().addTypes(Sets.newHashSet(message.getMetadataTypes()));
		}

		String script = buildOdysseusScriptText(message);

		Collection<Integer> ids = executor.addQuery(script, "OdysseusScript", QueryDistributionPlugIn.getActiveSession(), message.getTransCfgName(), Context.empty(), configuration);

		QueryPartController.getInstance().registerAsSlave(ids, message.getSharedQueryID(), senderNode);
	}

	private static String buildOdysseusScriptText(AddQueryPartMessage message) {
		StringBuilder sb = new StringBuilder();

		Collection<String> metadataTypeNames = message.getMetadataTypes();
		Collection<Class<? extends IMetaAttribute>> metadataTypes = determineMetadataTypes(metadataTypeNames);
		Set<String> metadataNames = MetadataRegistry.getNames();

		for (Class<? extends IMetaAttribute> metadataType : metadataTypes) {
			for (String metadataName : metadataNames) {
				Optional<IMetaAttribute> metaAttribute = tryGetMetaAttribute(metadataName);
				if (metaAttribute.isPresent() && metadataType.equals(metaAttribute.get().getClass())) {
					sb.append("#NO_METADATA " + metadataName).append("\n");
					break;
				}
			}
		}

		sb.append("#PARSER PQL").append("\n");

		// TODO should not be set that way
		sb.append("#ADDQUERY").append("\n");
		sb.append(message.getPqlStatement()).append("\n");
		return sb.toString();
	}

	private static Optional<IMetaAttribute> tryGetMetaAttribute(String metadataName) {
		try {
			return Optional.fromNullable(MetadataRegistry.getMetadataTypeByName(metadataName));
		} catch (IllegalArgumentException e) {
			return Optional.absent();
		}
	}

	private static Collection<Class<? extends IMetaAttribute>> determineMetadataTypes(Collection<String> metadataTypeNames) {
		Collection<Class<? extends IMetaAttribute>> result = Lists.newArrayList();
		for (String metadataTypeName : metadataTypeNames) {
			try {
				IMetaAttribute metaAttribute = MetadataRegistry.getMetadataType(metadataTypeName);
				result.add(metaAttribute.getClass());
			} catch (Throwable t) {
				LOG.error("Could not determine metaAttribute of '{}'", metadataTypeName);
			}
		}
		return result;
	}

	private static Optional<ParameterTransformationConfiguration> getParameterTransformationConfiguration(List<IQueryBuildSetting<?>> settings) {
		for (IQueryBuildSetting<?> s : settings) {
			if (s instanceof ParameterTransformationConfiguration) {
				return Optional.of((ParameterTransformationConfiguration) s);
			}
		}

		return Optional.absent();
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
