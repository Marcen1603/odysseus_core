package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public interface IQueryPartController {

	public ID getSharedQueryID(int queryId);

	public Collection<Integer> getLocalIds(ID sharedQueryId);

	public void registerAsSlave(Collection<Integer> ids, ID sharedQueryID, PeerID masterPeer);
	
	public void registerAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers);
	
	public void unregisterAsMaster(ID sharedQueryId);
	
	public boolean isMasterForQuery(int queryID);
	
	public boolean isSharedQueryKnown(ID sharedQueryID);
	
	public PeerID getMasterForQuery(ID sharedQueryID);
	
	public Collection<PeerID> getOtherPeers(ID sharedQueryId);
	
	public PeerID addOtherPeer(ID sharedQueryId, PeerID peerID);
	
	public void removeOtherPeer(ID sharedQueryId, PeerID peerID);
	
	public void unregisterLocalQueriesFromSharedQuery(ID sharedQueryID, Collection<Integer> toRemove);

	void addLocalQueryToShared(ID sharedQueryID, int localQueryID);
	
}