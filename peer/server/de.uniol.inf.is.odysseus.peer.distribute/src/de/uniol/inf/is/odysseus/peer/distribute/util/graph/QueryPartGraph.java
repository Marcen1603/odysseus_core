package de.uniol.inf.is.odysseus.peer.distribute.util.graph;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public final class QueryPartGraph {

	private static class QueryPartRelation {
		
		public ILogicalQueryPart queryPart;
		public LogicalSubscription subscription;
		public ILogicalOperator fromOperator;
		
		public QueryPartRelation( ILogicalQueryPart queryPart, LogicalSubscription subscription, ILogicalOperator fromOperator ) {
			this.queryPart = queryPart;
			this.subscription = subscription;
			this.fromOperator = fromOperator;
		}
	}
	
	private final Collection<ILogicalQueryPart> queryParts = Lists.newArrayList();
	private final Map<ILogicalQueryPart, QueryPartGraphNode> graphNodes;
	
	public QueryPartGraph( Collection<ILogicalQueryPart> queryParts ) {
		Preconditions.checkNotNull(queryParts, "Collection of query Parts must not be null!");
		Preconditions.checkArgument(!queryParts.isEmpty(), "Collection of query parts must not be empty!");
		
		this.queryParts.addAll(queryParts);
		this.graphNodes = createGraph(this.queryParts);
	}
	
	public ImmutableCollection<QueryPartGraphNode> getGraphNodes() {
		return ImmutableList.copyOf(graphNodes.values());
	}
	
	public QueryPartGraphNode getGraphNode( ILogicalQueryPart queryPart ) {
		Preconditions.checkNotNull(queryPart, "Query Part to get query part graph node must not be null!");
		Preconditions.checkArgument(queryParts.contains(queryPart), "Query Part %s is not known in this graph!", queryPart);
		
		return graphNodes.get(queryPart);
	}
	
	public boolean contains( ILogicalQueryPart queryPart ) {
		return queryParts.contains(queryPart);
	}
	
	private static Map<ILogicalQueryPart, QueryPartGraphNode> createGraph( Collection<ILogicalQueryPart> queryParts ) {
		Map<ILogicalQueryPart, QueryPartGraphNode> nodeMap = Maps.newHashMap();
		Map<ILogicalOperator, ILogicalQueryPart> operatorPartMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			nodeMap.put(queryPart, new QueryPartGraphNode(queryPart));
			for( ILogicalOperator operator : queryPart.getOperators() ) {
				operatorPartMap.put(operator, queryPart);
			}
		}
		
		for( ILogicalQueryPart queryPart : nodeMap.keySet()) {
			
			Collection<QueryPartRelation> nextQueryPartRelations = determineNextQueryParts(queryPart, operatorPartMap);
			Collection<QueryPartRelation> previousQueryPartRelations = determinePreviousQueryParts(queryPart, operatorPartMap);
			
			QueryPartGraphNode graphNode = nodeMap.get(queryPart);
			
			for( QueryPartRelation nextQueryPartRelation : nextQueryPartRelations ) {
				QueryPartGraphNode nextGraphNode = nodeMap.get(nextQueryPartRelation.queryPart);
				
				QueryPartGraphConnection connection = new QueryPartGraphConnection(graphNode, nextQueryPartRelation.fromOperator, nextGraphNode, nextQueryPartRelation.subscription.getTarget());
				
				graphNode.addConnection(connection);
				nextGraphNode.addConnection(connection);
			}
			
			for( QueryPartRelation prevQueryPartRelation : previousQueryPartRelations ) {
				QueryPartGraphNode prevGraphNode = nodeMap.get(prevQueryPartRelation.queryPart);
				QueryPartGraphConnection connection = new QueryPartGraphConnection(prevGraphNode, prevQueryPartRelation.subscription.getTarget(), graphNode, prevQueryPartRelation.fromOperator);
				
				graphNode.addConnection(connection);
				prevGraphNode.addConnection(connection);
			}
		}
		
		return nodeMap;
	}

	private static Collection<QueryPartRelation> determineNextQueryParts(ILogicalQueryPart queryPart, Map<ILogicalOperator, ILogicalQueryPart> operatorPartMap) {
		Collection<QueryPartRelation> nextParts = Lists.newArrayList();
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
		for( ILogicalOperator relativeSink : relativeSinks ) {
			
			for( LogicalSubscription sub : relativeSink.getSubscriptions() ) {
				ILogicalOperator target = sub.getTarget();
				ILogicalQueryPart queryPartOfTarget = operatorPartMap.get(target);
				
				if( queryPartOfTarget != null && !queryPartOfTarget.equals(queryPart)) {
					nextParts.add(new QueryPartRelation(queryPartOfTarget, sub, relativeSink));
				}
			}
		}
		return nextParts;
	}
	
	private static Collection<QueryPartRelation> determinePreviousQueryParts(ILogicalQueryPart queryPart, Map<ILogicalOperator, ILogicalQueryPart> operatorPartMap) {
		Collection<QueryPartRelation> previousParts = Lists.newArrayList();
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(queryPart);
		for( ILogicalOperator relativeSource : relativeSources ) {
			
			for( LogicalSubscription sub : relativeSource.getSubscribedToSource() ) {
				ILogicalOperator target = sub.getTarget();
				ILogicalQueryPart queryPartOfTarget = operatorPartMap.get(target);
				
				if( queryPartOfTarget != null  && !queryPartOfTarget.equals(queryPart)) {
					previousParts.add(new QueryPartRelation(queryPartOfTarget, sub, relativeSource));
				}
			}
		}
		return previousParts;
	}
}
