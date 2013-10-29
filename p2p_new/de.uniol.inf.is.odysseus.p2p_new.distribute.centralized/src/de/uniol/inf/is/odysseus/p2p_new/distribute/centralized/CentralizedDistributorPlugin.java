package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;


import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.IdenticalQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.IdenticalQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.MasterNotificationAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPartAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.ResourceUsageUpdateAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.ResourceUsageUpdateAdvertisementInstantiator;

public class CentralizedDistributorPlugin implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(PhysicalQueryPartAdvertisement.getAdvertisementType(), new PhysicalQueryPartAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(MasterNotificationAdvertisement.getAdvertisementType(), new MasterNotificationAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(PhysicalQueryPlanAdvertisement.getAdvertisementType(), new PhysicalQueryPlanAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(ResourceUsageUpdateAdvertisement.getAdvertisementType(), new ResourceUsageUpdateAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(IdenticalQueryAdvertisement.getAdvertisementType(), new IdenticalQueryAdvertisementInstantiator());
		// activate the advertisementmanager in order to find the other peers, send the the current plan to the master etc.
		CentralizedDistributorAdvertisementManager.getInstance().activate();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
