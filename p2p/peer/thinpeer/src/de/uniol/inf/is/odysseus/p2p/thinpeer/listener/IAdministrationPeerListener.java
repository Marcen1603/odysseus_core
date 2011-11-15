package de.uniol.inf.is.odysseus.p2p.thinpeer.listener;

import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;

public interface IAdministrationPeerListener {
	void foundAdminPeers(IExtendedPeerAdvertisement adv);

}
