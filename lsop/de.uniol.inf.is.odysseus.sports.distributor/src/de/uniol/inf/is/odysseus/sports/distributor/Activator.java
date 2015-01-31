package de.uniol.inf.is.odysseus.sports.distributor;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.rest.webservice.WebserviceAdvertisementInstantiator;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		registerAdvertisementTypes();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	private void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(WebserviceAdvertisement.getAdvertisementType(), new WebserviceAdvertisementInstantiator());
	}

}
