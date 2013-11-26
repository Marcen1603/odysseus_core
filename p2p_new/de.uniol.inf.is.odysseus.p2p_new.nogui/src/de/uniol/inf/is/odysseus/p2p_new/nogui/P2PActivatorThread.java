package de.uniol.inf.is.odysseus.p2p_new.nogui;

import net.jxta.platform.NetworkManager;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;

public class P2PActivatorThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(P2PActivatorThread.class);
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.SEVERE;

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";
	
	private static final String DEFAULT_PEER_NAME = "OdysseusPeer";
	private static final String DEFAULT_PEER_GROUP_NAME = "OdysseusPeerGroup";
	
	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";

	private final BundleContext bundleContext;
	
	private NetworkManager manager;
	
	public P2PActivatorThread( BundleContext bundleContext ) {
		Preconditions.checkNotNull(bundleContext, "Bundle context must not be null!");
		
		this.bundleContext = bundleContext;
		
		setName("P2P Activator Thread");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		configureLogging(bundleContext.getBundle());

		waitForP2PNetworkManager();
		
		String ownPeerName = determinePeerName();
		String ownPeerGroupName = determinePeerGroupName();
		
		P2PNetworkManagerService.get().setLocalPeerName(ownPeerName);
		P2PNetworkManagerService.get().setLocalPeerGroupName(ownPeerGroupName);
		
		try {
			P2PNetworkManagerService.get().start();
		} catch (P2PNetworkException ex) {
			LOG.error("Could not start p2p network automatically", ex);
		}
	}
	
	private static void waitForP2PNetworkManager() {
		while( !P2PNetworkManagerService.isBound() ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}
	}

	private static String determinePeerName() {
		String peerName = System.getProperty(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return peerName;
		}

		peerName = OdysseusConfiguration.get(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return peerName;
		}

		return DEFAULT_PEER_NAME + "_" + System.getProperty("user.name");
	}
	
	private static String determinePeerGroupName() {
		String peerGroupName = System.getProperty(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		peerGroupName = OdysseusConfiguration.get(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		return DEFAULT_PEER_GROUP_NAME + "_" + System.getProperty("user.name");
	}
	
	public NetworkManager getNetworkManager() {
		return manager;
	}
	
	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		if (jxtaLogger != null) {
			jxtaLogger.setLevel(JXTA_LOG_LEVEL);
		} else {
			LOG.warn("Could not get JXTA-Logger for setting level to {}", JXTA_LOG_LEVEL);
		}
		jxtaLogger = java.util.logging.Logger.getLogger("net.jxta.impl.endpoint.router.EndpointRouter");
		jxtaLogger.setLevel(java.util.logging.Level.OFF);
		
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}
}
