package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.document.Advertisement;

public interface IAdvertisementListener {

	public boolean isSelected( Advertisement advertisement );
	public void advertisementOccured( IAdvertisementManager sender, Advertisement adv );
	
}
