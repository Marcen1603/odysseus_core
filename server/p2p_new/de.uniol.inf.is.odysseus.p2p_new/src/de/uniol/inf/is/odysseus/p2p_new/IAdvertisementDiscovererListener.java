package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.document.Advertisement;

public interface IAdvertisementDiscovererListener {

	public void advertisementDiscovered( Advertisement advertisement );
	public void updateAdvertisements();
	
}
