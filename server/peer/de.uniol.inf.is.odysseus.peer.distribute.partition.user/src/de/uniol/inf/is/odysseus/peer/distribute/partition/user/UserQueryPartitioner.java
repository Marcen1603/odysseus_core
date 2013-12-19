package de.uniol.inf.is.odysseus.peer.distribute.partition.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;

public class UserQueryPartitioner implements IQueryPartitioner {

	private static final String LOCAL_DESTINATION_NAME = "local";

	@Override
	public String getName() {
		return "user";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config, List<String> partitionParameters) throws QueryPartitionException {
		return determineQueryParts(operators);
	}

	private static Collection<ILogicalQueryPart> determineQueryParts(Collection<ILogicalOperator> operators) {
		final List<ILogicalQueryPart> parts = Lists.newArrayList();

		final Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		final List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(operators);

		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator chosenOperator = operatorsToVisit.get(0);
			String chosenDestination = destinations.get(chosenOperator);

			List<ILogicalOperator> partOperators = Lists.newArrayList();
			collectOperatorsWithEqualDestination(chosenOperator, chosenDestination, partOperators, destinations);

			operatorsToVisit.removeAll(partOperators);

			ILogicalQueryPart part = new LogicalQueryPart(partOperators);
			for (ILogicalOperator operator : partOperators) {
				operator.setDestinationName(chosenDestination);
			}
			parts.add(part);
		}

		return parts;
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(Collection<ILogicalOperator> operators) {
		final Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (final ILogicalOperator operator : operators) {
			String destinationName = getDestinationName(operator);
			if( !Strings.isNullOrEmpty(destinationName)) {
				throw new RuntimeException("Could not determine destination name of operator " + operator);
			}
			destinationNames.put(operator, destinationName);
		}

		return destinationNames;
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if (operator instanceof StreamAO) {
			return LOCAL_DESTINATION_NAME;
		}

		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		}
		if (operator.getSubscribedToSource().size() > 0) {
			return getDestinationName(getOnePreviousOperator(operator));
		}

		return LOCAL_DESTINATION_NAME;
	}

	private static ILogicalOperator getOnePreviousOperator(ILogicalOperator operator) {
		return operator.getSubscribedToSource().iterator().next().getTarget();
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
