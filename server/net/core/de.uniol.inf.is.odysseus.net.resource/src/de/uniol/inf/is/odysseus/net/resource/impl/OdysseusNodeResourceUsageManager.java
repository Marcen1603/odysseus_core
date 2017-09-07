package de.uniol.inf.is.odysseus.net.resource.impl;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;

public final class OdysseusNodeResourceUsageManager implements IOdysseusNodeResourceUsageManager, IOdysseusNodeCommunicatorListener {

	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeResourceUsageManager.class);

	private static final int RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS = 7000;
	private static final SigarWrapper SIGAR_WRAPPER = new SigarWrapper();

	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final ExecutorService FUTURE_SERVICE = Executors.newCachedThreadPool();
	private static final long MAX_WAIT = 5000;
	private static final long MAX_WAIT_STEP = 200;
	private static final long MAX_ASK_WAITING_TIME = 3000;
	private static final Callable<Optional<IResourceUsage>> EMPTY_RESOURCE_USAGE = new Callable<Optional<IResourceUsage>>() {
		@Override
		public Optional<IResourceUsage> call() throws Exception {
			return Optional.absent();
		}
	};

	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static IServerExecutor serverExecutor;

	private final Map<IOdysseusNode, IResourceUsage> usageMap = Maps.newHashMap();
	private final Map<IOdysseusNode, Long> timestampMap = Maps.newHashMap();
	private final UsageStatisticCollector usageCollector = new UsageStatisticCollector();
	private final Map<IOdysseusNode, Long> askingNodeMap = Maps.newHashMap();

	private long lastUsageUpdateTimestamp;

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;

		nodeCommunicator.registerMessageType(AskUsageMessage.class);
		nodeCommunicator.registerMessageType(AnswerUsageMessage.class);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.unregisterMessageType(AskUsageMessage.class);
			nodeCommunicator.unregisterMessageType(AnswerUsageMessage.class);

			nodeCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Activated");

		nodeCommunicator.addListener(this, AskUsageMessage.class);
		nodeCommunicator.addListener(this, AnswerUsageMessage.class);
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Deactivated");

		nodeCommunicator.removeListener(this, AskUsageMessage.class);
		nodeCommunicator.removeListener(this, AnswerUsageMessage.class);
	}

	@Override
	public Future<Optional<IResourceUsage>> getRemoteResourceUsage(final IOdysseusNode odysseusNode, boolean forceNetwork) {
		Preconditions.checkNotNull(odysseusNode, "node to get current resource usage must not be null!");

		LOG.debug("Getting remote usage of node {}", odysseusNode);

		if (nodeCommunicator == null) {
			return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
		}

		synchronized (askingNodeMap) {
			Long askTS = askingNodeMap.get(odysseusNode);
			if (askTS != null && System.currentTimeMillis() - askTS < MAX_ASK_WAITING_TIME) {
				LOG.debug("Already asking...");

				IResourceUsage usage = null;
				synchronized (usageMap) {
					usage = usageMap.get(odysseusNode);
				}

				if (usage == null) {
					return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
				}
				final IResourceUsage finalUsage = usage;
				return FUTURE_SERVICE.submit(new Callable<Optional<IResourceUsage>>() {
					@Override
					public Optional<IResourceUsage> call() throws Exception {
						return Optional.of(finalUsage);
					}
				});
			}

			try {
				Long ts = null;
				synchronized (timestampMap) {
					ts = timestampMap.get(odysseusNode);
				}

				IResourceUsage usage = null;
				synchronized (usageMap) {
					usage = usageMap.get(odysseusNode);
				}

				if (forceNetwork || usage == null || ts == null || System.currentTimeMillis() - ts > RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS) {
					LOG.debug("Remote resource usage is too old");

					synchronized (askingNodeMap) {
						askingNodeMap.put(odysseusNode, System.currentTimeMillis());
					}

					LOG.debug("ASKING NODE {}", odysseusNode);

					nodeCommunicator.send(odysseusNode, new AskUsageMessage(forceNetwork));
				} else {
					LOG.debug("Use cached resource usage");

					final IResourceUsage finalUsage = usage;
					return FUTURE_SERVICE.submit(new Callable<Optional<IResourceUsage>>() {
						@Override
						public Optional<IResourceUsage> call() throws Exception {
							return Optional.of(finalUsage);
						}
					});
				}

			} catch (OdysseusNodeCommunicationException e) {
				LOG.warn("Could not send message for asking for remote resource usage", e);
				return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
			}
		}

		return FUTURE_SERVICE.submit(new Callable<Optional<IResourceUsage>>() {
			@Override
			public Optional<IResourceUsage> call() throws Exception {
				Long timestamp;
				synchronized (timestampMap) {
					timestamp = timestampMap.get(odysseusNode);
				}

				long waited = 0;
				while (timestamp == null || System.currentTimeMillis() - timestamp > RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS) {
					if (waited > MAX_WAIT) {
						// takes too long
						// use cached one
						return Optional.fromNullable(usageMap.get(odysseusNode));
					}

					Thread.sleep(MAX_WAIT_STEP);
					waited += MAX_WAIT_STEP;

					synchronized (timestampMap) {
						timestamp = timestampMap.get(odysseusNode);
					}
				}

				return Optional.of(usageMap.get(odysseusNode));
			}
		});
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof AskUsageMessage) {
			LOG.debug("Getting local resource usage for remote node");

			AskUsageMessage msg = (AskUsageMessage)message;
			IResourceUsage localUsage = getLocalResourceUsage(msg.isForce());
			AnswerUsageMessage answer = new AnswerUsageMessage(localUsage);

			try {
				communicator.send(senderNode, answer);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send aswer to resource usage asking", e);
			}
		} else if (message instanceof AnswerUsageMessage) {
			AnswerUsageMessage answer = (AnswerUsageMessage) message;

			synchronized (usageMap) {
				usageMap.put(senderNode, answer.getResourceUsage());
			}
			synchronized (askingNodeMap) {
				askingNodeMap.remove(senderNode);
			}
			synchronized (timestampMap) {
				timestampMap.put(senderNode, System.currentTimeMillis());
			}
		}
	}

	@Override
	public IResourceUsage getLocalResourceUsage() {
		return getLocalResourceUsage(false);
	}

	private IResourceUsage getLocalResourceUsage(boolean force ) {
		synchronized (usageCollector) {

			if( force || System.currentTimeMillis() - lastUsageUpdateTimestamp > RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS ) {
				lastUsageUpdateTimestamp = System.currentTimeMillis();
				updateLocalUsage();
			}

			return usageCollector.getCurrentResourceUsage();
		}
	}

	private void updateLocalUsage() {
		try {
			LOG.debug("Updating local resource usage");
			if (serverExecutor == null) {
				return;
			}

			long totalMemory = RUNTIME.totalMemory();
			long freeMemory = RUNTIME.freeMemory();

			int runningQueries = 0;
			int stoppedQueries = 0;

			if (serverExecutor == null) {
				return;
			}

			for (IPhysicalQuery physicalQuery : serverExecutor.getExecutionPlan(superUser).getQueries(superUser)) {
				if (physicalQuery.isOpened()) {
					runningQueries++;
				} else {
					stoppedQueries++;
				}
			}

			int remoteNodeCount = nodeCommunicator.getDestinationNodes().size();

			synchronized (usageCollector) {
				usageCollector.addStatistics(freeMemory, totalMemory, SIGAR_WRAPPER.getCpuFree(), SIGAR_WRAPPER.getCpuMax(), runningQueries, stoppedQueries, remoteNodeCount, SIGAR_WRAPPER.getNetMax(), SIGAR_WRAPPER.getNetOutputRate(), SIGAR_WRAPPER.getNetInputRate());
			}
		} catch (Throwable t) {
			LOG.error("Cannot determine own resource usage", t);
		}
	}
}
