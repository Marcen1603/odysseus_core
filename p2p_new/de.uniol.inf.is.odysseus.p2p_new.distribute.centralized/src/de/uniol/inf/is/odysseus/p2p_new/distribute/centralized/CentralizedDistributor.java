package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service.P2PDictionaryService;

public class CentralizedDistributor implements ILogicalQueryDistributor {
	private static CentralizedDistributor instance;
	// a map containing the physical plans for each peer known to the master
	private Map<PeerID,Map<Integer,IPhysicalOperator>> operatorPlans = new HashMap<PeerID,Map<Integer,IPhysicalOperator>>();
	
	public CentralizedDistributor() {
		instance = this;
	}
	
	IP2PDictionary dictionary = P2PDictionaryService.get();
	private static final String DISTRIBUTION_TYPE = "centralized";
	
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute,
			QueryBuildConfiguration parameters) {
		// TODO Auto-generated method stub
		// gather information of all the peers and their physical plans
		// 
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

}
