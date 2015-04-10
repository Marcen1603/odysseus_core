package de.uniol.inf.is.odysseus.p2p_new.activator;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveMultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.RemoveSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.MultipleSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.RemoveMultipleSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.RemoveSourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);

	private static Bundle bundle;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		registerAdvertisementTypes();
		
		bundle = bundleContext.getBundle();
				
		PeerBundlesStatusChecker checker = new PeerBundlesStatusChecker(bundleContext);
		checker.start(); // fire-and-forget
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		bundle = null;
	}
	
	public static Bundle getBundle() {
		return bundle;
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(RemoveSourceAdvertisement.getAdvertisementType(), new RemoveSourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(MultipleSourceAdvertisement.getAdvertisementType(), new MultipleSourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(RemoveMultipleSourceAdvertisement.getAdvertisementType(), new RemoveMultipleSourceAdvertisementInstantiator());
	}
}
