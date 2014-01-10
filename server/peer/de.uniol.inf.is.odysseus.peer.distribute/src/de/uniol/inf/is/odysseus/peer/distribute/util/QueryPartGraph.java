package de.uniol.inf.is.odysseus.peer.distribute.util;

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

public final class QueryPartGraph {

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
			
			Collection<ILogicalQueryPart> nextQueryParts = determineNextQueryParts(queryPart, operatorPartMap);
			Collection<ILogicalQueryPart> previousQueryParts = determinePreviousQueryParts(queryPart, operatorPartMap);
			
			QueryPartGraphNode graphNode = nodeMap.get(queryPart);
			graphNode.setNextNodes(transformToNodes(nextQueryParts, nodeMap));
			graphNode.setPreviousNodes(transformToNodes(previousQueryParts, nodeMap));
		}
		
		return nodeMap;
	}

	private static Collection<QueryPartGraphNode> transformToNodes(Collection<ILogicalQueryPart> queryParts, Map<ILogicalQueryPart, QueryPartGraphNode> nodeMap) {
		Collection<QueryPartGraphNode> nodes = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : queryParts ) {
			nodes.add(nodeMap.get(queryPart));
		}
		return nodes;
	}

	private static Collection<ILogicalQueryPart> determineNextQueryParts(ILogicalQueryPart queryPart, Map<ILogicalOperator, ILogicalQueryPart> operatorPartMap) {
		Collection<ILogicalQueryPart> nextParts = Lists.newArrayList();
		Collection<ILogicalOperator> relativeSinks = LogicalQueryHelper.getRelativeSinksOfLogicalQueryPart(queryPart);
		for( ILogicalOperator relativeSink : relativeSinks ) {
			
			for( LogicalSubscription sub : relativeSink.getSubscriptions() ) {
				ILogicalOperator target = sub.getTarget();
				ILogicalQueryPart queryPartOfTarget = operatorPartMap.get(target);
				
				if( queryPartOfTarget != null && !queryPartOfTarget.equals(queryPart)) {
					nextParts.add(queryPartOfTarget);
				}
			}
		}
		return nextParts;
	}
	
	private static Collection<ILogicalQueryPart> determinePreviousQueryParts(ILogicalQueryPart queryPart, Map<ILogicalOperator, ILogicalQueryPart> operatorPartMap) {
		Collection<ILogicalQueryPart> previousParts = Lists.newArrayList();
		Collection<ILogicalOperator> relativeSources = LogicalQueryHelper.getRelativeSourcesOfLogicalQueryPart(queryPart);
		for( ILogicalOperator relativeSource : relativeSources ) {
			
			for( LogicalSubscription sub : relativeSource.getSubscribedToSource() ) {
				ILogicalOperator target = sub.getTarget();
				ILogicalQueryPart queryPartOfTarget = operatorPartMap.get(target);
				
				if( queryPartOfTarget != null  && !queryPartOfTarget.equals(queryPart)) {
					previousParts.add(queryPartOfTarget);
				}
			}
		}
		return previousParts;
	}
}
