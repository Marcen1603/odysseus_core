package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

public class QueryPartGraphNode {

	private final ILogicalQueryPart queryPart;
	
	private Collection<QueryPartGraphNode> previous = Lists.newArrayList();
	private Collection<QueryPartGraphNode> next = Lists.newArrayList();
	
	QueryPartGraphNode( ILogicalQueryPart queryPart) {
		Preconditions.checkNotNull(queryPart, "Query Part for queryPartGraph must not be null!");
		
		this.queryPart = queryPart;
	}
	
	public ILogicalQueryPart getQueryPart() {
		return queryPart;
	}
	
	void setPreviousNodes(Collection<QueryPartGraphNode> nodes ) {
		previous.clear();
		previous.addAll(nodes); 
	}
	
	void setNextNodes( Collection<QueryPartGraphNode> nodes ) {
		next.clear();
		next.addAll(nodes);
	}
	
	public ImmutableCollection<QueryPartGraphNode> getPreviousNodes() {
		return ImmutableList.<QueryPartGraphNode>copyOf(previous);
	}
	
	public ImmutableCollection<QueryPartGraphNode> getNextNodes() {
		return ImmutableList.<QueryPartGraphNode>copyOf(next);
	}
}
