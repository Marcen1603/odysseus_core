package de.uniol.inf.is.odysseus.p2p.distribution.client.receiver;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IReceiverStrategy {
	public boolean handleQuery(Query query);
}
