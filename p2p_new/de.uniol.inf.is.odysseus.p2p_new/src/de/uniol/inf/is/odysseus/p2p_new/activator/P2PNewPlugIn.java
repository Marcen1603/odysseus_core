package de.uniol.inf.is.odysseus.p2p_new.activator;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		registerAdvertisementTypes();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
	}

}
