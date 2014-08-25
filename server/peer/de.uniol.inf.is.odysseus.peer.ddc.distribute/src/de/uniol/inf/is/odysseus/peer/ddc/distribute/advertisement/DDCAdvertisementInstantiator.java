package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public class DDCAdvertisementInstantiator implements AdvertisementFactory.Instantiator{

	@Override
	public String getAdvertisementType() {
		return DDCAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new DDCAdvertisement();
	}

	@Override
	public Advertisement newInstance(Element<?> root) {
		return new DDCAdvertisement(root);
	}

}
