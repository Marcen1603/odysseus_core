package de.uniol.inf.is.odysseus.p2p_new.activator;

import net.jxta.document.AdvertisementFactory;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.communication.CommunicationAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.communication.CommunicationAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);
	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.ALL;
	
	public static final int TRANSPORT_BUFFER_SIZE = 640*480*3+(1+4);

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		configureLogging(bundleContext.getBundle());
		
		registerAdvertisementTypes();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
		AdvertisementFactory.registerAdvertisementInstance(CommunicationAdvertisement.getAdvertisementType(), new CommunicationAdvertisementInstantiator());
	}
	
	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		if (jxtaLogger != null) {
			jxtaLogger.setLevel(JXTA_LOG_LEVEL);
		} else {
			LOG.warn("Could not get JXTA-Logger for setting level to {}", JXTA_LOG_LEVEL);
		}
		
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}

}
