package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.odyload.strategy.CommunicatorChooser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.OsgiServiceProvider;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.interfaces.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingCommunicatorRegistry;

public class OdyLoadCommunicatorChooser implements ICommunicatorChooser {

@Override
public HashMap<Integer,ILoadBalancingCommunicator> chooseCommunicators(List<Integer> queryIds,ISession session) {
	
		ILoadBalancingCommunicatorRegistry communicatorRegistry = OsgiServiceProvider.getCommunicatorRegistry();
		
		HashMap<Integer,ILoadBalancingCommunicator> queryCommunicatorMapping = new HashMap<Integer,ILoadBalancingCommunicator>();
		for (int queryId : queryIds) {
			if(!isActive(queryId)) {
				ILoadBalancingCommunicator inactiveQueryCommunicator = communicatorRegistry.getCommunicator(OdyLoadConstants.INACTIVE_QUERIES_COMMUNICATOR_NAME);
				queryCommunicatorMapping.put(queryId, inactiveQueryCommunicator);
			}
			if(!hasStatefulOperator(queryId,session)) {
				ILoadBalancingCommunicator parallelTrackCommunicator = communicatorRegistry.getCommunicator(OdyLoadConstants.STATELESS_QUERIES_COMMUNICATOR_NAME);
				queryCommunicatorMapping.put(queryId, parallelTrackCommunicator);
			}
			ILoadBalancingCommunicator movingStateCommunicator = communicatorRegistry.getCommunicator(OdyLoadConstants.STATEFUL_QUERIES_COMMUNICATOR_NAME);
			queryCommunicatorMapping.put(queryId, movingStateCommunicator);
			
		}
		
		return queryCommunicatorMapping;
	}
	

	
	
	private static boolean isActive(int queryId) {

		IServerExecutor executor = OsgiServiceProvider.getExecutor();
		QueryState queryState = executor.getQueryState(queryId);
		if(queryState==QueryState.INACTIVE) {
			return false;
		}
		return true;
	}
	
	private static boolean hasStatefulOperator(int queryId, ISession session) {

		HashMap<Integer,IPhysicalQuery> queries = getPhysicalQueries(session);
		Collection<IPhysicalOperator> operatorsInQuery = queries.get(queryId).getAllOperators();
		for (IPhysicalOperator op : operatorsInQuery) {
			if(op instanceof IStatefulPO)
				return true;
		}
		return false;
		
		
	}
	

	private static HashMap<Integer, IPhysicalQuery> getPhysicalQueries(ISession session) {
		

		IServerExecutor executor = OsgiServiceProvider.getExecutor();

		HashMap<Integer, IPhysicalQuery> queries = new HashMap<Integer, IPhysicalQuery>();

		for (int queryId : executor.getLogicalQueryIds(session)) {
			
			IPhysicalQuery query = executor.getExecutionPlan().getQueryById(
					queryId);
			if (query == null)
				continue;
			queries.put(queryId, query);
		}

		return queries;
	}
}
