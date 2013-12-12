package de.uniol.inf.is.odysseus.peer.resource.impl;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class ResourceUsageAdvertisementInstantiator implements AdvertisementFactory.Instantiator {
	
	@Override
	public String getAdvertisementType() {
		return ResourceUsageAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new ResourceUsageAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new ResourceUsageAdvertisement(root);
	}
}