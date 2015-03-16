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
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.PeerDictionary;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class PeerResourceUsageManager implements IPeerResourceUsageManager, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerResourceUsageManager.class);

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

	private static PeerResourceUsageManager instance;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor serverExecutor;
	private static IPeerDictionary peerDictionary;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final Map<PeerID, Long> timestampMap = Maps.newHashMap();
	private final UsageStatisticCollector usageCollector = new UsageStatisticCollector();
	private final Map<PeerID, Long> askingPeerMap = Maps.newHashMap();
	
	private long lastUsageUpdateTimestamp;

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
	public void activate() {
		instance = this;
		LOG.debug("Activated");

		peerCommunicator.addListener(this, AskUsageMessage.class);
		peerCommunicator.addListener(this, AnswerUsageMessage.class);
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		LOG.debug("Deactivated");

		peerCommunicator.removeListener(this, AskUsageMessage.class);
		peerCommunicator.removeListener(this, AnswerUsageMessage.class);
	}

	public static PeerResourceUsageManager getInstance() {
		return instance;
	}

	@Override
	public Future<Optional<IResourceUsage>> getRemoteResourceUsage(final PeerID peerID, boolean forceNetwork) {
		Preconditions.checkNotNull(peerID, "PeerID to get current resource usage must not be null!");

		LOG.debug("Getting remote usage of peer {}", PeerDictionary.getInstance().getRemotePeerName(peerID));

		if (peerCommunicator == null) {
			return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
		}

		synchronized (askingPeerMap) {
			Long askTS = askingPeerMap.get(peerID);
			if (askTS != null && System.currentTimeMillis() - askTS < MAX_ASK_WAITING_TIME) {
				LOG.debug("Already asking...");
				
				IResourceUsage usage = null;
				synchronized (usageMap) {
					usage = usageMap.get(peerID);
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
					ts = timestampMap.get(peerID);
				}

				IResourceUsage usage = null;
				synchronized (usageMap) {
					usage = usageMap.get(peerID);
				}

				if (forceNetwork || usage == null || ts == null || System.currentTimeMillis() - ts > RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS) {
					LOG.debug("Remote resource usage is too old");

					synchronized (askingPeerMap) {
						askingPeerMap.put(peerID, System.currentTimeMillis());
					}
					
					LOG.debug("ASKING PEER {}", PeerDictionary.getInstance().getRemotePeerName(peerID));
					
					peerCommunicator.send(peerID, new AskUsageMessage(forceNetwork));
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

			} catch (PeerCommunicationException e) {
				LOG.error("Could not send message for asking for remote resource usage", e);
				return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
			}
		}

		return FUTURE_SERVICE.submit(new Callable<Optional<IResourceUsage>>() {
			@Override
			public Optional<IResourceUsage> call() throws Exception {
				Long timestamp;
				synchronized (timestampMap) {
					timestamp = timestampMap.get(peerID);
				}

				long waited = 0;
				while (timestamp == null || System.currentTimeMillis() - timestamp > RESOURCE_USAGE_UPDATE_INTERVAL_MILLIS) {
					if (waited > MAX_WAIT) {
						// takes too long
						// use cached one
						return Optional.fromNullable(usageMap.get(peerID));
					}

					Thread.sleep(MAX_WAIT_STEP);
					waited += MAX_WAIT_STEP;

					synchronized (timestampMap) {
						timestamp = timestampMap.get(peerID);
					}
				}

				return Optional.of(usageMap.get(peerID));
			}
		});
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof AskUsageMessage) {
			LOG.debug("Getting local resource usage for remote peer");
			
			AskUsageMessage msg = (AskUsageMessage)message;
			IResourceUsage localUsage = getLocalResourceUsage(msg.isForce());
			AnswerUsageMessage answer = new AnswerUsageMessage(localUsage);

			try {
				communicator.send(senderPeer, answer);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send aswer to resource usage asking", e);
			}
		} else if (message instanceof AnswerUsageMessage) {
			AnswerUsageMessage answer = (AnswerUsageMessage) message;

			synchronized (usageMap) {
				usageMap.put(senderPeer, answer.getResourceUsage());
			}
			synchronized (askingPeerMap) {
				askingPeerMap.remove(senderPeer);
			}
			synchronized (timestampMap) {
				timestampMap.put(senderPeer, System.currentTimeMillis());
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

			int remotePeerCount = peerDictionary.getRemotePeerIDs().size();

			synchronized (usageCollector) {
				usageCollector.addStatistics(freeMemory, totalMemory, SIGAR_WRAPPER.getCpuFree(), SIGAR_WRAPPER.getCpuMax(), runningQueries, stoppedQueries, remotePeerCount, SIGAR_WRAPPER.getNetMax(), SIGAR_WRAPPER.getNetOutputRate(), SIGAR_WRAPPER.getNetInputRate());
			}
		} catch (Throwable t) {
			LOG.error("Cannot determine own resource usage", t);
		}
	}
}
