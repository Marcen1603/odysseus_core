package de.uniol.inf.is.odysseus.peer.distribute.util;

import com.google.common.base.Preconditions;

public class QueryPartGraphConnection {

	private final QueryPartGraphNode fromNode;
	private final QueryPartGraphNode toNode;
	
	public QueryPartGraphConnection( QueryPartGraphNode from, QueryPartGraphNode to ) {
		Preconditions.checkNotNull(from, "From GraphPartNode must not be null!");
		Preconditions.checkNotNull(to, "To GraphPartNode must not be null!");
		
		this.fromNode = from;
		this.toNode = to;
	}
	
	public QueryPartGraphNode getStartNode() {
		return fromNode;
	}
	
	public QueryPartGraphNode getEndNode() {
		return toNode;
	}
	
	@Override
	public boolean equals(Object other) {
		if( !(other instanceof QueryPartGraphConnection)) {
			return false;
		}
		if( other == this ) {
			return true;
		}
		
		QueryPartGraphConnection conn = (QueryPartGraphConnection)other;
		return conn.fromNode.equals(fromNode) && conn.toNode.equals(toNode);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QueryPartGraphConnection[");
		sb.append(fromNode.getQueryPart()).append(" to ").append(toNode.getQueryPart());
		sb.append("]");
		return sb.toString();
	}
}
