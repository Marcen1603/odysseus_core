package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.LogicRuleProcessor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.QueryExecutor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceLocalConfigurationServer;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceDictionaryDiscovery;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.config.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceResponseMessage;

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

	private static IPeerDictionary peerDictionary;

	public void start(BundleContext bundleContext) throws Exception {
		SmartDeviceDictionaryDiscovery.getInstance().addListener(LogicRuleProcessor.getInstance());

		bundle = bundleContext.getBundle();
	}

	public void stop(BundleContext context) throws Exception {
		SmartDeviceDictionaryDiscovery.getInstance().removeListener(LogicRuleProcessor.getInstance());

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

		p2pNetworkManager
		.addAdvertisementListener(SmartDeviceDictionaryDiscovery
				.getInstance());
		
		SmartDevicePublisher.getInstance().bindP2PNetworkManager(serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance()
		//		.bindP2PNetworkManager(serv);
		QueryExecutor.getInstance().bindP2PNetworkManager(serv);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			SmartDevicePublisher.getInstance().unbindP2PNetworkManager(serv);
			//SmartDeviceServerDictionaryDiscovery.getInstance()
			//		.unbindP2PNetworkManager(serv);
			QueryExecutor.getInstance().unbindP2PNetworkManager(serv);

			
			p2pNetworkManager
			.removeAdvertisementListener(SmartDeviceDictionaryDiscovery
					.getInstance());
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		SmartDevicePublisher.getInstance()
				.bindPeerCommunicator(serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance()
		//.bindPeerCommunicator(serv);
		SmartDeviceLocalConfigurationServer.getInstance().bindPeerCommunicator(
				serv);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		SmartDeviceLocalConfigurationServer.getInstance()
				.unbindPeerCommunicator(serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance()
		//		.unbindPeerCommunicator(serv);
		SmartDevicePublisher.getInstance()
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

		SmartDevicePublisher.getInstance().bindP2PDictionary(serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance().bindP2PDictionary(
		//		serv);
		QueryExecutor.getInstance().bindP2PDictionary(serv);
		// showActualImportedSourcesAsync();
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		//SmartDeviceServerDictionaryDiscovery.getInstance().unbindP2PDictionary(
		//		serv);
		SmartDevicePublisher.getInstance().unbindP2PDictionary(serv);
		QueryExecutor.getInstance().unbindP2PDictionary(serv);

		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;

		SmartDevicePublisher.getInstance().bindJxtaServicesProvider(serv);
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		SmartDevicePublisher.getInstance().unbindJxtaServicesProvider(serv);

		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;

		SmartDevicePublisher.getInstance().bindPQLGenerator(
				serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance().bindPQLGenerator(
		//		serv);
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		SmartDevicePublisher.getInstance().unbindPQLGenerator(
				serv);
		//SmartDeviceServerDictionaryDiscovery.getInstance().unbindPQLGenerator(
		//		serv);

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

	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}

}