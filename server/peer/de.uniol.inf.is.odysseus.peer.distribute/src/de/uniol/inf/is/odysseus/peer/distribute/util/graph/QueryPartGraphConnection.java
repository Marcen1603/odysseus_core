package de.uniol.inf.is.odysseus.peer.distribute.util.graph;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class QueryPartGraphConnection {

	private final QueryPartGraphNode fromNode;
	private final QueryPartGraphNode toNode;
	
	private final ILogicalOperator fromOperator;
	private final ILogicalOperator toOperator;
	
	public QueryPartGraphConnection( QueryPartGraphNode from, ILogicalOperator fromOp, QueryPartGraphNode to, ILogicalOperator toOp ) {
		Preconditions.checkNotNull(from, "From GraphPartNode must not be null!");
		Preconditions.checkNotNull(to, "To GraphPartNode must not be null!");
		Preconditions.checkNotNull(fromOp, "From Operator must not be null!");
		Preconditions.checkNotNull(toOp, "To Operator must not be null!");
		
		Preconditions.checkArgument(from.getQueryPart().getOperators().contains(fromOp), "From Operator must be inside the From Query Parts");
		Preconditions.checkArgument(to.getQueryPart().getOperators().contains(toOp), "To Operator must be inside the To Query Parts");
		
		this.fromNode = from;
		this.toNode = to;
		this.fromOperator = fromOp;
		this.toOperator = toOp;
	}
	
	public QueryPartGraphNode getStartNode() {
		return fromNode;
	}
	
	public QueryPartGraphNode getEndNode() {
		return toNode;
	}
	
	public ILogicalOperator getStartOperator() {
		return fromOperator;
	}
	
	public ILogicalOperator getEndOperator() {
		return toOperator;
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
		return conn.fromNode.equals(fromNode) && conn.toNode.equals(toNode) && 
				( ( conn.fromOperator.equals(fromOperator) && conn.toOperator.equals(toOperator) ) ||
				  ( conn.fromOperator.equals(toOperator) && conn.toOperator.equals(fromOperator) ) );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QueryPartGraphConnection[");
		sb.append(fromNode.getQueryPart()).append("<").append(fromOperator).append(">");
		sb.append(" to ");
		sb.append(toNode.getQueryPart()).append("<").append(toOperator).append(">");
		sb.append("]");
		return sb.toString();
	}
}
