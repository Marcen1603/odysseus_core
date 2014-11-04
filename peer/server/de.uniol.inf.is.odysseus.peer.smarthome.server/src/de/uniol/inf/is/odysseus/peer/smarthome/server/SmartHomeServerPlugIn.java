package de.uniol.inf.is.odysseus.peer.smarthome.server;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceResponseMessage;

public class SmartHomeServerPlugIn implements BundleActivator {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	private static Bundle bundle;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IPQLGenerator pqlGenerator;

	public void start(BundleContext bundleContext) throws Exception {
		SmartDeviceServerDictionaryDiscovery.getInstance().addListener(
				Logic.getInstance());
		SmartDeviceServerDictionaryDiscovery.getInstance().addListener(
				QueryExecutor.getInstance());
		

		bundle = bundleContext.getBundle();
	}

	public void stop(BundleContext context) throws Exception {
		SmartDeviceServerDictionaryDiscovery.getInstance().removeListener(
				Logic.getInstance());
		SmartDeviceServerDictionaryDiscovery.getInstance().removeListener(
				QueryExecutor.getInstance());
		bundle = null;
	}

	// called by OSGi-DS
	public void activate() {
	}

	// called by OSGi-DS
	public void deactivate() {
	}

	public static Bundle getBundle() {
		return bundle;
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;

		SmartDeviceServer.getInstance().bindP2PNetworkManager(serv);
		SmartDeviceServerDictionaryDiscovery.getInstance()
				.bindP2PNetworkManager(serv);
		Logic.getInstance()
				.bindP2PNetworkManager(serv);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		SmartDeviceServer.getInstance().unbindP2PNetworkManager(serv);
		SmartDeviceServerDictionaryDiscovery.getInstance()
				.unbindP2PNetworkManager(serv);
		Logic.getInstance()
				.unbindP2PNetworkManager(serv);

		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		SmartDeviceServerDictionaryDiscovery.getInstance()
				.bindPeerCommunicator(serv);
		SmartDeviceLocalConfigurationServer.getInstance().bindPeerCommunicator(
				serv);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		SmartDeviceLocalConfigurationServer.getInstance()
				.unbindPeerCommunicator(serv);
		SmartDeviceServerDictionaryDiscovery.getInstance()
				.unbindPeerCommunicator(serv);

		if (peerCommunicator == serv) {
			peerCommunicator
					.unregisterMessageType(SmartDeviceRequestMessage.class);
			peerCommunicator
					.unregisterMessageType(SmartDeviceResponseMessage.class);

			peerCommunicator
					.unregisterMessageType(SmartDeviceConfigurationResponseMessage.class);
			peerCommunicator
					.unregisterMessageType(SmartDeviceConfigurationRequestMessage.class);

			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;

		SmartDeviceServer.getInstance().bindP2PDictionary(serv);
		SmartDeviceServerDictionaryDiscovery.getInstance().bindP2PDictionary(
				serv);
		QueryExecutor.getInstance().bindP2PDictionary(serv);
		// showActualImportedSourcesAsync();
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		SmartDeviceServerDictionaryDiscovery.getInstance().unbindP2PDictionary(
				serv);
		SmartDeviceServer.getInstance().unbindP2PDictionary(serv);
		QueryExecutor.getInstance().unbindP2PDictionary(serv);
		
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;

		SmartDeviceServer.getInstance().bindJxtaServicesProvider(serv);
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		SmartDeviceServer.getInstance().unbindJxtaServicesProvider(serv);

		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;

		SmartDeviceServerDictionaryDiscovery.getInstance().bindPQLGenerator(
				serv);
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		SmartDeviceServerDictionaryDiscovery.getInstance().unbindPQLGenerator(
				serv);

		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}

	public static IPQLGenerator getPQLGenerator() {
		return pqlGenerator;
	}

	public static IJxtaServicesProvider getJxtaServicesProvider() {
		return jxtaServicesProvider;
	}

	public static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

	public static IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}

	public static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}

}
