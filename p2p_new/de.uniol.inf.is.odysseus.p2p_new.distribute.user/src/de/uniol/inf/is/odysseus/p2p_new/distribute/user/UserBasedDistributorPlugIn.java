package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class UserBasedDistributorPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(QueryPartAdvertisement.getAdvertisementType(), new QueryPartAdvertisementInstantiator());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
