package de.uniol.inf.is.odysseus.peer.distribute;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.adv.QueryPartAdvertisementInstantiator;

public class PeerDistributePlugIn implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(QueryPartAdvertisement.getAdvertisementType(), new QueryPartAdvertisementInstantiator());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
