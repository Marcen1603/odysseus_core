package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class RemoveMultipleSourceAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return RemoveMultipleSourceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new RemoveMultipleSourceAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new RemoveMultipleSourceAdvertisement(root);
	}
}
