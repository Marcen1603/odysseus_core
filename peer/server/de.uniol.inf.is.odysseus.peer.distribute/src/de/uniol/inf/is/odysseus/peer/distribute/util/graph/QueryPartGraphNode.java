package de.uniol.inf.is.odysseus.peer.distribute.util.graph;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;

public class QueryPartGraphNode {

	private final ILogicalQueryPart queryPart;
	
	private final Collection<QueryPartGraphConnection> connections = Lists.newLinkedList();
	
	QueryPartGraphNode( ILogicalQueryPart queryPart) {
		Preconditions.checkNotNull(queryPart, "Query Part for queryPartGraph must not be null!");
		
		this.queryPart = queryPart;
	}
	
	public ILogicalQueryPart getQueryPart() {
		return queryPart;
	}
	
	void addConnection(QueryPartGraphConnection connection ) {
		if( !connections.contains(connection)) {
			connections.add(connection);
		}
	}
	
	public Collection<QueryPartGraphConnection> getConnections() {
		return ImmutableList.copyOf(connections);
	}
	
	public Collection<QueryPartGraphConnection> getConnectionsAsStart() {
		Collection<QueryPartGraphConnection> startConnections = Lists.newLinkedList();
		for( QueryPartGraphConnection connection : connections ) {
			if( connection.getStartNode().equals(this)) {
				startConnections.add(connection);
			}
		}
		return startConnections;
	}
	
	public Collection<QueryPartGraphConnection> getConnectionsAsEnd() {
		Collection<QueryPartGraphConnection> endConnections = Lists.newLinkedList();
		for( QueryPartGraphConnection connection : connections ) {
			if( connection.getEndNode().equals(this)) {
				endConnections.add(connection);
			}
		}
		return endConnections;
	}
	
	@Override
	public boolean equals(Object other) {
		if( !(other instanceof QueryPartGraphNode)) {
			return false;
		}
		if( other == this ) {
			return true;
		}
		
		QueryPartGraphNode node = (QueryPartGraphNode)other;
		return queryPart.equals(node.queryPart);
	}
	
	@Override
	public int hashCode() {
		return queryPart.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QueryPartGraphNode[").append(queryPart);
		
		Collection<QueryPartGraphConnection> connectionsAsStart = getConnectionsAsStart();
		if( !connectionsAsStart.isEmpty() ) {
			sb.append("Connected to ");
			for( QueryPartGraphConnection connection : connectionsAsStart) {
				sb.append(connection.getEndNode().getQueryPart());
			}
		} else {
			sb.append("Not connected");
		}
		sb.append("]");
		return sb.toString();
	}
}
