package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import net.jxta.document.Advertisement;
import net.jxta.document.Element;
import net.jxta.document.AdvertisementFactory.Instantiator;

public class PhysicalQueryPartAdvertisementInstantiator implements Instantiator {

	@Override
	public String getAdvertisementType() {
		return PhysicalQueryPartAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new PhysicalQueryPartAdvertisement();
	}

	/**
	 * {@inheritDoc}}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new PhysicalQueryPartAdvertisement(root);
	}

}
