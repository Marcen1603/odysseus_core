package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

/**
 * Instantiator Class to create DDCAdvertisements. Needed for JXTA
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerAdvertisementInstantiator implements
		AdvertisementFactory.Instantiator {

	@Override
	public String getAdvertisementType() {
		return DistributedDataContainerAdvertisement.getAdvertisementType();
	}

	@Override
	public Advertisement newInstance() {
//		System.out.println("newInstance()");
		return new DistributedDataContainerAdvertisement();
	}

	@Override
	public Advertisement newInstance(Element<?> root) {
//		System.out.println("newInstance(Element)");
		return new DistributedDataContainerAdvertisement(root);
	}

}
