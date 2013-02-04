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
	
	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);
	
	private NetworkManager manager;
	
	public void start(BundleContext bundleContext) throws Exception {
		loadOwnLogProperties(bundleContext.getBundle());
		
		manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, "Odysseus JXTA");
		PeerGroup peerGroup = manager.startNetwork();
		
		LOG.debug("JXTA-Network started. Peer is in group '{}'", peerGroup);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		manager.stopNetwork();
		LOG.debug("JXTA-Network stopped");
	}

	private static void loadOwnLogProperties(Bundle bundle) {
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}

}
