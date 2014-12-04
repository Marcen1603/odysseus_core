package de.uniol.inf.is.odysseus.peer.recovery.advertisement;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;


public class RecoveryStrategyManagerAdvertisementInstantiator implements AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return RecoveryStrategyManagerAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new RecoveryStrategyManagerAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new RecoveryStrategyManagerAdvertisement(root);
	}
}

