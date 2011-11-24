package de.uniol.inf.is.odysseus.p2p.thinpeer;

import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IThinPeer {

	public void publishQuerySpezification(String query,
			String language, User user);

	public void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, User user);

	public void registerListener(IThinPeerListener thinPeerListener);
	public void removeListener(IThinPeerListener thinPeerListener);

	public void addToDD(ISourceAdvertisement adv);
	

}
