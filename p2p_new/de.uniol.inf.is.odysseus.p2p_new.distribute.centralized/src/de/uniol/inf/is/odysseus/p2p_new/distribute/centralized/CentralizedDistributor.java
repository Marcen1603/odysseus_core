package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

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
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;

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

		
		
		for(PeerID peer : operatorPlans.keySet()) {
			Map<Integer,IPhysicalOperator> operatorsOnPeer = operatorPlans.get(peer);
			if(this.getCostModel() == null) {
				LOG.debug("Centralized distribution not possible without a bound costmodel");
				return null;
			}
			List<IPhysicalOperator> mergedOps = simulateQS(operatorsOnPeer,newOperators);
			costModel.estimateCost(mergedOps, false);
		}
		return null;
	}

	private List<IPhysicalOperator> simulateQS(
			Map<Integer, IPhysicalOperator> operatorsOnPeer,
			List<IPhysicalOperator> newOperators) {
		// TODO Auto-generated method stub
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

}
