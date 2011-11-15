package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener;

import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;

public interface ISourceDiscovererListener {
	public void foundNewSource(SourceAdvertisement adv);
}
