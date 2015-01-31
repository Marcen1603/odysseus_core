package de.uniol.inf.is.odysseus.peer.ddc.distribute;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerRequestAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerRequestAdvertisementInstantiator;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(
				DistributedDataContainerAdvertisement.getAdvertisementType(),
				new DistributedDataContainerAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(
				DistributedDataContainerChangeAdvertisement.getAdvertisementType(),
				new DistributedDataContainerChangeAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(
				DistributedDataContainerRequestAdvertisement.getAdvertisementType(),
				new DistributedDataContainerRequestAdvertisementInstantiator());
		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
