package de.uniol.inf.is.odysseus.peer.recovery.advertisement;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;


public class RecoveryAllocatorAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return RecoveryAllocatorAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new RecoveryAllocatorAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new RecoveryAllocatorAdvertisement(root);
	}
}

