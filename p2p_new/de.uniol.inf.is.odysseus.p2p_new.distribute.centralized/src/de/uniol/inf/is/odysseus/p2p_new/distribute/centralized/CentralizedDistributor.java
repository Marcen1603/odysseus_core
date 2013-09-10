package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.Graph;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.QSSimulator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.simulation.SimulationResult;

public class CentralizedDistributor implements ILogicalQueryDistributor {
	private static CentralizedDistributor instance;
	private static final Logger LOG = LoggerFactory.getLogger(CentralizedDistributor.class);
	private ICostModel<IPhysicalOperator> costModel = null;
	private IServerExecutor executor = null;
	protected IQueryOptimizer queryOptimizer = null;
	
	// a map containing the physical plans for each peer known to the master
	private Map<PeerID,Map<Integer,IPhysicalOperator>> operatorPlans = new HashMap<PeerID,Map<Integer,IPhysicalOperator>>();
	private CentralizedDistributorAdvertisementManager manager = CentralizedDistributorAdvertisementManager.getInstance();
	public CentralizedDistributor() {
		instance = this;
	}
	
	IP2PDictionary dictionary = P2PDictionaryService.get();
	private static final String DISTRIBUTION_TYPE = "centralized";
	
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute,
			QueryBuildConfiguration parameters) {
		if(!manager.isMaster()) {
			LOG.debug("This instance wasn't designated as the master instance and therefore isn't eligible to distribute queries in a centralized manner");
			return null;
		} else if(this.getExecutor() == null) {
			LOG.debug("The centralized distributor hasn't bound an IServerExecutor-instance");
			return null;
		} else if(this.getQueryOptimizer() == null) {
			LOG.debug("The centralized distributor hasn't bound an IQueryOptimizer-instance");
			return null;
		}
		// convert the operators of the new Queries to physical ones
		List<IPhysicalOperator> newOperators = new ArrayList<IPhysicalOperator>();
		for(ILogicalQuery q : queriesToDistribute) {
			newOperators.addAll(this.getQueryOptimizer().optimizeQuery(
					this.getExecutor(),
					q,
					new OptimizationConfiguration(parameters),
					this.getExecutor().getDataDictionary(null)).getAllOperators());
		}
		Map<Integer,IPhysicalOperator> newOperatorsMap = new HashMap<Integer,IPhysicalOperator>();
		for(IPhysicalOperator o : newOperators) {
			newOperatorsMap.put(o.hashCode(), o);
		}

		List<SimulationResult> results = new ArrayList<SimulationResult>();
	
		SimulationResult bestResult = getMostPromisingPlacement(newOperatorsMap, parameters);
		results.add(bestResult);
		List<SimulationResult> placeableResults = placeable(results);
		// only stop, if we have successfully placed every result on some peer
		while(placeableResults.size() != results.size()) {
			List<SimulationResult> currentlyUnplaceable = new ArrayList<SimulationResult>();
			for(SimulationResult s : results) {
				if(!placeableResults.contains(s)) {
					currentlyUnplaceable.add(s);
				}
			}
			for(SimulationResult s : currentlyUnplaceable) {
				SimulationResult additionalResult = getMostPromisingPlacement(splitGraph(s).getValue(), parameters);
				results.add(additionalResult);
			}
			// we now have additional results in the results-list,
			// as well as the former non-placeable results which should be placeable now that they've been cut down a little
			placeableResults = placeable(results);
			// continuing means, that all the results were placeable,
			// looping means that there were still some results who couldn't be realized
		}
		// if we reached this point, we have one or more results wating to get placed on their respective peers.
		// TODO: pack them into PhysicalQueryPartAdvertisements and get them distributed
		return null;
	}
	
	// this method will find a splitpoint within a graph of a SimulationResult,
	// so that the result would henceforth be placeable on the peer. It cuts the graph at the appropriate points and
	// returns the pruned SimulationResult as well as Map containing the surplus operators
	public SimpleImmutableEntry<SimulationResult,Map<Integer,IPhysicalOperator>> splitGraph(SimulationResult r) {
		// TODO: implement
		return new SimpleImmutableEntry<SimulationResult,Map<Integer,IPhysicalOperator>>(r,r.getPlan(true));
	}
	
	public SimulationResult getMostPromisingPlacement(Map<Integer, IPhysicalOperator> newOperatorsMap, QueryBuildConfiguration parameters) {
		SimulationResult bestResult = null;
		ICost<IPhysicalOperator> bestCost = this.costModel.getMaximumCost();
		for(PeerID peer : operatorPlans.keySet()) {
			Map<Integer,IPhysicalOperator> operatorsOnPeer = operatorPlans.get(peer);
			if(this.getCostModel() == null) {
				LOG.debug("Centralized distribution not possible without a bound costmodel");
				return null;
			}
			// this is the graph representing the plans and their connections "as is",
			// we'll work on clones in order to simulate the query-sharing
			Graph baseGraph = new Graph(operatorsOnPeer, newOperatorsMap);
			
			SimulationResult res = this.getQuerySharingSimulator().simulateQuerySharing(baseGraph.clone(), new OptimizationConfiguration(parameters));
			res.setPeer(peer);
			Map<Integer,IPhysicalOperator> mergedOps = res.getPlan(true);
			res.setCost(costModel.estimateCost(new ArrayList<IPhysicalOperator>(mergedOps.values()), false));
			if(res.getCost().compareTo(bestCost) < 0) {
				bestCost = res.getCost();
				bestResult = res;
			}
		}
		return bestResult;
	}
	
	// From a list of SimulationResults, return only those whose costs don't exceed the capacity of their peers
	public List<SimulationResult> placeable(List<SimulationResult> results) {
		List<SimulationResult> r = new ArrayList<SimulationResult>();
		for(SimulationResult res : results) {
			// Despite having the best result in terms of cost savings,
			// the cost of placing this (partial) plan as-is would still be too much given the resources of the peer
			if(res.getCost().compareTo(determineBearableCost(res.getPeer())) > 0 ) {
				r.add(res);
			}
		}
		return r;
	}
	
	// function to determine the load a peer could potentially bear, represented as ICost
	public ICost<IPhysicalOperator> determineBearableCost(PeerID peer) {
		// TODO: implement
		return null;
	}
	
	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}
	
	public void setPhysicalPlan(PeerID peer, Map<Integer,IPhysicalOperator> operators) {
		this.operatorPlans.put(peer,operators);
	}
	
	public static CentralizedDistributor getInstance() {
		return instance;
	}
	
	public void bindCostModel(ICostModel<IPhysicalOperator> costModel) {
		this.costModel = costModel;
	}

	public void unbindCostModel(ICostModel<IPhysicalOperator> costModel) {
		this.costModel = null;
	}
	
	public ICostModel<IPhysicalOperator> getCostModel() {
		return this.costModel;
	}
	
	public void bindExecutor(IExecutor exe) {
		executor = (IServerExecutor) exe;
		LOG.debug("Executor bound: " + exe);
	}
	
	public void unbindExecutor(IExecutor exe) {
		executor = null;
		LOG.debug("Executor unbound: " + exe);
	}
	
	private IServerExecutor getExecutor() {
		return this.executor;
	}
	
	public void bindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
		LOG.debug("QueryOptimizer bound: " + queryOptimizer);
	}

	public void unbindQueryOptimizer(IQueryOptimizer queryOptimizer) {
		this.queryOptimizer = null;
		LOG.debug("QueryOptimizer unbound: " + queryOptimizer);
	}
	
	private IQueryOptimizer getQueryOptimizer() {
		return this.queryOptimizer;
	}
	
	private QSSimulator getQuerySharingSimulator() {
		return QSSimulator.getInstance();
	}

}
