package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory.Instantiator;
import net.jxta.document.Element;

public class SmartDeviceAdvertisementInstantiator implements Instantiator {
	private static final Logger LOG = LoggerFactory.getLogger(SmartDeviceAdvertisementInstantiator.class);
	
	@Override
	public String getAdvertisementType() {
		LOG.debug("getAdvertisementType");
		return SmartDeviceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		LOG.debug("newInstance");
		return new SmartDeviceAdvertisement();
	}

	@Override
	public Advertisement newInstance(Element<?> root) {
		LOG.debug("newInstance(Element<?> root)");
		return new SmartDeviceAdvertisement(root);
	}

}
