package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communicatorchooser.odyload;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ICommunicatorChooser;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.odyload.OdyLoadConstants;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingCommunicatorRegistry;

public class OdyLoadCommunicatorChooser implements ICommunicatorChooser {

	private static final String CHOOSER_NAME = "OdyLoad";
	
	private static ILoadBalancingCommunicatorRegistry communicatorRegistry;
	private static IServerExecutor executor;
	
	public static void bindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		communicatorRegistry = serv;
	}
	
	public static void unbindCommunicatorRegistry(ILoadBalancingCommunicatorRegistry serv) {
		if(communicatorRegistry==serv) {
			communicatorRegistry=null;
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

	@Override
	public HashMap<Integer, ILoadBalancingCommunicator> chooseCommunicators(
			List<Integer> queryIds, ISession session) {

		HashMap<Integer, ILoadBalancingCommunicator> queryCommunicatorMapping = new HashMap<Integer, ILoadBalancingCommunicator>();

		for (int queryId : queryIds) {
			queryCommunicatorMapping.put(queryId,
					chooseCommunicator(queryId, session));
		}

		return queryCommunicatorMapping;
	}

	private static boolean isActive(int queryId) {

		QueryState queryState = executor.getQueryState(queryId);
		if (queryState == QueryState.INACTIVE) {
			return false;
		}
		return true;
	}

	private static boolean hasStatefulOperator(int queryId, ISession session) {

		HashMap<Integer, IPhysicalQuery> queries = getPhysicalQueries(session);
		Collection<IPhysicalOperator> operatorsInQuery = queries.get(queryId)
				.getAllOperators();
		for (IPhysicalOperator op : operatorsInQuery) {
			if (op instanceof IStatefulPO)
				return true;
		}
		return false;

	}

	private static HashMap<Integer, IPhysicalQuery> getPhysicalQueries(
			ISession session) {

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

	@Override
	public ILoadBalancingCommunicator chooseCommunicator(int queryID,
			ISession session) {


		if (!isActive(queryID)) {
			ILoadBalancingCommunicator inactiveQueryCommunicator = communicatorRegistry
					.getCommunicator(OdyLoadConstants.INACTIVE_QUERIES_COMMUNICATOR_NAME);
			return inactiveQueryCommunicator;
		} else {
			if (!hasStatefulOperator(queryID, session)) {
				ILoadBalancingCommunicator parallelTrackCommunicator = communicatorRegistry
						.getCommunicator(OdyLoadConstants.STATELESS_QUERIES_COMMUNICATOR_NAME);
				return parallelTrackCommunicator;
			} else {
				ILoadBalancingCommunicator movingStateCommunicator = communicatorRegistry
						.getCommunicator(OdyLoadConstants.STATEFUL_QUERIES_COMMUNICATOR_NAME);
				return movingStateCommunicator;
			}
		}
	}

	@Override
	public String getName() {
		return CHOOSER_NAME;
	}

}
