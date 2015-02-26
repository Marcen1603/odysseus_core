package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.IPeerLockContainerListener;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.PeerLockContainer;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

/**
 * The {@link SimpleLoadBalancingStrategy} defines a threshold for the load (CPU and storage). 
 * If the current load is above this threshold, it takes the first query it gets from {@link IExecutor} and 
 * allocates it using the allocator set by {@link #setAllocator(ILoadBalancingAllocator)}.
 * @author Michael Brand
 */
public class SimpleLoadBalancingStrategy implements ILoadBalancingStrategy, ILoadBalancingListener {
	
	
	public static final boolean DO_NOT_MOVE_SINKS = true;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SimpleLoadBalancingStrategy.class);
	
	/**
	 * The name of this strategy.
	 */
	public static final String NAME = "simple";
	
	/**
	 * The default threshold for the CPU load as percentage.
	 */
	public static final double DEFAULT_CPU_THRESHOLD = 0.01;
	
	/**
	 * Used to count successes.
	 */
	private volatile int successes=0;
	
	/**
	 * Used to count failues
	 */
	private volatile int failures=0;
	
	
	/**
	 * The default threshold for the memory load as percentage.
	 */
	public static final double DEFAULT_MEM_THRESHOLD = 0.1;
	
	/**
	 * The default time between look ups of the local resources [ms].
	 */
	public static final long DEFAULT_RESOURCE_LOOKUP_TIME = 60000;

	private static final long NANOSECONDS_TO_MILLISECONDS = 1000000;
	

	/***
	 * Start time for timer.
	 */
	private volatile long startTime;
	
	/**
	 * The {@link Thread} to look up the current resource usage. <br />
	 * It will call {@link ILoadBalancingCommunicator#initiateLoadBalancing(PeerID, int)} if load balancing has to be done.
	 */
	private class SimpleLBThread extends Thread implements IPeerLockContainerListener {
		
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
		protected volatile boolean mRunning = false;
		
		/**
		 * True, if {@link ILoadBalancingCommunicator#initiateLoadBalancing(PeerID, int)} has been called.
		 */
		protected volatile boolean mCurrentlyBalancing = false;
		
		protected volatile PeerLockContainer lockContainer = null;
		
		protected volatile PeerID volunteer=null;
		
		protected volatile int queryID;
		
		
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
			UnmodifiableIterator<Integer> iterator = queryIDs.iterator();
			int queryID = iterator.next();
			while(hasRealSinks(queryID) && DO_NOT_MOVE_SINKS) {
				LOG.debug("Query {} has at least one real sink and DO_NOT_MOVE_SINKS is set. Trying to take next query.",queryID);
				if(iterator.hasNext()) {
					queryID = iterator.next();
				}
				else {
					throw new InterruptedException("No query found to be handled over!");
				}
			}
			return new Pair<ILogicalQuery, Integer>(executor.getLogicalQueryById(queryID, session), queryID);
			
		}
		
		/**
		 * Gets the ID of the peer to allocate.
		 * @param query The {@link ILogicalQuery} to be allocated.
		 * @return The ID of the peer, to which <code>query</code> has been allocated to.
		 * @throws InterruptedException if any error occurs.
		 */
		private PeerID getPeerIDToAllocate(ILogicalQuery query) throws InterruptedException {
			
			final IPeerDictionary peerDictionary = Activator.getPeerDictionary().get();
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
			if(!Activator.getExecutor().isPresent()) {
				
				throw new InterruptedException("No executor bound");
				
			} else if(!Activator.getPeerDictionary().isPresent()) {
				
				throw new InterruptedException("No peer dictionary set");
				
			} else if(!Activator.getNetworkManager().isPresent()) {
				
				throw new InterruptedException("No network manager set");
			} 
			else if(!Activator.getPeerCommunicator().isPresent()) {
				throw new InterruptedException("No Peer Communicator set");
			} 
			else if (!Activator.getLoadBalancingLock().isPresent()) {
				throw new InterruptedException("No LoadBalancing Lock set");
			}
			
			ILoadBalancingLock lock = Activator.getLoadBalancingLock().get();
			IPeerCommunicator peerCommunicator = Activator.getPeerCommunicator().get();
					
			this.mUsage = usage;
			
			if(this.loadBalancingNeeded(usage)) {
				
				LOG.debug("LoadBalancing is needed. Trying to acquire Local Lock.");
				startTime = System.nanoTime();
				//Try to lock peer first.
				if(lock.requestLocalLock()) {
					try {
						LOG.debug("Lock acquired.");
						//Additional boolean to prevent multiple parallel LoadBalancing Processes.
						this.mCurrentlyBalancing = true;
						
						// Get the query
						IPair<ILogicalQuery, Integer> queryAndID = this.getQueryToRemove();
						
						// Get the peer and the involved peers.
						PeerID peerID = this.getPeerIDToAllocate(queryAndID.getE1());
						List<PeerID> otherPeers = this.mCommunicator.getInvolvedPeers(queryAndID.getE2());
						
						
						if(!otherPeers.contains(peerID))
							otherPeers.add(peerID);
						
						this.volunteer = peerID;
						this.queryID = queryAndID.getE2();
						
						//Acquire Locks for other peers
						PeerLockContainer peerLocks = new PeerLockContainer(peerCommunicator,otherPeers,this);
						peerLocks.requestLocks();
						this.lockContainer = peerLocks;
					}
					catch(Exception e) {
						LOG.error("An exception occured in LoadBalancing Strategy: {}",e.getMessage());
						mCurrentlyBalancing = false;
						lock.releaseLocalLock();
						
						
					}
					
					
				}
				
				
			}
			
		}
		
		/**
		 * Creates a new {@link SimpleLBThread}.
		 * @param allocator An implementation of {@link ILoadBalancingAllocator}.
		 * @param communicator An implementation of {@link ILoadBalancingCommunicator}.
		 */
		public SimpleLBThread(ILoadBalancingAllocator allocator, ILoadBalancingCommunicator communicator) {
			
			super("Simple Load Balancing Thread");
			
			Preconditions.checkNotNull(allocator, "The load balancing allocator to be used must be not null!");
			Preconditions.checkNotNull(communicator, "The load balancing communicator to be used must be not null!");
			
			this.mAllocator = allocator;
			this.mCommunicator = communicator;
			
		}
		
		@Override
		public void run() {
			
			Preconditions.checkArgument(Activator.getResourceManager().isPresent(), "No resource manager bound");
			
			final IPeerResourceUsageManager resourceManager = Activator.getResourceManager().get();
			LOG.info("Started monitoring local resources");
			
			while(this.isAlive() && !this.isInterrupted() && this.mRunning) {
				
				try {
			
					if(!this.mCurrentlyBalancing) {
					
						final Optional<IResourceUsage> lastUsage = Optional.fromNullable(this.mUsage);
						final IResourceUsage usage = resourceManager.getLocalResourceUsage();
						
						if(!lastUsage.isPresent() || this.hasUsageChanged(usage, lastUsage.get())) {
							
							this.updateUsage(usage);
							
						}
						
					}
				
					Thread.sleep(this.mResourceLookupTime);
				
				} catch(InterruptedException e) {
					
					LOG.error(e.getMessage());
					break;
					
				}
				
			}
			
			LOG.info("Stopped monitoring local resources");
			
		}

		@Override
		public void notifyLockingFailed() {
			LOG.debug("Locking other Peers failed.");
			lockContainer = null;
			volunteer = null;
			Activator.getLoadBalancingLock().get().releaseLocalLock();
			this.mCurrentlyBalancing = false;
			
		}

		@Override
		public void notifyLockingSuccessfull() {
			LOG.debug("Locking of other peers successfull.");
			if(this.mRunning) {
				mCommunicator.initiateLoadBalancing(volunteer, queryID);
			}
			else {
				//Release if Thread is stopped.
				lockContainer.releaseLocks();
			}
		}

		@Override
		public void notifyReleasingFinished() {
			LOG.debug("Releasing locks on other Peers finished.");
			//Dispose Lock Container
			lockContainer = null;
			volunteer = null;
			Activator.getLoadBalancingLock().get().releaseLocalLock();
			this.mCurrentlyBalancing = false;
		}
		
	}
	
	/**
	 * The {@link SimpleLBThread} to look up the current resource usage. <br />
	 * It will call {@link ILoadBalancingCommunicator#initiateLoadBalancing(PeerID, int)} if load balancing has to be done.
	 */
	private SimpleLBThread mLookupThread;
	
	/**
	 * The allocator to use.
	 */
	private ILoadBalancingAllocator mAllocator;
	
	/**
	 * The communicator to use.
	 */
	private ILoadBalancingCommunicator mCommunicator;

	@Override
	public String getName() {
		
		return NAME;
		
	}

	@Override
	public void setAllocator(ILoadBalancingAllocator allocator) {
		
		Preconditions.checkNotNull(allocator, "The load balancing allocator to be used must be not null!");
		
		this.mAllocator = allocator;		
		LOG.debug("Set {} as implementation of {}", allocator.getClass().getSimpleName(), ILoadBalancingAllocator.class.getSimpleName());
		
	}

	@Override
	public void setCommunicator(ILoadBalancingCommunicator communicator) {
		
		if(mCommunicator!=null) {
			mCommunicator.removeLoadBalancingListener(this);
		}
		
		Preconditions.checkNotNull(communicator, "The load balancing communicator to be used must be not null!");
		
		this.mCommunicator = communicator;
		mCommunicator.registerLoadBalancingListener(this);
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
		
		if(this.mLookupThread != null && this.mLookupThread.isAlive()) {
			
			LOG.error("Load balancing strategy is already running!");
			return;
			
		}
		resetSuccessCounter();
		this.mLookupThread = new SimpleLBThread(mAllocator, mCommunicator);
		this.mLookupThread.mRunning = true;
		this.mLookupThread.start();
		
	}

	@Override
	public void stopMonitoring() {
		
		this.mLookupThread.mRunning = false;
		
	}

	@Override
	public void notifyLoadBalancingFinished(boolean successful) {

		//Release Locks.
		if(this.mLookupThread.lockContainer!=null) {
			mLookupThread.lockContainer.releaseLocks();
		}
		else {
			//Something went wrong... Make sure we are not locked any more.

			LOG.error("Lock Container is null. Remote Peers could stay locked.");
			if(Activator.getLoadBalancingLock().isPresent()) {
				Activator.getLoadBalancingLock().get().releaseLocalLock();
			}
		}
		
		
		
		if(successful) {
			successes +=1;
			LOG.info("Load balancing process successfully finished. Continue monitoring");
			if(startTime>0) {
				long duration = System.nanoTime()-startTime;
				duration = duration/NANOSECONDS_TO_MILLISECONDS;
				LOG.info("LoadBalancing took {} ms", duration);
				startTime = 0;
			}
			
			
		}
		else {
			failures +=1;
			LOG.info("Load balancing process failed. Continue monitoring");
			if(startTime>0) {
				long duration = System.nanoTime()-startTime;
				duration = duration/NANOSECONDS_TO_MILLISECONDS;
				LOG.info("LoadBalancing took {} ms", duration);
				startTime = 0;
			}
		}
		LOG.info("Successful LoadBalancing Processes: {}", successes);
		LOG.info("Failues: {}",failures);
		
	}
	
	private static boolean hasRealSinks(int queryID) {
		
		ILogicalQueryPart queryPart = getInstalledQueryPart(queryID);
		ILogicalQueryPart copy = getCopyOfQueryPart(queryPart);
		removeTopAOs(copy);
		
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper
				.getRelativeSinksOfLogicalQueryPart(copy);
		
		for(ILogicalOperator sink : relativeSinks) {
			
			//Operator is Sink AND no jxtaSender.
			if (!(sink instanceof JxtaSenderAO)) {
				LOG.debug("Found real sink " + sink.toString());
				return true;
			};
			LOG.debug("Rel. Sink: {}",sink.toString());
		}
		return false;
		
	}
	
	
	

	/**
	 * Removes all TopAOs from a queryPart.
	 * 
	 * @param part
	 *            Part where TopAOs should be removed.
	 */
	private static void removeTopAOs(ILogicalQueryPart part) {
		ArrayList<ILogicalOperator> toRemove = new ArrayList<>();
		for (ILogicalOperator operator : part.getOperators()) {
			if (operator instanceof TopAO) {
				toRemove.add(operator);
			}
		}

		for (ILogicalOperator topAO : toRemove) {
			topAO.unsubscribeFromAllSinks();
			topAO.unsubscribeFromAllSources();
			part.removeOperator(topAO);
		}
	}
	
	
	/**
	 * Get a particular Query as query part.
	 * 
	 * @param executor
	 * @param session
	 * @param queryId
	 * @return
	 */
	private static ILogicalQueryPart getInstalledQueryPart(int queryId) {

		IExecutor executor = Activator.getExecutor().get();
		ISession session = Activator.getSession();

		ILogicalQuery query = executor.getLogicalQueryById(queryId, session);
		ArrayList<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		RestructHelper.collectOperators(query.getLogicalPlan(), operators);
		return new LogicalQueryPart(operators);
	}
	
	/**
	 * Gets a (logical) Copy of a single Query Part.
	 * 
	 * @param part
	 *            Part to copy.
	 * @return Copy of part.
	 */
	private static ILogicalQueryPart getCopyOfQueryPart(ILogicalQueryPart part) {
		ILogicalQueryPart result = null;
		ArrayList<ILogicalQueryPart> partsList = new ArrayList<ILogicalQueryPart>();

		partsList.add(part);

		Map<ILogicalQueryPart, ILogicalQueryPart> copies = LogicalQueryHelper
				.copyQueryPartsDeep(partsList);

		for (Map.Entry<ILogicalQueryPart, ILogicalQueryPart> entry : copies.entrySet()) {
			result = entry.getKey();
		}

		return result;
	}
	
	public void resetSuccessCounter() {
		LOG.info("Resetting success/failure counter");
		successes = 0;
		failures = 0;
	}

}