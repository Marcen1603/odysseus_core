package de.uniol.inf.is.odysseus.p2p_new.distributor.user;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class QueryPart {

	private static final String DESTINATION_PQLPARAMETER_KEY = "DESTINATION";
	private static final String PARSER_ID = "PQL";
	
	private final Map<ILogicalOperator, List<AccessAO>> relativeSources;
	private final Map<ILogicalOperator, List<SenderAO>> relativeSinks;
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

	public final ImmutableCollection<ILogicalOperator> getOperators() {
		return ImmutableList.copyOf(operators);
	}
	
	public final boolean containsRelativeSource( ILogicalOperator op ){
		return relativeSources.containsKey(op);
	}
	
	public final ImmutableCollection<ILogicalOperator> getRelativeSources() {
		return ImmutableList.copyOf(relativeSources.keySet());
	}
	
	public final ImmutableCollection<ILogicalOperator> getRelativeSinks() {
		return ImmutableList.copyOf(relativeSinks.keySet());
	}
	
	public final String getDestinationName() {
		return destinationName;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void addSenderAO( SenderAO senderAO, ILogicalOperator forOperator ) {
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSinks.containsKey(forOperator));
		
		relativeSinks.get(forOperator).add(senderAO);
	}
	
	public final void addAccessAO( AccessAO accessAO, ILogicalOperator forOperator ) {
		Preconditions.checkNotNull(forOperator, "Operator to set sender for must not be null!");
		Preconditions.checkArgument(relativeSources.containsKey(forOperator));
		
		relativeSources.get(forOperator).add(accessAO);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public final ILogicalQuery toLogicalQuery(IPQLGenerator generator) {
		ILogicalQuery query = new LogicalQuery();
		query.setLogicalPlan(getOneTopOperator(), true);
		query.setName(getName());
		query.setParserId(PARSER_ID);
		query.setPriority(0);
		query.setUser(SessionManagementService.getActiveSession());
		query.setQueryText(generator.generatePQLStatement(query.getLogicalPlan()));
		return query;
	}

	public final void removeDestinationName() {
		for( ILogicalOperator operator : operators ) {
			operator.setDestinationName(null);
			operator.addParameterInfo(DESTINATION_PQLPARAMETER_KEY, null);
		}
	}

	private ILogicalOperator getOneTopOperator() {
		for( ILogicalOperator relativeSink : relativeSinks.keySet() ) {
			List<SenderAO> senderAOs = relativeSinks.get(relativeSink);
			if( !senderAOs.isEmpty() ) {
				return senderAOs.get(0);
			}
		}

		return relativeSinks.keySet().iterator().next();
	}
	
	private static Map<ILogicalOperator, List<AccessAO>> determineRelativeSources(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, List<AccessAO>> sourcesMap = Maps.newHashMap();
		for( ILogicalOperator operator : operators ) {
			if( operator.getSubscribedToSource().size() == 0 || allTargetsNotInList(operators, operator.getSubscribedToSource())) {
				sourcesMap.put(operator, new ArrayList<AccessAO>());
			} 
		}
		return sourcesMap;
	}

	private static Map<ILogicalOperator, List<SenderAO>> determineRelativeSinks(Collection<ILogicalOperator> operators) {
		Map<ILogicalOperator, List<SenderAO>> sinksMap = Maps.newHashMap();
		for( ILogicalOperator operator : operators ) {
			if( operator.getSubscriptions().size() == 0 || allTargetsNotInList(operators, operator.getSubscriptions())) {
				sinksMap.put(operator, new ArrayList<SenderAO>());
			} 
		}
		return sinksMap;
	}

	private static boolean allTargetsNotInList(Collection<ILogicalOperator> operators, Collection<LogicalSubscription> subscriptions) {
		for( LogicalSubscription subscription : subscriptions ) {
			if( operators.contains(subscription.getTarget())) {
				return false;
			}
		}
		return true;
	}

	private static Collection<ILogicalOperator> filter(Collection<ILogicalOperator> operators) {
		ImmutableList.Builder<ILogicalOperator> filteredOperators = new ImmutableList.Builder<>();
		for( ILogicalOperator operator : operators ) {
			if( !(operator instanceof TopAO )) {
				filteredOperators.add(operator);
			}
		}
		return filteredOperators.build();
	}

}
