package de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IQuerySelectionStrategy {
	public boolean handleQuery(Query query, IPeer peer);
}
