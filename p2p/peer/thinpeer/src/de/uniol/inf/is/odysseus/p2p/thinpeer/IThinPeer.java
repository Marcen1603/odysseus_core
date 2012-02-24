package de.uniol.inf.is.odysseus.p2p.thinpeer;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;

public interface IThinPeer {

	public void publishQuerySpezification(String query,
			String language, ISession user);

	public void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, ISession user);

	public void registerListener(IThinPeerListener thinPeerListener);
	public void removeListener(IThinPeerListener thinPeerListener);

	public void addToDD(ISourceAdvertisement adv, IDataDictionary dd, ISession caller);
	

}
