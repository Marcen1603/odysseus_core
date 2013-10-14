package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;


import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisementInstantiator;

public class CentralizedDistributorPlugin implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(PhysicalQueryPartAdvertisement.getAdvertisementType(), new PhysicalQueryPartAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(MasterNotificationAdvertisement.getAdvertisementType(), new MasterNotificationAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(PhysicalQueryPlanAdvertisement.getAdvertisementType(), new PhysicalQueryPlanAdvertisementInstantiator());
		// activate the advertisementmanager in order to find the other peers, send the the current plan to the master etc.
		CentralizedDistributorAdvertisementManager.getInstance().activate();
		System.out.println("The Centralized Distributor has been activated");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
