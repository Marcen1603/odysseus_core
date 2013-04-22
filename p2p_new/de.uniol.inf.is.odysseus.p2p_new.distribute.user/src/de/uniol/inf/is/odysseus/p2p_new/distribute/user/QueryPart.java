package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class QueryPart {

	private static final String DESTINATION_PQLPARAMETER_KEY = "DESTINATION";
	private static final String PARSER_ID = "PQL";

	private final Map<ILogicalOperator, List<JxtaReceiverAO>> relativeSources;

	private final Map<ILogicalOperator, List<JxtaSenderAO>> relativeSinks;

	private final Collection<ILogicalOperator> operators;

	private final String destinationName;

	private final String name;

	public QueryPart(Collection<ILogicalOperator> operators, String destinationName, String partName) {
		Preconditions.checkNotNull(operators, "List of operators must not be null!");
		Preconditions.checkArgument(!filter(operators).isEmpty(), "List of operators must not be empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationName), "Destination name must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(partName), "Query Part name must not be null or empty!");

		this.destinationName = destinationName;
		this.operators = filter(operators);
		this.name = partName;
		this.relativeSinks = determineRelativeSinks(this.operators);
		this.relativeSources = determineRelativeSources(this.operators);
	}

	public final void addAccessAO(JxtaReceiverAO accessAO, ILogicalOperator forOperator) {
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSources.containsKey(forOperator));

		relativeSources.get(forOperator).add(accessAO);
	}

	public final void addSenderAO(JxtaSenderAO senderAO, ILogicalOperator forOperator) {
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSinks.containsKey(forOperator));

		relativeSinks.get(forOperator).add(senderAO);
	}

	public final boolean containsRelativeSource(ILogicalOperator op) {
		return relativeSources.containsKey(op);
	}

	public final String getDestinationName() {
		return destinationName;
	}

	public final String getName() {
		return name;
	}

	public final ImmutableCollection<ILogicalOperator> getOperators() {
		return ImmutableList.copyOf(operators);
	}

	public final ImmutableCollection<ILogicalOperator> getRelativeSinks() {
		return ImmutableList.copyOf(relativeSinks.keySet());
	}

	public final ImmutableCollection<ILogicalOperator> getRelativeSources() {
		return ImmutableList.copyOf(relativeSources.keySet());
	}

	public final void removeDestinationName() {
		for (final ILogicalOperator operator : operators) {
			operator.setDestinationName(null);
			operator.addParameterInfo(DESTINATION_PQLPARAMETER_KEY, null);
		}
	}

	public final ILogicalQuery toLogicalQuery(IPQLGenerator generator) {
		final ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(getOneTopOperator(), true);
		query.setName(getName());
		query.setParserId(PARSER_ID);
		query.setPriority(0);
		query.setUser(SessionManagementService.getActiveSession());
		query.setQueryText(generator.generatePQLStatement(query.getLogicalPlan()));
		return query;
	}

	@Override
	public String toString() {
		return getName();
	}

	private ILogicalOperator getOneTopOperator() {
		for (final ILogicalOperator relativeSink : relativeSinks.keySet()) {
			final List<JxtaSenderAO> senderAOs = relativeSinks.get(relativeSink);
			if (!senderAOs.isEmpty()) {
				return senderAOs.get(0);
			}
		}

		return relativeSinks.keySet().iterator().next();
	}

	private static boolean oneTargetNotInList(Collection<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {
		for (final LogicalSubscription subscription : subscriptions) {
			if (!operators.contains(subscription.getTarget())) {
				return true;
			}
		}
		return false;
	}

	private static Map<ILogicalOperator, List<JxtaSenderAO>> determineRelativeSinks(Collection<ILogicalOperator> operators) {
		final Map<ILogicalOperator, List<JxtaSenderAO>> sinksMap = Maps.newHashMap();
		for (final ILogicalOperator operator : operators) {
			if (operator.getSubscriptions().size() == 0 || oneTargetNotInList(operators, operator.getSubscriptions())) {
				sinksMap.put(operator, new ArrayList<JxtaSenderAO>());
			}
		}
		return sinksMap;
	}

	private static Map<ILogicalOperator, List<JxtaReceiverAO>> determineRelativeSources(Collection<ILogicalOperator> operators) {
		final Map<ILogicalOperator, List<JxtaReceiverAO>> sourcesMap = Maps.newHashMap();
		for (final ILogicalOperator operator : operators) {
			if (operator.getSubscribedToSource().size() == 0 || oneTargetNotInList(operators, operator.getSubscribedToSource())) {
				sourcesMap.put(operator, new ArrayList<JxtaReceiverAO>());
			}
		}
		return sourcesMap;
	}

	private static Collection<ILogicalOperator> filter(Collection<ILogicalOperator> operators) {
		final ImmutableList.Builder<ILogicalOperator> filteredOperators = new ImmutableList.Builder<>();
		for (final ILogicalOperator operator : operators) {
			if (!(operator instanceof TopAO)) {
				filteredOperators.add(operator);
			}
		}
		return filteredOperators.build();
	}

}
