package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class PeerResourceUsageManager implements IPeerResourceUsageManager, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerResourceUsageManager.class);

	private static final int RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS = 15000;
	private static final SigarWrapper SIGAR_WRAPPER = new SigarWrapper();
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final ExecutorService FUTURE_SERVICE = Executors.newCachedThreadPool();
	private static final long MAX_WAIT = 5000;
	private static final long MAX_WAIT_STEP = 200;
	private static final Callable<Optional<IResourceUsage>> EMPTY_RESOURCE_USAGE = new Callable<Optional<IResourceUsage>>() {
		@Override
		public Optional<IResourceUsage> call() throws Exception {
			return Optional.absent();
		}
	};

	private static PeerResourceUsageManager instance;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor serverExecutor;
	private static IP2PDictionary p2pDictionary;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final UsageStatisticCollector usageCollector = new UsageStatisticCollector();

	private RepeatingJobThread localUsageChecker;

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
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		
		peerCommunicator.registerMessageType(AskUsageMessage.class);
		peerCommunicator.registerMessageType(AnswerUsageMessage.class);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.unregisterMessageType(AskUsageMessage.class);
			peerCommunicator.unregisterMessageType(AnswerUsageMessage.class);
			
			peerCommunicator = null;
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
	public void activate() {
		instance = this;
		LOG.debug("Activated");

		peerCommunicator.addListener(this, AskUsageMessage.class);
		peerCommunicator.addListener(this, AnswerUsageMessage.class);

		localUsageChecker = new RepeatingJobThread(RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS, "Local usage checker") {
			@Override
			public void doJob() {
				updateLocalUsage();
			};
		};
		localUsageChecker.start();
	}

	// called by OSGi-DS
	public void deactivate() {
		localUsageChecker.stopRunning();

		instance = null;
		LOG.debug("Deactivated");

		peerCommunicator.removeListener(this, AskUsageMessage.class);
		peerCommunicator.removeListener(this, AnswerUsageMessage.class);
	}

	public static PeerResourceUsageManager getInstance() {
		return instance;
	}

	@Override
	public Future<Optional<IResourceUsage>> getRemoteResourceUsage(final PeerID peerID) {
		Preconditions.checkNotNull(peerID, "PeerID to get current resource usage must not be null!");
		if (peerCommunicator == null) {
			return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
		}

		try {
			usageMap.remove(peerID);
			peerCommunicator.send(peerID, new AskUsageMessage());
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send message for asking for remote resource usage", e);
			return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
		}

		return FUTURE_SERVICE.submit(new Callable<Optional<IResourceUsage>>() {
			@Override
			public Optional<IResourceUsage> call() throws Exception {
				IResourceUsage remoteUsage = null;
				synchronized (usageMap) {
					remoteUsage = usageMap.get(peerID);
				}

				long waited = 0;
				while (remoteUsage == null) {
					if (waited > MAX_WAIT) {
						// takes too long
						return Optional.absent();
					}

					Thread.sleep(MAX_WAIT_STEP);
					waited += MAX_WAIT_STEP;

					synchronized (usageMap) {
						remoteUsage = usageMap.get(peerID);
					}
				}

				return Optional.of(remoteUsage);
			}
		});
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof AskUsageMessage) {
			IResourceUsage localUsage = getLocalResourceUsage();
			AnswerUsageMessage answer = new AnswerUsageMessage(localUsage);

			try {
				communicator.send(senderPeer, answer);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send aswer to resource usage asking", e);
			}
		} else if (message instanceof AnswerUsageMessage) {
			AnswerUsageMessage answer = (AnswerUsageMessage)message;

			synchronized (usageMap) {
				usageMap.put(senderPeer, answer.getResourceUsage());
			}
		}
	}

	@Override
	public IResourceUsage getLocalResourceUsage() {
		updateLocalUsage();
		synchronized( usageCollector ) {
			return usageCollector.getCurrentResourceUsage();
		}
	}

	private void updateLocalUsage() {
		try {
			if (!p2pNetworkManager.isStarted() || serverExecutor == null) {
				return;
			}

			long totalMemory = RUNTIME.totalMemory();
			long freeMemory = RUNTIME.freeMemory();

			int runningQueries = 0;
			int stoppedQueries = 0;

			if (serverExecutor == null) {
				return;
			}

			for (IPhysicalQuery physicalQuery : serverExecutor.getExecutionPlan().getQueries()) {
				if (physicalQuery.isOpened()) {
					runningQueries++;
				} else {
					stoppedQueries++;
				}
			}
			
			synchronized( usageCollector ) {
				usageCollector.addStatistics(freeMemory, totalMemory, SIGAR_WRAPPER.getCpuFree(), SIGAR_WRAPPER.getCpuMax(), runningQueries, stoppedQueries, SIGAR_WRAPPER.getNetMax(), SIGAR_WRAPPER.getNetOutputRate(), SIGAR_WRAPPER.getNetInputRate());
			}
		} catch (Throwable t) {
			LOG.error("Cannot determine own resource usage", t);
		}
	}
}
