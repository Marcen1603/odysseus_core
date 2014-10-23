package de.uniol.inf.is.odysseus.peer.smarthome.rcp;

import net.jxta.document.Advertisement;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;


public class SmartHomeRCPActivator implements BundleActivator {
	public static final String NO_P2P_CONNECTION_TEXT = "<no p2p connection>";

	//private static ISession activeSession;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static SmartDeviceAdvertisementListener smartDeviceAdvertisementListener;
	
	@Override
	public void start(BundleContext context) throws Exception {
		
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		
	}
	
	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		
		smartDeviceAdvertisementListener = new SmartDeviceAdvertisementListener();
		p2pNetworkManager.addAdvertisementListener(smartDeviceAdvertisementListener);
	}
	
	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		//peerCommunicator.registerMessageType(SmartDeviceMessage.class);
	}
	
	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			//peerCommunicator.unregisterMessageType(SmartDeviceMessage.class);
			peerCommunicator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}
	
	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
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
	public static class SmartDeviceAdvertisementListener implements IAdvertisementDiscovererListener {

		@Override
		public void advertisementDiscovered(Advertisement advertisement) {
			//System.out.println("SmartHomeRCPActivator advertisementDiscovered:");
			//System.out.println("advertisement: "+advertisement.toString());
			
		}

		@Override
		public void updateAdvertisements() {
			//System.out.println("SmartHomeRCPActivator updateAdvertisements");
			
		}
		
	}
}
