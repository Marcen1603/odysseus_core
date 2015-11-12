package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IQueryPartControllerListener {

	public void afterRegisterAsSlave(Collection<Integer> ids, UUID sharedQueryID, IOdysseusNode masterNode);

	public void afterRegisterAsMaster(ILogicalQuery query, int queryID, final UUID sharedQueryID, Collection<IOdysseusNode> otherNodes);
	
	public void afterUnregisterAsMaster(UUID sharedQueryID);
	
	public void afterUnregisterAsSlave(UUID sharedQueryID, Collection<Integer> ids);
	
}