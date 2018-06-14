package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IQueryPartController {

	public UUID getSharedQueryID(int queryId);
	
	public Collection<Integer> getLocalIds(UUID sharedQueryId);
	public Collection<UUID> getSharedQueryIds();
	
	public void registerAsSlave(Collection<Integer> ids, UUID sharedQueryID, IOdysseusNode masterNodes);
	public void registerAsMaster(ILogicalQuery query, int queryID, final UUID sharedQueryID, Collection<IOdysseusNode> otherNodes);
	
	public void unregisterAsMaster(UUID sharedQueryId);
	public void unregisterLocalQueriesFromSharedQuery(UUID sharedQueryID, Collection<Integer> toRemove);
	
	public boolean isMasterForQuery(int queryID);
	public boolean isSharedQueryKnown(UUID sharedQueryID);
	
	public IOdysseusNode getMasterForQuery(UUID sharedQueryID);
	public Collection<IOdysseusNode> getOtherNodes(UUID sharedQueryId);
	
	public IOdysseusNode addOtherNode(UUID sharedQueryId, IOdysseusNode node);
	public void removeOtherNode(UUID sharedQueryId, IOdysseusNode node);
	
	public void addLocalQueryToShared(UUID sharedQueryID, int localQueryID);
	
}