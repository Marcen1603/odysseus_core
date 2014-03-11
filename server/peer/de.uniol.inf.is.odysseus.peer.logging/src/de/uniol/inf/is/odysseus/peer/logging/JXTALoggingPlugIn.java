package de.uniol.inf.is.odysseus.peer.logging;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class JXTALoggingPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(JXTALoggingPlugIn.class);
	private static final P2PNetworkManagerListener P2P_NETWORKMANAGER_LISTENER = new P2PNetworkManagerListener();
	private static final AdvertisementManagerListener ADVERTISEMENT_MANAGER_LISTENER = new AdvertisementManagerListener();
	private static final LogMessageReceiver LOGMESSAGE_RECEIVER = new LogMessageReceiver();
	private static final P2PDictionaryListener P2PDICTIONARY_LISTENER = new P2PDictionaryListener();
	
	public static final byte LOG_BYTE = 111;
	
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IAdvertisementManager advertisementManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PNetworkManager p2pNetworkManager;

	
	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		peerCommunicator.addListener(LOGMESSAGE_RECEIVER);
		
		LOG.debug("Bound PeerCommunicator {}", serv);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(LOGMESSAGE_RECEIVER);
			peerCommunicator = null;
			
			LOG.debug("Unbound PeerCommunicator {}", serv);
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
		p2pDictionary.addListener(P2PDICTIONARY_LISTENER);
		
		LOG.debug("Bound P2PDictionary {}", serv);
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary.removeListener(P2PDICTIONARY_LISTENER);
			p2pDictionary = null;
			
			LOG.debug("Unbound P2PDictionary {}", serv);
		}
	}

	// called by OSGi-DS
	public static void bindAdvertisementManager(IAdvertisementManager serv) {
		advertisementManager = serv;
		advertisementManager.addAdvertisementListener(ADVERTISEMENT_MANAGER_LISTENER);
		
		LOG.debug("Bound Advertisement Manager {}", serv);
	}

	// called by OSGi-DS
	public static void unbindAdvertisementManager(IAdvertisementManager serv) {
		if (advertisementManager == serv) {
			advertisementManager.removeAdvertisementListener(ADVERTISEMENT_MANAGER_LISTENER);
			advertisementManager = null;
			LOG.debug("Unbound Advertisement Manager {}", serv);
		}
	}
	
	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;

		LOG.debug("Bound JxtaServicesProvider Manager {}", serv);
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
			LOG.debug("Unbound JxtaServicesProvider Manager {}", serv);
		}
	}
	
	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addListener(P2P_NETWORKMANAGER_LISTENER);
		LOG.debug("Bound P2PNetworkManager {}", serv);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager.removeListener(P2P_NETWORKMANAGER_LISTENER);
			p2pNetworkManager = null;
			LOG.debug("Unbound P2PNetworkManager {}", serv);
		}
	}
	
	public static IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}
	
	public static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}
	
	public static IAdvertisementManager getAdvertisementManager() {
		return advertisementManager;
	}
	
	public static IJxtaServicesProvider getJxtaServicesProvider() {
		return jxtaServicesProvider;
	}
	
	public static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdvertisementFactory.registerAdvertisementInstance(LoggingAdvertisement.getAdvertisementType(), new LoggingAdvertisementInstatiator());
		LOG.debug("Registered LoggingAdvertisemet in Factory");
		
		LOG.debug("PlugIn Started");
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("PlugIn Stopped");
	}
}
