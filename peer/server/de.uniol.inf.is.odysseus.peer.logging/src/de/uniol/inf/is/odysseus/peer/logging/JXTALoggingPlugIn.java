package de.uniol.inf.is.odysseus.peer.logging;

import net.jxta.document.AdvertisementFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;
import de.uniol.inf.is.odysseus.peer.jxta.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisement;
import de.uniol.inf.is.odysseus.peer.logging.adv.LoggingAdvertisementInstatiator;
import de.uniol.inf.is.odysseus.peer.logging.impl.LogAdvertisementListener;
import de.uniol.inf.is.odysseus.peer.logging.impl.LogMessage;
import de.uniol.inf.is.odysseus.peer.logging.impl.LogMessageReceiver;
import de.uniol.inf.is.odysseus.peer.logging.impl.P2PNetworkManagerListener;
import de.uniol.inf.is.odysseus.peer.network.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class JXTALoggingPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(JXTALoggingPlugIn.class);
	private static final P2PNetworkManagerListener P2P_NETWORKMANAGER_LISTENER = new P2PNetworkManagerListener();
	private static final LogMessageReceiver LOGMESSAGE_RECEIVER = new LogMessageReceiver();
	private static final String LOGACTIVE_SYS_PROPERTY = "peer.log";
	private static final IAdvertisementDiscovererListener LOGADVERTISEMENT_DISCOVERER_LISTENER = new LogAdvertisementListener();

	private static IPeerCommunicator peerCommunicator;
	private static IPeerDictionary peerDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IP2PNetworkManager p2pNetworkManager;

	
	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(LogMessage.class);
		peerCommunicator.addListener(LOGMESSAGE_RECEIVER, LogMessage.class);
		
		LOG.debug("Bound PeerCommunicator {}", serv);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.unregisterMessageType(LogMessage.class);
			peerCommunicator.removeListener(LOGMESSAGE_RECEIVER, LogMessage.class);
			peerCommunicator = null;
			
			LOG.debug("Unbound PeerCommunicator {}", serv);
		}
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
		
		LOG.debug("Bound PeerDictionary {}", serv);
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
			
			LOG.debug("Unbound PeerDictionary {}", serv);
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
		p2pNetworkManager.addAdvertisementListener(LOGADVERTISEMENT_DISCOVERER_LISTENER);
		LOG.debug("Bound P2PNetworkManager {}", serv);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager.removeAdvertisementListener(LOGADVERTISEMENT_DISCOVERER_LISTENER);
			p2pNetworkManager.removeListener(P2P_NETWORKMANAGER_LISTENER);
			p2pNetworkManager = null;
			LOG.debug("Unbound P2PNetworkManager {}", serv);
		}
	}
	
	public static IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}
	
	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
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
	
	public static boolean isLogging() {
		try {
			String isLoggingString = System.getProperty(LOGACTIVE_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(isLoggingString)) {
				return Boolean.valueOf(isLoggingString);
			}

			Optional<String> optIsLogging = PeerConfiguration.get(LOGACTIVE_SYS_PROPERTY);
			if (optIsLogging.isPresent()) {
				return Boolean.valueOf(optIsLogging.get());
			}
		} catch (Throwable t) {
			LOG.error("Could not determine if this peer is logging", t);
		}
		return false;
	}
}
