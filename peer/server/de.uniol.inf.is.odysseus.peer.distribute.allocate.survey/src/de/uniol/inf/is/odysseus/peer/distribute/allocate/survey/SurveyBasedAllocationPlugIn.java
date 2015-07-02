package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisementInstanciator;

public class SurveyBasedAllocationPlugIn implements BundleActivator {

	public static final String DEFAULT_BID_PROVIDER_NAME = "costmodel";
	
	private static final String BID_PROVIDER_NAME = "peer.distribute.bidprovider";
	
	private static String selectedBidProviderName;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(AuctionQueryAdvertisement.getAdvertisementType(), new AuctionQueryAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(AuctionResponseAdvertisement.getAdvertisementType(), new AuctionResponseAdvertisementInstanciator());
		
		selectedBidProviderName = determineSelectedBidProviderName();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
	
	public static String getSelectedBidProviderName() {
		return selectedBidProviderName;
	}
	
	private static String determineSelectedBidProviderName() {
		String bidProviderName = System.getProperty(BID_PROVIDER_NAME);
		if (!Strings.isNullOrEmpty(bidProviderName)) {
			return bidProviderName;
		}

		Optional<String> optBidProviderName = PeerConfiguration.get(BID_PROVIDER_NAME);
		if (optBidProviderName.isPresent()) {
			return optBidProviderName.get();
		}

		return DEFAULT_BID_PROVIDER_NAME;
	}
}
