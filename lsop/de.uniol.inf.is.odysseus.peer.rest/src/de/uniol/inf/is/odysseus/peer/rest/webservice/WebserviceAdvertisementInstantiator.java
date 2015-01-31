package de.uniol.inf.is.odysseus.peer.rest.webservice;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;



/**
 * Instantiator for WebserviceAdvertisement
 * @author Thore Stratmann
 *
 */
public class WebserviceAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return WebserviceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new WebserviceAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new WebserviceAdvertisement(root);
	}
}

