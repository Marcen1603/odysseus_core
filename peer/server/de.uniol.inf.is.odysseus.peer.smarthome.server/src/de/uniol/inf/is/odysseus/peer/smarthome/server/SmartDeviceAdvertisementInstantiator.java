package de.uniol.inf.is.odysseus.peer.smarthome.server;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory.Instantiator;
import net.jxta.document.Element;

public class SmartDeviceAdvertisementInstantiator implements Instantiator {

	@Override
	public String getAdvertisementType() {
		return SmartDeviceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new SmartDeviceAdvertisement();
	}

	@Override
	public Advertisement newInstance(Element<?> root) {
		return new SmartDeviceAdvertisement(root);
	}

}
