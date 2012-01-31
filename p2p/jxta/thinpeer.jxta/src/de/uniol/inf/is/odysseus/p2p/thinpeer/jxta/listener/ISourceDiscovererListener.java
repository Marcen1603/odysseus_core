package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener;

import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public interface ISourceDiscovererListener {
	public void foundNewSource(SourceAdvertisement adv, ISession caller);
}
