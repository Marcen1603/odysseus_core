package de.uniol.inf.is.odysseus.peer.rest;

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

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		registerAdvertisementTypes();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	private void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(WebserviceAdvertisement.getAdvertisementType(), new WebserviceAdvertisementInstantiator());
	}

}
