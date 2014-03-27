package de.uniol.inf.is.odysseus.peer.logging.adv;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory.Instantiator;
import net.jxta.document.Element;

public class LoggingAdvertisementInstatiator implements Instantiator {

	@Override
	public String getAdvertisementType() {
		return LoggingAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new LoggingAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new LoggingAdvertisement(root);
	}

}
