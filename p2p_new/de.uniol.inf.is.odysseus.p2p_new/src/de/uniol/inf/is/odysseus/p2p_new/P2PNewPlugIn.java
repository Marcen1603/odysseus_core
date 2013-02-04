package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P2PNewPlugIn implements BundleActivator {

	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.WARNING;
	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);
	
	private NetworkManager manager;
	
	public void start(BundleContext bundleContext) throws Exception {
		configureLogging(bundleContext.getBundle());
		
		manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "Odysseus JXTA");
		PeerGroup peerGroup = manager.startNetwork();
		
		LOG.debug("JXTA-Network started. Peer is in group '{}'", peerGroup);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		manager.stopNetwork();
		LOG.debug("JXTA-Network stopped");
	}

	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		if(jxtaLogger != null ) {
			jxtaLogger.setLevel(JXTA_LOG_LEVEL);
		} else {
			LOG.warn("Could not get JXTA-Logger for setting level to {}", JXTA_LOG_LEVEL);
		}
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
		
	}

}
