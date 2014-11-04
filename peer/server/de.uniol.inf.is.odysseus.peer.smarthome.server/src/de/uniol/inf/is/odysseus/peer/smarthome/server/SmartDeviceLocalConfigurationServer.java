package de.uniol.inf.is.odysseus.peer.smarthome.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;

public class SmartDeviceLocalConfigurationServer implements
		IPeerCommunicatorListener {
	public static final String SMART_DEVICE_CONFIG_FILE = "odysseusSmartDevice.conf";
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static SmartDeviceLocalConfigurationServer instance;
	IPeerCommunicator peerCommunicator;
	private static SmartDeviceConfig smartDeviceConfig;
	
	SmartDeviceLocalConfigurationServer(){
		initSmartDeviceConfig();
	}
	
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof SmartDeviceConfigurationRequestMessage) {
			try {
				SmartDeviceConfigurationResponseMessage configResponse = new SmartDeviceConfigurationResponseMessage(
						"Test config");
				configResponse.setSmartDeviceConfig(
						getSmartDeviceConfig());

				try {
					getPeerCommunicator().send(
							senderPeer, configResponse);
				} catch (PeerCommunicationException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (message instanceof SmartDeviceConfigurationResponseMessage) {
			SmartDeviceConfigurationResponseMessage configResponse = (SmartDeviceConfigurationResponseMessage) message;

			if (!SmartDeviceServerDictionaryDiscovery.isLocalPeer(senderPeer)) {
				if (getSmartDeviceConfig()
						.getContextname() != null) {
					try {
						
								overWriteSmartDeviceConfigWith(configResponse
										.getSmartDeviceConfig());

						saveSmartDeviceConfig();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		} else if (message instanceof SmartDeviceMessage) {
			LOG.debug("receivedMessage instanceof SmartDeviceMessage");
		}
	}

	public static SmartDeviceLocalConfigurationServer getInstance() {
		if (instance == null) {
			instance = new SmartDeviceLocalConfigurationServer();
		}
		return instance;
	}

	public void bindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		peerCommunicator = _peerCommunicator;
		
		_peerCommunicator
				.registerMessageType(SmartDeviceConfigurationRequestMessage.class);
		_peerCommunicator
				.registerMessageType(SmartDeviceConfigurationResponseMessage.class);
		_peerCommunicator.addListener(this,
				SmartDeviceConfigurationRequestMessage.class);
		_peerCommunicator.addListener(this,
				SmartDeviceConfigurationResponseMessage.class);
	}

	public void unbindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		if (_peerCommunicator != null) {
			_peerCommunicator.removeListener(this,
					SmartDeviceConfigurationResponseMessage.class);
			_peerCommunicator.removeListener(this,
					SmartDeviceConfigurationRequestMessage.class);
			peerCommunicator = null;
		}
	}

	public IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}

	public void setPeerCommunicator(IPeerCommunicator peerCommunicator) {
		this.peerCommunicator = peerCommunicator;
	}
	
	public void overWriteSmartDeviceConfigWith(SmartDeviceConfig config) {
		if (smartDeviceConfig == null) {
			initSmartDeviceConfig();
		}
		smartDeviceConfig.overwriteWith(config);
	}
	
	private void initSmartDeviceConfig() {
		if (smartDeviceConfig == null) {
			smartDeviceConfig = new SmartDeviceConfig();
			// overwrite the initital object with information from saved file
			smartDeviceConfig.overwriteWith(SmartDeviceLocalConfiguration
					.getSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE));
		}
	}
	
	public SmartDeviceConfig getSmartDeviceConfig() {
		return smartDeviceConfig;
	}
	
	public static void saveSmartDeviceConfig() {
		SmartDeviceLocalConfiguration.setSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE,
				smartDeviceConfig);
	}
}
