package de.uniol.inf.is.odysseus.p2p.thinpeer;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;

public interface IThinPeerListener {

	void foundAdminPeer(IExtendedPeerAdvertisement peer);
	void foundSource(ISourceAdvertisement adv, ISession caller);

}
