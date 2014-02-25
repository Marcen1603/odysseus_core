package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class RemoveSourceAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return RemoveSourceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new RemoveSourceAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new RemoveSourceAdvertisement(root);
	}
}
