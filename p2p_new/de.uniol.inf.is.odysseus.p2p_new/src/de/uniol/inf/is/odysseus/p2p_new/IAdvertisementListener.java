package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.document.Advertisement;

public interface IAdvertisementListener {

	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv);
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv);

}
