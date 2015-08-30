package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ICommunicatorChooserRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingQuerySelectorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingStrategyRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingTriggerRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IQueryTransmissionHandlerRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class OsgiServiceProvider {
	
	//Registries
	private static ILoadBalancingTriggerRegistry triggerRegistry;
	private static ILoadBalancingQuerySelectorRegistry selectorRegistry;
	private static ILoadBalancingAllocatorRegistry allocatorRegistry;
	private static ICommunicatorChooserRegistry communicatorChooserRegistry;
	private static IQueryTransmissionHandlerRegistry transmissionHandlerRegistry;
	private static ILoadBalancingStrategyRegistry strategyRegistry;
	private static IExcludedQueriesRegistry excludedQueriesRegistry;
	private static IPeerDictionary peerDictionary;
	
	private static IServerExecutor executor;
	private static IP2PNetworkManager networkManager;
	private static IQueryPartController queryPartController;
	
	
	public static ILoadBalancingStrategyRegistry getStrategyRegistry() {
		return strategyRegistry;
	}

	public static void bindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		strategyRegistry = serv;
	}
	
	public static void unbindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		if(strategyRegistry==serv) {
			strategyRegistry = null;
		}
	}
	
	public static void bindTriggerRegistry(ILoadBalancingTriggerRegistry serv) {
		triggerRegistry = serv;
	}
	
	public static void unbindTriggerRegistry(ILoadBalancingTriggerRegistry serv) {
		if(triggerRegistry==serv) {
			triggerRegistry = null;
		}
	}
	
	public static void bindSelectorRegistry(ILoadBalancingQuerySelectorRegistry serv) {
		selectorRegistry = serv;
	}
	
	public static void unbindSelectorRegistry(ILoadBalancingQuerySelectorRegistry serv) {
		if(selectorRegistry==serv) {
			selectorRegistry = null;
		}
	}
	
	public static void bindCommunicatorChooserRegistry(ICommunicatorChooserRegistry serv) {
		communicatorChooserRegistry = serv;
	}
	
	public static void unbindCommunicatorChooserRegistry(ICommunicatorChooserRegistry serv) {
		if(communicatorChooserRegistry==serv) {
			communicatorChooserRegistry = null;
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
	

	public static void bindTransmissionHandlerRegistry(IQueryTransmissionHandlerRegistry serv) {
		transmissionHandlerRegistry = serv;
	}
	
	public static void unbindTransmissionHandlerRegistry(IQueryTransmissionHandlerRegistry serv) {
		if(transmissionHandlerRegistry==serv) {
			transmissionHandlerRegistry = null;
		}
	}
	
	
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}
	

	public static void unbindExecutor(IExecutor serv) {
		if(executor==serv) {
			executor = null;
		}
	}
	
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}
	
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if(networkManager==serv) {
			networkManager = null;
		}
	}
	
	public static void bindQueryPartController(IQueryPartController serv) {
		queryPartController = serv;
	}
	
	public void unbindQueryPartController(IQueryPartController serv) {
		if(queryPartController==serv) {
			queryPartController=null;
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary=null;
		}
	}
	
	public void bindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		excludedQueriesRegistry = serv;
	}
	
	public void unbindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		if(excludedQueriesRegistry==serv) {
			excludedQueriesRegistry=null;
		}
	}
	

	public static IExcludedQueriesRegistry getExcludedQueriesRegistry() {
		return excludedQueriesRegistry;
	}

	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}

	public static IServerExecutor getExecutor() {
		return executor;
	}

	public static IP2PNetworkManager getNetworkManager() {
		return networkManager;
	}

	public static IQueryPartController getQueryPartController() {
		return queryPartController;
	}

	public static ILoadBalancingTriggerRegistry getTriggerRegistry() {
		return triggerRegistry;
	}

	public static ILoadBalancingQuerySelectorRegistry getSelectorRegistry() {
		return selectorRegistry;
	}

	public static ILoadBalancingAllocatorRegistry getAllocatorRegistry() {
		return allocatorRegistry;
	}

	public static ICommunicatorChooserRegistry getCommunicatorChooserRegistry() {
		return communicatorChooserRegistry;
	}

	public static IQueryTransmissionHandlerRegistry getTransmissionHandlerRegistry() {
		return transmissionHandlerRegistry;
	}
	
	
}
