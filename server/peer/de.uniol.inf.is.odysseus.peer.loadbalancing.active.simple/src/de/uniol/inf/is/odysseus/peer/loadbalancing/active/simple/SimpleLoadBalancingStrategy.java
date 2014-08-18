package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

/**
 * The {@link SimpleLoadBalancingStrategy} defines a threshold for the load (CPU and storage). 
 * If the current load is above this threshold, it takes the first query it gets from {@link IExecutor} and 
 * allocates it using the allocator set by {@link #setAllocator(ILoadBalancingAllocator)}.
 * @author Michael Brand
 */
public class SimpleLoadBalancingStrategy implements ILoadBalancingStrategy, ILoadBalancingListener {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SimpleLoadBalancingStrategy.class);
	
	/**
	 * The name of this strategy.
	 */
	public static final String NAME = "Simple";
	
	/**
	 * The default threshold for the CPU load as percentage.
	 */
	public static final double DEFAULT_CPU_THRESHOLD = 0.1;
	
	/**
	 * The default threshold for the memory load as percentage.
	 */
	public static final double DEFAULT_MEM_THRESHOLD = 0.1;
	
	/**
	 * The default time between look ups of the local resources [ms].
	 */
	public static final long DEFAULT_RESOURCE_LOOKUP_TIME = 60000;
	
	/**
	 * The {@link Thread} to look up the current resource usage. <br />
	 * It will call {@link ILoadBalancingCommunicator#initiateLoadBalancing(PeerID, int)} if load balancing has to be done.
	 */
	private class SimpleLBThread extends Thread {
		
		/**
		 * The allocator to use.
		 */
		protected ILoadBalancingAllocator mAllocator;
		
		/**
		 * The communicator to use.
		 */
		protected ILoadBalancingCommunicator mCommunicator;
		
		/**
		 * The threshold for the CPU load as percentage.
		 */
		protected volatile double mCPUThreshold = DEFAULT_CPU_THRESHOLD;
		
		/**
		 * The threshold for the memory load as percentage.
		 */
		protected volatile double mMemThreshold = DEFAULT_MEM_THRESHOLD;
		
		/**
		 * The time between look ups of the local resources [ms].
		 */
		protected volatile long mResourceLookupTime = DEFAULT_RESOURCE_LOOKUP_TIME;
		
		/**
		 * The current resource usage.
		 */
		protected IResourceUsage mUsage;
		
		/**
		 * True, if a load balancing process is currently running.
		 */
		protected volatile boolean mCurrentlyBalancing = false;
		
		/**
		 * Compares the current with the last usage.
		 * @param currentUsage The current {@link IResourceUsage}.
		 * @param lastUsage The last known {@link IResourceUsage}.
		 * @return True, if CPU or storage usage have changed.
		 */
		private boolean hasUsageChanged(IResourceUsage currentUsage, IResourceUsage lastUsage) {
			
			return 	currentUsage.getCpuMax() != lastUsage.getCpuMax() ||
					currentUsage.getCpuFree() != lastUsage.getCpuFree() ||
					currentUsage.getMemMaxBytes() != lastUsage.getMemMaxBytes() ||
					currentUsage.getMemFreeBytes() != lastUsage.getMemFreeBytes();
			
		}
		
		/**
		 * Checks, if load balancing should be initiated.
		 * @param usage The current {@link IResourceUsage}.
		 * @return True, if there is more CPU in use as determined by {@link #mCPUThreshold} or if there is more memory in use as determined by {@link #mMemThreshold}.
		 * @throws InterruptedException if any error occurs.
		 */
		private boolean loadBalancingNeeded(IResourceUsage usage) throws InterruptedException {
		
			final double cpuMax = usage.getCpuMax();
			final double cpuFree = usage.getCpuFree();
			final long memMax = usage.getMemMaxBytes();
			final long memFree = usage.getMemFreeBytes();
			
			// Preconditions for upcoming fractions
			if(cpuMax == 0) {
				
				throw new InterruptedException("CPU max has the value 0");
				
			} else if(memMax == 0) {
				
				throw new InterruptedException("Mem max has the value 0");
				
			}
			
			final double cpuUsedPercentage = (cpuMax - cpuFree) / cpuMax;
			final double memUsedPercentage = (memMax - memFree) / memMax;
			return cpuUsedPercentage > this.mCPUThreshold || memUsedPercentage > this.mMemThreshold;
			
		}
		
		/**
		 * Gets the query to remove.
		 * @return A pair of {@link ILogicalQuery} and the corresponding query ID.
		 * @throws InterruptedException if any error occurs.
		 */
		private IPair<ILogicalQuery, Integer> getQueryToRemove() throws InterruptedException {
			
			final IExecutor executor = Activator.getExecutor().get();
			final ISession session = Activator.getSession();
			final ImmutableCollection<Integer> queryIDs = ImmutableList.copyOf(executor.getLogicalQueryIds(session));
			if(queryIDs.isEmpty()) {
				
				throw new InterruptedException("No query found to be handled over!");
				
			}
			
			final int queryID = queryIDs.iterator().next();
			return new Pair<ILogicalQuery, Integer>(executor.getLogicalQueryById(queryID, session), queryID);
			
		}
		
		/**
		 * Gets the ID of the peer to allocate.
		 * @param query The {@link ILogicalQuery} to be allocated.
		 * @return The ID of the peer, to which <code>query</code> has been allocated to.
		 * @throws InterruptedException if any error occurs.
		 */
		private PeerID getPeerIDToAllocate(ILogicalQuery query) throws InterruptedException {
			
			final IP2PDictionary peerDictionary = Activator.getPeerDictionary().get();
			final IP2PNetworkManager networkManager = Activator.getNetworkManager().get();
			final ImmutableCollection<ILogicalOperator> partOperators = ImmutableList.copyOf(LogicalQueryHelper.getAllOperators(query.getLogicalPlan()));
			final ILogicalQueryPart queryPart = new LogicalQueryPart(partOperators);
			final ImmutableCollection<ILogicalQueryPart> queryParts = ImmutableList.copyOf(Lists.newArrayList(queryPart));			
			final ImmutableCollection<PeerID> remotePeers = ImmutableList.copyOf(peerDictionary.getRemotePeerIDs());
			final PeerID localPeerID = networkManager.getLocalPeerID();
			ImmutableMap<ILogicalQueryPart, PeerID> allocationMap;
			
			try {
				
				allocationMap= ImmutableMap.copyOf(this.mAllocator.allocate(queryParts, query, remotePeers, localPeerID));
				
			} catch(QueryPartAllocationException e) {
				
				throw new InterruptedException("Error while allocation: " + e.getMessage());
				
			}
			
			if(allocationMap.isEmpty()) {
				
				throw new InterruptedException("No peers found for load balancing!");
				
			}
			
			return allocationMap.get(queryPart);
			
		}
		
		/**
		 * Updates the resource usage and checks, if the threshold has been reached. <br />
		 * Initializes load balancing, if that happens.
		 * @param usage The new {@link IResourceUsage}.
		 * @throws InterruptedException if any error occurs
		 */
		private void updateUsage(IResourceUsage usage) throws InterruptedException {
			
			// Preconditions
			if(this.mAllocator == null) {
				
				throw new InterruptedException("No allocator set");
				
			} else if(this.mCommunicator == null) {
				
				throw new InterruptedException("No communicator set");
				
			} else if(!Activator.getExecutor().isPresent()) {
				
				throw new InterruptedException("No executor bound");
				
			} else if(!Activator.getPeerDictionary().isPresent()) {
				
				throw new InterruptedException("No peer dictionary set");
				
			} else if(!Activator.getNetworkManager().isPresent()) {
				
				throw new InterruptedException("No network manager set");
				
			}
					
			this.mUsage = usage;
			
			if(this.loadBalancingNeeded(usage)) {
				
				// Get the query
				IPair<ILogicalQuery, Integer> queryAndID = this.getQueryToRemove();
				
				// Get the peer
				PeerID peerID = this.getPeerIDToAllocate(queryAndID.getE1());
				
				// Initialize the load balancing
				this.mCommunicator.initiateLoadBalancing(peerID, queryAndID.getE2());
				this.mCurrentlyBalancing = true;
				
			}
			
		}
		
		/**
		 * Creates a new {@link SimpleLBThread}.
		 */
		public SimpleLBThread() {
			
			super("Simple Load Balancing Thread");
			
		}
		
		@Override
		public void run() {
			
			Preconditions.checkArgument(Activator.getResourceManager().isPresent(), "No resource manager bound");
			
			final IPeerResourceUsageManager resourceManager = Activator.getResourceManager().get();
			
			while(this.isAlive() && !this.isInterrupted()) {
			
				if(!this.mCurrentlyBalancing) {
				
					try {
					
						final Optional<IResourceUsage> lastUsage = Optional.fromNullable(this.mUsage);
						final IResourceUsage usage = resourceManager.getLocalResourceUsage();
						
						if(!lastUsage.isPresent() || this.hasUsageChanged(usage, lastUsage.get())) {
							
							this.updateUsage(usage);
							
						}
						
						Thread.sleep(this.mResourceLookupTime);
						
					} catch(InterruptedException e) {
						
						LOG.error(e.getMessage());
						break;
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * The {@link SimpleLBThread} to look up the current resource usage. <br />
	 * It will call {@link ILoadBalancingCommunicator#initiateLoadBalancing(PeerID, int)} if load balancing has to be done.
	 */
	private SimpleLBThread mLookupThread = new SimpleLBThread();

	@Override
	public String getName() {
		
		return NAME;
		
	}

	@Override
	public void setAllocator(ILoadBalancingAllocator allocator) {
		
		Preconditions.checkNotNull(allocator, "The load balancing allocator to be used must be not null!");
		
		if(this.mLookupThread.mAllocator == null) {
			
			this.mLookupThread.mAllocator = allocator;
			
		} else {
		
			synchronized (this.mLookupThread.mAllocator) {
				
				this.mLookupThread.mAllocator = allocator;
				
			}
		
		}
		
		LOG.debug("Set {} as implementation of {}", allocator.getClass().getSimpleName(), ILoadBalancingAllocator.class.getSimpleName());
		
	}

	@Override
	public void setCommunicator(ILoadBalancingCommunicator communicator) {
		
		Preconditions.checkNotNull(communicator, "The load balancing communicator to be used must be not null!");
		
		if(this.mLookupThread.mCommunicator == null) {
			
			this.mLookupThread.mCommunicator = communicator;
			
		} else {
		
			synchronized (this.mLookupThread.mCommunicator) {
	
				this.mLookupThread.mCommunicator = communicator;
				
			}
		
		}
		
		LOG.debug("Set {} as implementation of {}", communicator.getClass().getSimpleName(), ILoadBalancingCommunicator.class.getSimpleName());
		
	}
	
	/**
	 * Gets the CPU threshold.
	 * @return The threshold for the CPU load as percentage.
	 */
	public double getCPUThreshold() {
		
		return this.mLookupThread.mCPUThreshold;
		
	}
	
	/**
	 * Sets the CPU threshold.
	 * @param threshold The threshold for the CPU load as percentage.
	 */
	public void setCPUThreshold(double threshold) {
		
		Preconditions.checkArgument(threshold >= 0 && threshold <= 100, threshold + " is not a valid percentage for the CPU threshold!");
		
		this.mLookupThread.mCPUThreshold = threshold;
		LOG.debug("Set the CPU threshold to {} percent", threshold);
		
	}
	
	/**
	 * Gets the memory threshold.
	 * @return The threshold for the memory load as percentage.
	 */
	public double getMemoryThreshold() {
		
		return this.mLookupThread.mMemThreshold;
		
	}
	
	/**
	 * Sets the memory threshold.
	 * @param threshold The threshold for the memory load as percentage.
	 */
	public void setMemoryThreshold(double threshold) {
		
		Preconditions.checkArgument(threshold >= 0 && threshold <= 100, threshold + " is not a valid percentage for the memory threshold!");
		
		this.mLookupThread.mMemThreshold = threshold;
		LOG.debug("Set the memory threshold to {} percent", threshold);
		
	}
	
	/**
	 * Gets the loop up time.
	 * @return The time between look ups of the local resources [ms].
	 */
	public long getLookupTime() {
		
		return this.mLookupThread.mResourceLookupTime;
		
	}
	
	/**
	 * Sets the loop up time.
	 * @param time The time between look ups of the local resources [ms].
	 */
	public void setLookupTime(long time) {
		
		Preconditions.checkArgument(time > 0, time + " is not a valid time [ms] for resource look ups!");
		
		this.mLookupThread.mResourceLookupTime = time;
		LOG.debug("Set the resource look up time to {} ms", time);
		
	}

	@Override
	public void startMonitoring() throws LoadBalancingException {
		
		this.mLookupThread.start();
		LOG.debug("Started monitoring local resources");
		
	}

	@Override
	public void stopMonitoring() {
		
		this.mLookupThread.interrupt();
		LOG.debug("Stopped monitoring local resources");
		
	}

	@Override
	public void notifyLoadBalancingFinished() {
		
		this.mLookupThread.mCurrentlyBalancing = false;
		LOG.debug("Load balancing process finished. Continue monitoring");
		
	}

}