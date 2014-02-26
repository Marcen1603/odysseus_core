package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class MultipleSourceAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return MultipleSourceAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new MultipleSourceAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new MultipleSourceAdvertisement(root);
	}
}
