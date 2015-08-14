package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.DynamicStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.GreedyQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.SimulatedAnnealingQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SharedQueryIDModifier;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SinkTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SourceTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingStrategyRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class OdyLoadConsole implements CommandProvider {
	
	
	private static IP2PNetworkManager networkManager;
	private static IServerExecutor executor;
	private static IQueryPartController queryPartController;
	private static IExcludedQueriesRegistry excludedQueriesRegistry;
	private static ILoadBalancingStrategyRegistry strategyRegistry;
	private static ILoadBalancingAllocatorRegistry allocatorRegistry;
	private static IPeerDictionary peerDictionary;
	
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
	}
	
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if(networkManager==serv) {
			networkManager=null;
		}
	}
	
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor)serv;
	}
	
	public static void unbindExecutor(IExecutor serv) {
		if(executor==serv) {
			executor=null;
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
	
	public static void bindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		excludedQueriesRegistry = serv;
	}
	
	public static void unbindExcludedQueriesRegistry(IExcludedQueriesRegistry serv) {
		if(excludedQueriesRegistry==serv) {
			excludedQueriesRegistry=null;
		}
	}
	
	
	public static void bindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		strategyRegistry = serv;
	}
	
	public void unbindStrategyRegistry(ILoadBalancingStrategyRegistry serv) {
		if(strategyRegistry == serv) {
			strategyRegistry = null;
		}
	}
	
	public void bindAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		allocatorRegistry = serv;
	}
	
	public void unbindAllocatorRegistry(ILoadBalancingAllocatorRegistry serv) {
		if(allocatorRegistry==serv) {
			allocatorRegistry = null;
		}
	}
	
	public void bindPeerDictionary(IPeerDictionary serv)  {
		peerDictionary = serv;
	}
	
	public void unbindPeerDictionary(IPeerDictionary serv) {
		if(peerDictionary==serv) {
			peerDictionary = null;
		}
	}
	
	
	
	
	
	private static ISession activeSession;

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---OdyLoad-specific Loadbalancing commands---\n");
		sb.append("    transformSources <queryID>		   					  	    - Transformes all Sources in Query to JxtaSender-Receiver Pairs\n");
		sb.append("    transformSinks <queryID>								  		- Transformes all Sinks in Query to JxtaSender-Receiver Pairs\n");
		sb.append("    addSharedQueryID <queryID>							  	 	- Adds a shared QueryID to local Query\n");
		sb.append("    selectGreedy <cpuToRemove> <memToRemove> <netToRemove>  	 	- Tries greedy Selector to find combination that removes load\n");
		sb.append("    selectAnnealing <cpuToRemove> <memToRemove> <netToRemove> 	- Tries SA Selector to find combination that removes load\n");
		sb.append("    selectCombination <cpuToRemove> <memToRemove> <netToRemove>  - Tries combined Selectors to find combination that removes load\n");
		sb.append("    allocate <queryID>                                           - Allocates a queryID with OdyLoad allocator\n");
		sb.append("    determineCommunicator <queryID>                              - Determines which communicator to use for queryID\n");
		sb.append("    allocateAndTransfer <queryID>								- Allocates and transfers given QueryID\n");
		return sb.toString();
	}
	
	
	public void _transformSources(CommandInterpreter ci) {
		

		Preconditions.checkNotNull(networkManager,"Network Manager not bound.");
		Preconditions.checkNotNull(executor,"Executor not bound.");
		Preconditions.checkNotNull(queryPartController,"QueryPartController not bound.");
		Preconditions.checkNotNull(excludedQueriesRegistry,"ExcludedQueriesRegistry not bound.");
		
		final String ERROR_USAGE  = "Usage: transformSources <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SourceTransformer.replaceSources(localQueryID, getLocalPeerID(), getActiveSession(), networkManager, executor, queryPartController, excludedQueriesRegistry);
		ci.println("Done.");
	}
	
	

	public void _transformSinks(CommandInterpreter ci) {
		

		Preconditions.checkNotNull(networkManager,"Network Manager not bound.");
		Preconditions.checkNotNull(executor,"Executor not bound.");
		Preconditions.checkNotNull(queryPartController,"QueryPartController not bound.");
		Preconditions.checkNotNull(excludedQueriesRegistry,"ExcludedQueriesRegistry not bound.");
		
		final String ERROR_USAGE  = "Usage: transformSinks <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SinkTransformer.replaceSinks(localQueryID, getLocalPeerID(), getActiveSession(), networkManager, executor, queryPartController, excludedQueriesRegistry);
		ci.println("Done.");
	}
	


	public void _addSharedQueryID(CommandInterpreter ci) {
		

		Preconditions.checkNotNull(networkManager,"Network Manager not bound.");
		Preconditions.checkNotNull(executor,"Executor not bound.");
		Preconditions.checkNotNull(queryPartController,"QueryPartController not bound.");
		
		final String ERROR_USAGE  = "Usage: transformSinks <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SharedQueryIDModifier.addSharedQueryIDIfNeccessary(localQueryID, executor, queryPartController, networkManager, getActiveSession());
		ci.println("Done.");
	}
	
	public void _selectGreedy(CommandInterpreter ci) {

		Preconditions.checkNotNull(strategyRegistry,"Strategy Registry not bound.");

		final String ERROR_USAGE = "Usage: selectGreedy <cpuToRemove> <memToRemove> <netToRemove>";
		
		Double cpuToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double memToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double netToRemove = getDoubleParameter(ci, ERROR_USAGE);
		
		if(cpuToRemove==null||memToRemove==null||netToRemove==null) {
			return;
		}
		DynamicStrategy strategy = (DynamicStrategy) strategyRegistry.getStrategy(OdyLoadConstants.STRATEGY_NAME);
		QueryCostMap allQueries = strategy.generateCostMapForAllQueries();
		IQuerySelectionStrategy selector = new GreedyQuerySelector();
		QueryCostMap result = selector.selectQueries(allQueries, cpuToRemove, memToRemove, netToRemove);
		ci.println("=== RESUlT: ====");
		ci.println(result.toString());
	}
	
	public void _selectAnnealing(CommandInterpreter ci) {
		
		Preconditions.checkNotNull(strategyRegistry,"Strategy Registry not bound.");
		
		final String ERROR_USAGE = "Usage: selectAnnealing <cpuToRemove> <memToRemove> <netToRemove>";
		
		Double cpuToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double memToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double netToRemove = getDoubleParameter(ci, ERROR_USAGE);
		
		if(cpuToRemove==null||memToRemove==null||netToRemove==null) {
			return;
		}
		DynamicStrategy strategy = (DynamicStrategy) strategyRegistry.getStrategy(OdyLoadConstants.STRATEGY_NAME);
		QueryCostMap allQueries = strategy.generateCostMapForAllQueries();
		IQuerySelectionStrategy selector = new SimulatedAnnealingQuerySelector();
		QueryCostMap result = selector.selectQueries(allQueries, cpuToRemove, memToRemove, netToRemove);
		ci.println("=== RESUlT: ====");
		ci.println(result.toString());
	}
	
	public void _selectCombination(CommandInterpreter ci) {

		Preconditions.checkNotNull(strategyRegistry,"Strategy Registry not bound.");
		
		final String ERROR_USAGE = "Usage: selectCombination <cpuToRemove> <memToRemove> <netToRemove>";
		
		Double cpuToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double memToRemove = getDoubleParameter(ci, ERROR_USAGE);
		Double netToRemove = getDoubleParameter(ci, ERROR_USAGE);
		
		if(cpuToRemove==null||memToRemove==null||netToRemove==null) {
			return;
		}
		DynamicStrategy strategy = (DynamicStrategy) strategyRegistry.getStrategy(OdyLoadConstants.STRATEGY_NAME);
		QueryCostMap allQueries = strategy.generateCostMapForAllQueries();
		IQuerySelectionStrategy selector = new GreedyQuerySelector();
		QueryCostMap result = selector.selectQueries(allQueries, cpuToRemove, memToRemove, netToRemove);
		ci.println("=== Greedy Result: ====");
		ci.println(result.toString());
		selector = new SimulatedAnnealingQuerySelector(result);
		QueryCostMap simulatedAnnealingResult = selector.selectQueries(allQueries, cpuToRemove, memToRemove, netToRemove);
		ci.println("=== Simulated Annealing Result: ====");
		ci.println(simulatedAnnealingResult.toString());
	}
	
	
	public void _allocate(CommandInterpreter ci) {

		Preconditions.checkNotNull(allocatorRegistry,"Allocator Registry not bound.");
		Preconditions.checkNotNull(peerDictionary,"Peer Dictionary not bound.");

		
		final String ERROR_USAGE = "Usage: allocate <queryID>";
		final String ERROR_QUERY = "Query not found.";
		
		Integer queryID = getIntegerParameter(ci, ERROR_USAGE);
		if(queryID==null) {
			return;
		}
		
		ILoadBalancingAllocator allocator = allocatorRegistry.getAllocator(OdyLoadConstants.ALLOCATOR_NAME);
		
		ILogicalQuery query = executor.getLogicalQueryById(queryID, getActiveSession());
		if(query ==null) {
			ci.println(ERROR_QUERY);
			return;
		}
		
		List<ILogicalQueryPart> queryPartList = Lists.newArrayList();
		queryPartList.add(getLogicalQueryPart(queryID));
		Collection<PeerID> knownRemotePeers = peerDictionary.getRemotePeerIDs();
		
		
		try {
			Map<ILogicalQueryPart,PeerID> result = allocator.allocate(queryPartList, query, knownRemotePeers, getLocalPeerID());
			for(ILogicalQueryPart key : result.keySet()) {
				ci.println("Allocated to " + peerDictionary.getRemotePeerName(result.get(key)));
			}
			
			
		} catch (QueryPartAllocationException e) {
			ci.println("Could not allocate Query.");
		}
		
		ci.println("Done.");
		
	}
	
	public void _determineCommunicator(CommandInterpreter ci) {
		
		Preconditions.checkNotNull(strategyRegistry,"Strategy Registry not bound.");
		
		final String ERROR_USAGE = "Usage: determineCommunicator <queryID>";
		
		Integer queryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(queryID==null) {
			return;
		}
		
		List<Integer> queryIDAsList = Lists.newArrayList();
		queryIDAsList.add(queryID);
		
		DynamicStrategy strategy = (DynamicStrategy) strategyRegistry.getStrategy(OdyLoadConstants.STRATEGY_NAME);
		HashMap<Integer,ILoadBalancingCommunicator> result = strategy.chooseCommunicators(queryIDAsList);
		ILoadBalancingCommunicator comm = result.get(queryID);
		ci.println("Chosen Communicator: " + comm.getName());
	}
	
	
	public void _allocateAndTransfer(CommandInterpreter ci) {
		Preconditions.checkNotNull(executor,"Executor not bound.");
		Preconditions.checkNotNull(strategyRegistry,"Strategy Registry not bound.");
		Preconditions.checkNotNull(allocatorRegistry,"Allocator Registry not bound.");
		
		final String ERROR_USAGE = "Usage: allocateAndTransfer <queryID>";
		final String ERROR_QUERY = "Query not found.";
		Integer queryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(queryID==null) {
			return;
		}
		
		

		IPhysicalQuery query = executor.getExecutionPlan().getQueryById(queryID);
		if(query ==null) {
			ci.println(ERROR_QUERY);
			return;
		}
		
		Set<IPhysicalOperator> operatorList = query.getAllOperators();
		
		DynamicStrategy strategy = (DynamicStrategy)strategyRegistry.getStrategy(OdyLoadConstants.STRATEGY_NAME);
		
		QueryCostMap costMap = new QueryCostMap();
		
		strategy.addQueryToCostMap(costMap, queryID, operatorList);
		strategy.setAllocator(allocatorRegistry.getAllocator(OdyLoadConstants.ALLOCATOR_NAME));

		
		strategy.allocateAndTransferQueries(getLocalPeerID(), costMap);
		
		ci.println("Done.");
	}

	
	
	private Integer getIntegerParameter(CommandInterpreter ci,String errorMessage) {
		String parameterAsString = ci.nextArgument();
		if (Strings.isNullOrEmpty(parameterAsString)) {
			ci.println(errorMessage);
			return null;
		}

		Integer paramAsInt;

		try {
			paramAsInt = Integer.parseInt(parameterAsString);
		} catch (NumberFormatException e) {
			ci.println(errorMessage);
			return null;
		}
		
		return paramAsInt;
	}
	
	private Double getDoubleParameter(CommandInterpreter ci,String errorMessage) {
		String parameterAsString = ci.nextArgument();
		if (Strings.isNullOrEmpty(parameterAsString)) {
			ci.println(errorMessage);
			return null;
		}

		Double paramAsDouble;

		try {
			paramAsDouble = Double.parseDouble(parameterAsString);
		} catch (NumberFormatException e) {
			ci.println(errorMessage);
			return null;
		}
		
		return paramAsDouble;
	}
	
	/**
	 * Gets currently active Session.
	 * 
	 * @return active Session
	 */
	public static ISession getActiveSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());
		}
		return activeSession;
	}
	
	private PeerID getLocalPeerID() {
		Preconditions.checkNotNull(networkManager,"Network Manager not bound.");
		return networkManager.getLocalPeerID();
	}
	
	private ILogicalQueryPart getLogicalQueryPart(int queryID) {

		Preconditions.checkNotNull(executor,"Executor not bound.");
		ILogicalQuery query = executor.getLogicalQueryById(queryID, getActiveSession());
		List<ILogicalOperator> opsInQuery = Lists.newArrayList();
		RestructHelper.collectOperators(query.getLogicalPlan(),opsInQuery);
		return new LogicalQueryPart(opsInQuery);
		
	}
	
}
