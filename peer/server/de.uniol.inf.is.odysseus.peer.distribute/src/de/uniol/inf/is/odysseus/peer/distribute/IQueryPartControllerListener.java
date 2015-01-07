package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public interface IQueryPartControllerListener {

	public void afterRegisterAsSlave(Collection<Integer> ids, ID sharedQueryID);

	public void afterRegisterAsMaster(ILogicalQuery query, int queryID, final ID sharedQueryID, Collection<PeerID> otherPeers);
	
}