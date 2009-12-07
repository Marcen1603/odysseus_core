package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IDistributionProvider<T extends IPeer> {
	public void initializeService();
	public void startService();
	public String getDistributionStrategy();
	public void setPeer(T peer);
	void distributePlan(Query query, Object serverResponse);
}
