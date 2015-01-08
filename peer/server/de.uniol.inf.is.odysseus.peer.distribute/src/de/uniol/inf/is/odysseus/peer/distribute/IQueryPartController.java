package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public interface IQueryPartController {

	public ID getSharedQueryID(int queryId);

	public Collection<Integer> getLocalIds(ID sharedQueryId);

	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID);

	public void registerAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers);
	
	public void unregisterAsMaster(ID sharedQueryId);
	
	public boolean isMasterForQuery(int queryID);
	
	public Collection<PeerID> getOtherPeers(ID sharedQueryId);
	
}