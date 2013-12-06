package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisementInstanciator;

public class SurveyBasedAllocationPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(AuctionQueryAdvertisement.getAdvertisementType(), new AuctionQueryAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(AuctionResponseAdvertisement.getAdvertisementType(), new AuctionResponseAdvertisementInstanciator());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}
