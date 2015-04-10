package de.uniol.inf.is.odysseus.peer.network;

import net.jxta.document.Advertisement;

public interface IAdvertisementDiscovererListener {

	public void advertisementDiscovered( Advertisement advertisement );
	public void updateAdvertisements();
	
}
