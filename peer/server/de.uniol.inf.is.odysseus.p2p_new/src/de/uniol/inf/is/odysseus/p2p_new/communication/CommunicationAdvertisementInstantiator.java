package de.uniol.inf.is.odysseus.p2p_new.communication;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class CommunicationAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return CommunicationAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new CommunicationAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new CommunicationAdvertisement(root);
	}
}
