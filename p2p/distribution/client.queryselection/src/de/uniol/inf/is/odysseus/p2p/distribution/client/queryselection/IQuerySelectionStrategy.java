package de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection;

import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public interface IQuerySelectionStrategy {
	public boolean handleQuery(Subplan subplan, IOdysseusPeer peer);
}
