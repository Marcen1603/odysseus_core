package de.uniol.inf.is.odysseus.p2p_new.lb;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartAdvertisementInstantiator;

/**
 * The activator for the bundle "LoadBalancer".
 * @author Michael Brand
 */
public class LoadBalancerPlugIn implements BundleActivator {

	/**
	 * Registers a new {@link QueryPartAdvertisement}.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		
		AdvertisementFactory.registerAdvertisementInstance(QueryPartAdvertisement.getAdvertisementType(), new QueryPartAdvertisementInstantiator());

	}

	/**
	 * Nothing to do.
	 */
	@Override
	public void stop(BundleContext context) throws Exception {}

}