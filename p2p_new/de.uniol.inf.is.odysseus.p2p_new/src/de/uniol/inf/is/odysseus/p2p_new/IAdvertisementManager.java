package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.document.Advertisement;

public interface IAdvertisementManager {

	public void addAdvertisementListener( IAdvertisementListener listener );
	public void addAdvertisementListener( IAdvertisementListener listener, Class<? extends Advertisement> listenFor);
	public void addAdvertisementListener( IAdvertisementListener listener, IAdvertisementSelector selector);
	
	public void removeAdvertisementListener( IAdvertisementListener listener );
}
