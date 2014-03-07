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
	private static final byte ASK_BYTE = 54;
	private static final byte ANSWER_BYTE = 55;

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

		peerCommunicator.addListener(this);

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

		peerCommunicator.removeListener(this);
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

		byte[] message = buildAskUsageMessage();
		try {
			usageMap.remove(peerID);
			peerCommunicator.send(peerID, message);
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

	private static byte[] buildAskUsageMessage() {
		byte[] message = new byte[1];
		message[0] = ASK_BYTE;
		return message;
	}

	@Override
	public Collection<PeerID> getRemotePeerIDs() {
		return peerCommunicator.getConnectedPeers();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, byte[] message) {
		if (message[0] == ASK_BYTE) {
			IResourceUsage localUsage = getLocalResourceUsage();
			ByteBuffer bb = toByteBuffer(localUsage);

			try {
				communicator.send(senderPeer, bb.array());
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send aswer to resource usage asking", e);
			}
		} else if (message[0] == ANSWER_BYTE) {
			ByteBuffer bb = ByteBuffer.wrap(message);
			IResourceUsage remoteUsage = toResourceUsage(bb);

			synchronized (usageMap) {
				usageMap.put(senderPeer, remoteUsage);
			}
		}
	}

	private static ByteBuffer toByteBuffer(IResourceUsage localUsage) {
		ByteBuffer bb = ByteBuffer.allocate(65);
		bb.put(ANSWER_BYTE);
		bb.putLong(localUsage.getMemFreeBytes());
		bb.putLong(localUsage.getMemMaxBytes());
		bb.putDouble(localUsage.getCpuFree());
		bb.putDouble(localUsage.getCpuMax());
		bb.putInt(localUsage.getRunningQueriesCount());
		bb.putInt(localUsage.getStoppedQueriesCount());
		bb.putDouble(localUsage.getNetBandwidthMax());
		bb.putDouble(localUsage.getNetOutputRate());
		bb.putDouble(localUsage.getNetInputRate());
		bb.flip();
		return bb;
	}
	
	private static IResourceUsage toResourceUsage(ByteBuffer bb) {
		bb.get();

		long memFree = bb.getLong();
		long memMax = bb.getLong();
		double cpuFree = bb.getDouble();
		double cpuMax = bb.getDouble();
		int runQ = bb.getInt();
		int stopQ = bb.getInt();
		double netMax = bb.getDouble();
		double netOut = bb.getDouble();
		double netIn = bb.getDouble();

		return new ResourceUsage(memFree, memMax, cpuFree, cpuMax, runQ, stopQ, netMax, netOut, netIn);
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

			long inputTotal = net != null ? net.getRxBytes() : 0;
			long outputTotal = net != null ? net.getTxBytes() : 0;

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
