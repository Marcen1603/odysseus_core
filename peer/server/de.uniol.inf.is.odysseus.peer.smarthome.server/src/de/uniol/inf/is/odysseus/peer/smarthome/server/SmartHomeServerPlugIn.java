package de.uniol.inf.is.odysseus.peer.smarthome.server;

import net.jxta.peer.PeerID;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
/*
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;
*/

public class SmartHomeServerPlugIn implements BundleActivator {

	public static final String NO_P2P_CONNECTION_TEXT = "<no p2p connection>";

	//private static ISession activeSession;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	@SuppressWarnings("unused")
	private static SmartDeviceConfigurationListener smartDeviceConfigurationListener;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}
	
	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		//peerCommunicator.registerMessageType(SmartDeviceMessage.class);
		//smartDeviceConfigurationListener = new SmartDeviceConfigurationListener();
		/*
		peerCommunicator.addListener(smartDeviceConfigurationListener, SmartDeviceMessage.class);
		peerCommunicator.addListener(smartDeviceConfigurationListener, SmartDeviceConfigurationRequestMessage.class);
		peerCommunicator.addListener(smartDeviceConfigurationListener, SmartDeviceConfigurationResponseMessage.class);
		*/
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
	
	public static boolean isLocalPeer(PeerID peer)
	{
		if(SmartHomeServerPlugIn.getP2PNetworkManager()==null || SmartHomeServerPlugIn.getP2PNetworkManager().getLocalPeerName()==null
				|| SmartHomeServerPlugIn.getP2PNetworkManager().getLocalPeerName().intern()==null){
			return false;
		}else if(peer==null || peer.intern()==null){
			return false;
		}
		
		String str1 = SmartHomeServerPlugIn.getP2PNetworkManager().getLocalPeerName().intern().toString();
		String str2 = peer.intern().toString();
		
		if(str1==null || str2==null) return false;
		return str1.equals(str2);
	}
	
	public static class SmartDeviceConfigurationListener implements IPeerCommunicatorListener{
		@Override
		public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
			System.out.println("SmartHomeServerP2PCommunicatorListener receivedMessage!!");
			
			/*
			if(message instanceof SmartDeviceMessage){
				SmartDeviceMessage smessage = (SmartDeviceMessage)message;
				
				if(!isLocalPeer(senderPeer) && !smessage.getText().equals("Echo! from Server")){
					try {
						SmartHomeServerPlugIn.getPeerCommunicator().send(senderPeer, new SmartDeviceMessage("Echo! from Server"));
						
						
						
					} catch (PeerCommunicationException e) {
						e.printStackTrace();
					}
				}
			}else if(message instanceof SmartDeviceConfigurationRequestMessage){
				@SuppressWarnings("unused")
				SmartDeviceConfigurationRequestMessage configRequest = (SmartDeviceConfigurationRequestMessage)message;
				
				//read what is requested in: configRequest
				
				
				//load SmartDeviceConfig:
				//load: smartDeviceConfig
				SmartDeviceConfig smartDeviceConfig = new SmartDeviceConfig();
				
				
				//load and send the smart device config to the requester (senderPeer):
				SmartDeviceConfigurationResponseMessage configResponse = new SmartDeviceConfigurationResponseMessage();
				configResponse.setSmartDeviceConfig(smartDeviceConfig);
				
				try {
					SmartHomeServerPlugIn.getPeerCommunicator().send(senderPeer, configResponse);
				} catch (PeerCommunicationException e) {
					e.printStackTrace();
				}
			}else if(message instanceof SmartDeviceConfigurationResponseMessage){
				SmartDeviceConfigurationResponseMessage configResponse = (SmartDeviceConfigurationResponseMessage)message;
				
				//Save the config response for this peer:
				@SuppressWarnings("unused")
				SmartDeviceConfig smartDeviceConfig = configResponse.getSmartDeviceConfig();
				
				//save: smartDeviceConfig
				
			}
			*/
		}
		
	}
}
