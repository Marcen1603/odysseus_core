package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCostModel;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock.ILoadBalancingLock;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingCommunicatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingStrategyRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class OsgiServiceProvider {
	
	
	private static IPhysicalCostModel physicalCostModel;
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager networkManager;
	private static ILoadBalancingLock lock;
	private static ILoadBalancingCommunicatorRegistry communicatorRegistry;
	private static ILoadBalancingAllocatorRegistry allocatorRegistry;
	private static ILoadBalancingStrategyRegistry strategyRegistry;
	
	private static IPeerCommunicator peerCommunicator;
	private static IQueryPartController queryPartController;
	private static IExcludedQueriesRegistry excludedQueryRegistry;
	private static IPeerResourceUsageManager usageManager;
	private static IServerExecutor executor;

	
	public static void bindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		strategyRegistry = serv;
	}
	
	public static void unbindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		if(strategyRegistry==serv) {
			strategyRegistry=null;
		}
	}
	
	public static void bindAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		allocatorRegistry = serv;
	}
	
	public static void unbindAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		if(allocatorRegistry==serv) {
			allocatorRegistry = null;
		}
	}
	

	public static void bindExcludedQueryRegistry(IExcludedQueriesRegistry serv) {
		excludedQueryRegistry = serv;
	}
	
	public static void unbindExcludedQueryRegistry(IExcludedQueriesRegistry serv) {
		if(excludedQueryRegistry==serv) {
			excludedQueryRegistry = null;
		}
	}
	
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}
	
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if(peerCommunicator==serv) {
			peerCommunicator = null;
		}
	}
	
	public static void bindQueryPartController(IQueryPartController serv) {
			queryPartController = serv;
	}
	
	public static void unbindQueryPartController(IQueryPartController serv) {
		if(queryPartController==serv) {
			queryPartController=null;
		}
	}
	
	public static void bindLoadBalancingCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		communicatorRegistry = serv;
	}

	public static void unbindLoadBalancingCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		if(communicatorRegistry==serv) {
			communicatorRegistry = null;
		}
	}
	
	public static void bindPhysicalCostModel(IPhysicalCostModel serv) {
		physicalCostModel = serv;
		
	}
	
	public static void unbindPhysicalCostModel(IPhysicalCostModel serv) {
		if(physicalCostModel==serv) {
			physicalCostModel = null;
		}
	}
	
	public static void bindLoadBalancingLock(ILoadBalancingLock serv) {
		lock = serv;
	}


	public static void unbindLoadBalancingLock(ILoadBalancingLock serv) {
		if(lock == serv) {
			lock = null;
		}
	}

	
	public static void bindResourceUsageManager(IPeerResourceUsageManager serv) {
		usageManager = serv;
	}

	public static void unbindResourceUsageManager(IPeerResourceUsageManager serv) {
		if (usageManager == serv) {
			usageManager = null;
		}
	}
	
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}
	
	public static void unbindExecutor(IExecutor serv) {
		if(executor==serv){
			executor=null;
		}
	}
	
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if(serv==peerDictionary) {
			peerDictionary = null;
		}
	}
	
	public static void bindNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}
	
	public static void unbindNetworkManager(IP2PNetworkManager serv) {
		if(networkManager==serv) {
			networkManager = null;
		}
	}
	
	//Getters.

	public static IPhysicalCostModel getPhysicalCostModel() {
		Preconditions.checkNotNull(physicalCostModel,"Pysical Cost Model not bound.");
		return physicalCostModel;
	}

	public static IPeerDictionary getPeerDictionary() {
		Preconditions.checkNotNull(peerDictionary,"Peer Dictionary not bound.");
		return peerDictionary;
	}

	public static IP2PNetworkManager getNetworkManager() {
		Preconditions.checkNotNull(networkManager,"Network Manager not bound.");
		return networkManager;
	}

	public static ILoadBalancingLock getLock() {
		Preconditions.checkNotNull(lock,"Load Balancing Lock not bound.");
		return lock;
	}

	public static ILoadBalancingCommunicatorRegistry getCommunicatorRegistry() {
		Preconditions.checkNotNull(communicatorRegistry,"Communicator Registry not bound.");
		return communicatorRegistry;
	}

	public static IPeerCommunicator getPeerCommunicator() {
		Preconditions.checkNotNull(peerCommunicator,"Peer Communicator not bound.");
		return peerCommunicator;
	}

	public static IQueryPartController getQueryPartController() {
		Preconditions.checkNotNull(queryPartController,"Query Part Controller Lock not bound.");
		return queryPartController;
	}

	public static IExcludedQueriesRegistry getExcludedQueryRegistry() {
		Preconditions.checkNotNull(excludedQueryRegistry,"Excluded Query Registry not bound.");
		return excludedQueryRegistry;
	}

	public static IPeerResourceUsageManager getUsageManager() {
		Preconditions.checkNotNull(usageManager,"Resource Usage Manager not bound.");
		return usageManager;
	}

	public static IServerExecutor getExecutor() {
		Preconditions.checkNotNull(executor,"Executor not bound.");
		return executor;
	}


	public static ILoadBalancingAllocatorRegistry getAllocatorRegistry() {
		Preconditions.checkNotNull(executor,"Allocator Registry not bound.");
		return allocatorRegistry;
	}

	public static ILoadBalancingStrategyRegistry getStrategyRegistry() {
		Preconditions.checkNotNull(executor,"Strategy Registry not bound.");
		return strategyRegistry;
	}

}
