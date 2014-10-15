package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;

import net.jxta.id.ID;

public interface IQueryPartController {

	public ID getSharedQueryID(int queryId);
	
	public Collection<Integer> getLocalIds(ID sharedQueryId);

}