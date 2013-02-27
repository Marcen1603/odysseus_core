package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class P2PDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDistributor.class);
	private static final Random RAND = new Random();
	private static final String LOCAL_DESTINATION_NAME = "___local___";

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {

		for (ILogicalQuery query : queriesToDistribute) {
			List<ILogicalOperator> operators = Lists.newArrayList();
			collectOperators(query.getLogicalPlan(), operators);

			List<QueryPart> queryParts = determineQueryParts(operators);
			
			
		}

		return queriesToDistribute;
	}

	private static List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		List<QueryPart> parts = Lists.newArrayList();

		Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(operators);

		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator chosenOperator = chooseOneOperator(operatorsToVisit);
			String chosenDestination = destinations.get(chosenOperator);

			List<ILogicalOperator> partOperators = Lists.newArrayList();
			collectOperatorsWithEqualDestination(chosenOperator, chosenDestination, partOperators, destinations);
			
			operatorsToVisit.removeAll(partOperators);
			
			QueryPart part = new QueryPart(partOperators, chosenDestination);
			parts.add(part);
		}

		return parts;
	}
	
	private static void determinePeers() {
		try {
			Enumeration<Advertisement> peerAdvertisements = P2PNewPlugIn.getDiscoveryService().getLocalAdvertisements(DiscoveryService.PEER, null, null);
			while( peerAdvertisements.hasMoreElements() ) {
				PeerAdvertisement adv = (PeerAdvertisement)peerAdvertisements.nextElement();
				
			}
		} catch (IOException ex) {
			LOG.error("Could not get peers", ex);
		}
	}

	private static void collectOperatorsWithEqualDestination(ILogicalOperator operator, String chosenDestination, List<ILogicalOperator> collectedOperators, Map<ILogicalOperator, String> destinations) {
		if( collectedOperators.contains(operator) ) {
			return;
		}
		
		if( destinations.get(operator).equalsIgnoreCase(chosenDestination)) {
			
			collectedOperators.add(operator);
			
			for( LogicalSubscription subscription : operator.getSubscriptions() ) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}
			
			for( LogicalSubscription subscription : operator.getSubscribedToSource() ) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}
		}
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(List<ILogicalOperator> operators) {
		Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (ILogicalOperator operator : operators) {
			destinationNames.put(operator, getDestinationName(operator));
		}

		return destinationNames;
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		} else {
			if (operator.getSubscribedToSource().size() > 0) {
				return getDestinationName(operator.getSubscribedToSource().iterator().next().getTarget());
			} else {
				return LOCAL_DESTINATION_NAME;
			}
		}
	}

	private static ILogicalOperator chooseOneOperator(List<ILogicalOperator> operatorsToVisit) {
		int i = RAND.nextInt(operatorsToVisit.size());
		return operatorsToVisit.get(i);
	}

	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) { 
			
			list.add(currentOperator);
			
			for (LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getTarget(), list);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getTarget(), list);
			}
		}
	}

//	private static List<ILogicalOperator> determineSourceOperators(List<ILogicalOperator> operators) {
//		List<ILogicalOperator> sources = Lists.newArrayList();
//
//		for (ILogicalOperator operator : operators) {
//			if (operator.getSubscribedToSource().size() == 0) {
//				sources.add(operator);
//			}
//		}
//
//		return sources;
//	}
}
