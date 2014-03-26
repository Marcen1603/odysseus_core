package de.uniol.inf.is.odysseus.peer.resource.impl;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.peer.PeerID;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class PeerResourceUsageManager implements IPeerResourceUsageManager, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerResourceUsageManager.class);

	private static final Sigar SIGAR = new Sigar();
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

	private static final double DEFAULT_BANDWIDTH_KB = 1024.0;
	
	private static final int ASK_BYTE = 54;
	private static final int ANSWER_BYTE = 55;

	private static PeerResourceUsageManager instance;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IServerExecutor serverExecutor;

	private final Map<PeerID, IResourceUsage> usageMap = Maps.newHashMap();
	private final UsageStatisticCollector usageCollector = new UsageStatisticCollector();

	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;

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
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
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
	public void activate() {
		instance = this;
		LOG.debug("Activated");

		peerCommunicator.addListener(ASK_BYTE, this);
		peerCommunicator.addListener(ANSWER_BYTE, this);

		localUsageChecker = new RepeatingJobThread(1500, "Local usage checker") {
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

		peerCommunicator.removeListener(ASK_BYTE, this);
		peerCommunicator.removeListener(ANSWER_BYTE, this);
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

		if (!peerCommunicator.isConnected(peerID)) {
			return FUTURE_SERVICE.submit(EMPTY_RESOURCE_USAGE);
		}

		byte[] message = new byte[0];
		try {
			usageMap.remove(peerID);
			peerCommunicator.send(peerID, ASK_BYTE, message);
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send message for asking for remote resource usage", e);
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
	public Collection<PeerID> getRemotePeerIDs() {
		return peerCommunicator.getConnectedPeers();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, int messageID, byte[] message) {
		if (messageID == ASK_BYTE) {
			IResourceUsage localUsage = getLocalResourceUsage();
			ByteBuffer bb = ResourceUsageBytesConverter.toByteBuffer(localUsage);

			try {
				communicator.send(senderPeer, ANSWER_BYTE, bb.array());
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send aswer to resource usage asking", e);
			}
		} else if (messageID == ANSWER_BYTE) {
			ByteBuffer bb = ByteBuffer.wrap(message);
			IResourceUsage remoteUsage = ResourceUsageBytesConverter.toResourceUsage(bb);

			synchronized (usageMap) {
				usageMap.put(senderPeer, remoteUsage);
			}
		}
	}

	@Override
	public IResourceUsage getLocalResourceUsage() {
		return usageCollector.getCurrentResourceUsage();
	}

	private void updateLocalUsage() {
		try {
			if (!p2pNetworkManager.isStarted() || serverExecutor == null) {
				return;
			}

			CpuPerc perc = SIGAR.getCpuPerc();
			double cpuMax = SIGAR.getCpuPercList().length;
			double cpuFree = cpuMax - (perc != null ? perc.getUser() : 0.0) * cpuMax;
			cpuFree = Math.max(0, Math.min(cpuFree, cpuMax));
			if (Double.isNaN(cpuFree)) {
				return;
			}

			long totalMemory = RUNTIME.totalMemory();
			long freeMemory = RUNTIME.freeMemory();

			String interfaceName = SIGAR.getNetInterfaceConfig(null).getName();
			NetInterfaceStat net = SIGAR.getNetInterfaceStat(interfaceName);
			long rawSpeed = net.getSpeed();
			double bandwidthInKBs = rawSpeed >= 0 ? rawSpeed / 1024.0 : DEFAULT_BANDWIDTH_KB;

			long inputTotal = net != null ? net.getRxBytes() : previousInputTotal;
			long outputTotal = net != null ? net.getTxBytes() : previousOutputTotal;

			double netInputRate = (inputTotal - previousInputTotal) / 1024;
			double netOutputRate = (outputTotal - previousOutputTotal) / 1024;
			previousInputTotal = inputTotal;
			previousOutputTotal = outputTotal;

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

			usageCollector.addStatistics(freeMemory, totalMemory, cpuFree, cpuMax, runningQueries, stoppedQueries, bandwidthInKBs, netOutputRate, netInputRate);
		} catch (Throwable t) {
			LOG.error("Cannot determine own resource usage", t);
		}
	}

	@Override
	public PeerID getLocalPeerID() {
		return p2pNetworkManager.getLocalPeerID();
	}
}
