package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

/**
 * Instantiator Class to create DDCRequestAdvertisements. Needed for JXTA
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerRequestAdvertisementInstantiator implements
		AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return DistributedDataContainerRequestAdvertisement
				.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
		return new DistributedDataContainerRequestAdvertisement();
	}

	@Override
	public Advertisement newInstance(Element<?> root) {
		return new DistributedDataContainerRequestAdvertisement(root);
	}

}