package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OsgiServiceProvider;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.DynamicStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.CommunicatorChooser.OdyLoadCommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.CostEstimationHelper;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.GreedyQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.QueryCostMap;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.heuristic.SimulatedAnnealingQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SharedQueryIDModifier;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SinkTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.preprocessing.SourceTransformer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingStrategyRegistry;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class OdyLoadConsole implements CommandProvider {
	
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
		
		final String ERROR_USAGE  = "Usage: transformSources <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SourceTransformer.replaceSources(localQueryID, getLocalPeerID(), getActiveSession());
		ci.println("Done.");
	}
	
	

	public void _transformSinks(CommandInterpreter ci) {

		
		final String ERROR_USAGE  = "Usage: transformSinks <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SinkTransformer.replaceSinks(localQueryID, getLocalPeerID(), getActiveSession());
		ci.println("Done.");
	}
	


	public void _addSharedQueryID(CommandInterpreter ci) {
				
		final String ERROR_USAGE  = "Usage: transformSinks <queryID>";
		Integer localQueryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(localQueryID==null) {
			return;
		}
		
		SharedQueryIDModifier.addSharedQueryIDIfNeccessary(localQueryID,getActiveSession());
		ci.println("Done.");
	}
	
	public void _selectGreedy(CommandInterpreter ci) {

		
		ILoadBalancingStrategyRegistry strategyRegistry = OsgiServiceProvider.getStrategyRegistry();
		
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
		

		ILoadBalancingStrategyRegistry strategyRegistry = OsgiServiceProvider.getStrategyRegistry();
		
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


		ILoadBalancingStrategyRegistry strategyRegistry = OsgiServiceProvider.getStrategyRegistry();
		
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
		
		ILoadBalancingAllocatorRegistry allocatorRegistry = OsgiServiceProvider.getAllocatorRegistry();
		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		IPeerDictionary peerDictionary = OsgiServiceProvider.getPeerDictionary();

		
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
		
		final String ERROR_USAGE = "Usage: determineCommunicator <queryID>";
		
		Integer queryID = getIntegerParameter(ci, ERROR_USAGE);
		
		if(queryID==null) {
			return;
		}
		
		List<Integer> queryIDAsList = Lists.newArrayList();
		queryIDAsList.add(queryID);
		
		OdyLoadCommunicatorChooser chooser = new OdyLoadCommunicatorChooser();
		
		HashMap<Integer,ILoadBalancingCommunicator> result = chooser.chooseCommunicators(queryIDAsList,getActiveSession());
		ILoadBalancingCommunicator comm = result.get(queryID);
		ci.println("Chosen Communicator: " + comm.getName());
	}
	
	
	public void _allocateAndTransfer(CommandInterpreter ci) {
		
		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		ILoadBalancingAllocatorRegistry allocatorRegistry = OsgiServiceProvider.getAllocatorRegistry();
		ILoadBalancingStrategyRegistry strategyRegistry = OsgiServiceProvider.getStrategyRegistry();
		
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
		
		CostEstimationHelper.addQueryToCostMap(costMap, queryID, operatorList);
		strategy.setAllocator(allocatorRegistry.getAllocator(OdyLoadConstants.ALLOCATOR_NAME));
		
		strategy.allocateAndTransferQueries(costMap);
		
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
		IP2PNetworkManager networkManager = OsgiServiceProvider.getNetworkManager();
		return networkManager.getLocalPeerID();
	}
	
	private ILogicalQueryPart getLogicalQueryPart(int queryID) {
		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		ILogicalQuery query = executor.getLogicalQueryById(queryID, getActiveSession());
		List<ILogicalOperator> opsInQuery = Lists.newArrayList();
		RestructHelper.collectOperators(query.getLogicalPlan(),opsInQuery);
		return new LogicalQueryPart(opsInQuery);
		
	}
	
}
