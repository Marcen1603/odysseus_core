package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisementInstanciator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.CostResponseAdvertisementInstantiator;

public class AuctionBasedDistributorPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(AuctionQueryAdvertisement.getAdvertisementType(),
				new AuctionQueryAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(AuctionResponseAdvertisement.getAdvertisementType(),
				new AuctionResponseAdvertisementInstanciator());
		AdvertisementFactory.registerAdvertisementInstance(CostQueryAdvertisement.getAdvertisementType(),
				new CostQueryAdvertisementInstantiator());		
		AdvertisementFactory.registerAdvertisementInstance(CostResponseAdvertisement.getAdvertisementType(),
				new CostResponseAdvertisementInstantiator());					
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
