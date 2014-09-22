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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryPartGraphConnection other = (QueryPartGraphConnection) obj;
		if (fromNode == null) {
			if (other.fromNode != null)
				return false;
		} else if (!fromNode.equals(other.fromNode))
			return false;
		if (fromOperator == null) {
			if (other.fromOperator != null)
				return false;
		} else if (!fromOperator.equals(other.fromOperator))
			return false;
		if (toNode == null) {
			if (other.toNode != null)
				return false;
		} else if (!toNode.equals(other.toNode))
			return false;
		if (toOperator == null) {
			if (other.toOperator != null)
				return false;
		} else if (!toOperator.equals(other.toOperator))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromNode == null) ? 0 : fromNode.hashCode());
		result = prime * result
				+ ((fromOperator == null) ? 0 : fromOperator.hashCode());
		result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
		result = prime * result
				+ ((toOperator == null) ? 0 : toOperator.hashCode());
		return result;
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
