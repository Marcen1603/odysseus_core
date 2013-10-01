package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignment;
import de.uniol.inf.is.odysseus.p2p_new.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;

public class UserDefinedDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(UserDefinedDistributor.class);

	private static final String DISTRIBUTION_TYPE = "user";

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute, QueryBuildConfiguration transCfg) {

		if (queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			return queriesToDistribute;
		}

		// A list of all available remote peers
		final Collection<PeerID> remotePeerIDs = DistributionHelper.getAvailableRemotePeers();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queriesToDistribute;
			
		} 

		final List<ILogicalQuery> localQueries = Lists.newArrayList();

		for (final ILogicalQuery query : queriesToDistribute) {

			final List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			RestructHelper.removeTopAOs(operators);

			final List<QueryPart> queryParts = determineQueryParts(operators, query.toString());
			LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
			if (queryParts.size() == 1 && queryParts.get(0).getDestinationName().equals(DistributionHelper.LOCAL_DESTINATION_NAME)) {
				localQueries.add(query);
				LOG.debug("This one part is executed locally");
				continue;
			}
			
			// The peer assignment strategy to be used
			final IPeerAssignment peerAssignmentStrategy = DistributionHelper.determinePeerAssignmentStrategy(transCfg);
			final Map<QueryPart, PeerID> queryPartDistributionMap = peerAssignmentStrategy.assignQueryPartsToPeers(remotePeerIDs, queryParts);
			
			DistributionHelper.generatePeerConnections(queryPartDistributionMap);
			
			final ILogicalQuery logicalQuery = DistributionHelper.distributeAndTransformParts(queryParts, queryPartDistributionMap, transCfg, query.toString());

			localQueries.add(logicalQuery);
		}

		return localQueries;
	}

	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}
	
	private static List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, String baseName) {
		final List<QueryPart> parts = Lists.newArrayList();

		final Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		final List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(operators);

		while (!operatorsToVisit.isEmpty()) {
			final ILogicalOperator chosenOperator = operatorsToVisit.get(0);
			final String chosenDestination = destinations.get(chosenOperator);

			final List<ILogicalOperator> partOperators = Lists.newArrayList();
			collectOperatorsWithEqualDestination(chosenOperator, chosenDestination, partOperators, destinations);

			operatorsToVisit.removeAll(partOperators);

			final QueryPart part = new QueryPart(partOperators, chosenDestination);
			parts.add(part);
		}

		return parts;
	}
	
	private static Map<ILogicalOperator, String> determineDestinationNames(List<ILogicalOperator> operators) {
		final Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (final ILogicalOperator operator : operators) {
			destinationNames.put(operator, getDestinationName(operator));
		}

		return destinationNames;
	}
	
	private static String getDestinationName(ILogicalOperator operator) {
		if( operator instanceof StreamAO ) {
			return DistributionHelper.LOCAL_DESTINATION_NAME;
		}
		
		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		}
		if (operator.getSubscribedToSource().size() > 0) {
			return getDestinationName(operator.getSubscribedToSource().iterator().next().getTarget());
		}
		
		return DistributionHelper.LOCAL_DESTINATION_NAME;
	}
	
	private static void collectOperatorsWithEqualDestination(ILogicalOperator operator, String chosenDestination, List<ILogicalOperator> collectedOperators, Map<ILogicalOperator, String> destinations) {
		if (collectedOperators.contains(operator)) {
			return;
		}

		if (destinations.get(operator).equalsIgnoreCase(chosenDestination)) {

			collectedOperators.add(operator);

			for (final LogicalSubscription subscription : operator.getSubscriptions()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}

			for (final LogicalSubscription subscription : operator.getSubscribedToSource()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}
		}
	}
}
