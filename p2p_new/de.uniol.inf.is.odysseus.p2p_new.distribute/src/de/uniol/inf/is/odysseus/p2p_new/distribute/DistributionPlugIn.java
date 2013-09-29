package de.uniol.inf.is.odysseus.p2p_new.distribute;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisementInstantiator;

/**
 * The activator for the bundle "Distribution".
 * @author Michael Brand
 */
public class DistributionPlugIn implements BundleActivator {

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
