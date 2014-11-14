package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.TemperSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOActor.State;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SessionManagementService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.utils.SmartDeviceResponseMessage;

public class SmartDeviceServer {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static final String INIT_SENSORS_SYS_PROPERTY = "smartdevice.init.sensors";
	private static final String INIT_ACTORS_SYS_PROPERTY = "smartdevice.init.actors";
	private static final String INIT_EXAMPLE_ACTIVITY_INTERPRETERS_SYS_PROPERTY = "smartdevice.example.activityinterpreters";
	private static final String INIT_EXAMPLE_LOGIC_RULES_SYS_PROPERTY = "smartdevice.example.logicrules";
	private static SmartDeviceServer instance;
	private static IP2PDictionary p2pDictionary;
	// private static ISmartDevice localSmartDevice;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;

	final long SmartDeviceAdvertisement_LIFETIME = 30 * 2;
	final long SmartDeviceAdvertisement_EXPIRATION_TIME = 30 * 3;
	private static IPQLGenerator pqlGenerator;
	private SmartDeviceServerFieldDeviceListener localSmartDeviceListener;
	private TemperSensor temper;
	private RPiGPIOSensor gpioTaste7;
	private RPiGPIOActor gpioLED11;
	private RPiGPIOActor gpioLED10;
	private RPiGPIOSensor gpioTaste0;

	SmartDeviceServer() {
		initLocalSmartDeviceAsync();
		createAndPublishSmartDeviceAdvertisementWhenPossibleAsync();
	}

	private void createAndPublishSmartDeviceAdvertisementWhenPossibleAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForJxtaServicesProvider();
				waitForServerExecutorService();

				// printLocalPeerID();

				LOG.error("publishSmartDeviceAdvertisementAsync() in 5 sec.");
				threadSleep(5000);

				try {
					LOG.debug("publishSmartDeviceAdvertisementAsync() now and every "
							+ SmartDeviceAdvertisement_LIFETIME + " sec.");

					while (true) {
						waitForP2PNetworkManager();
						waitForJxtaServicesProvider();
						waitForServerExecutorService();

						SmartDeviceAdvertisement smartDeviceAdv = getLocalSmartDeviceAdvertisement();

						getJxtaServicesProvider().publish(smartDeviceAdv);
						getJxtaServicesProvider().remotePublish(smartDeviceAdv);

						getJxtaServicesProvider().publish(smartDeviceAdv,
								SmartDeviceAdvertisement_LIFETIME,
								SmartDeviceAdvertisement_EXPIRATION_TIME);
						getJxtaServicesProvider().remotePublish(smartDeviceAdv,
								SmartDeviceAdvertisement_EXPIRATION_TIME);

						Thread.sleep(SmartDeviceAdvertisement_LIFETIME * 1000);
					}
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (InterruptedException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		thread.setName(getClass() + " SmartDeviceAdvertisement Thread.");
		thread.setDaemon(true);
		thread.start();
	}

	private SmartDeviceAdvertisement getLocalSmartDeviceAdvertisement() {
		String advType = SmartDeviceAdvertisement.getAdvertisementType();
		ID advID = IDFactory.newPipeID(getP2PNetworkManager()
				.getLocalPeerGroupID());
		PeerID localPeerID = getP2PNetworkManager().getLocalPeerID();

		SmartDeviceAdvertisement smartDeviceAdv = (SmartDeviceAdvertisement) AdvertisementFactory
				.newAdvertisement(advType);
		smartDeviceAdv.setID(advID);
		smartDeviceAdv.setPeerID(localPeerID);

		return smartDeviceAdv;
	}

	private void threadSleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static SmartDeviceServer getInstance() {
		if (instance == null) {
			instance = new SmartDeviceServer();
		}
		return instance;
	}

	@SuppressWarnings("unused")
	private static void printLocalPeerID() {
		try {
			LOG.debug("SmartDevice PeerID: "
					+ getP2PNetworkManager().getLocalPeerID().intern()
							.toString());
		} catch (Exception ex) {
			LOG.debug("SmartDevice PeerID currently is null");
		}
	}

	private static void waitForServerExecutorService() {
		while (ServerExecutorService.getServerExecutor() == null
				|| !ServerExecutorService.getServerExecutor().isRunning()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
			}
		}
	}

	private static void waitForJxtaServicesProvider() {
		while (getJxtaServicesProvider() == null
				|| !getJxtaServicesProvider().isActive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void waitForP2PNetworkManager() {
		while (getP2PNetworkManager() == null
				|| !getP2PNetworkManager().isStarted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	public void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	public ASmartDevice getLocalSmartDevice() {
		return SmartDevice.getInstance();
	}

	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager
					.removeAdvertisementListener(SmartDeviceServerDictionaryDiscovery
							.getInstance());
			p2pNetworkManager = null;
		}
	}

	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager
				.addAdvertisementListener(SmartDeviceServerDictionaryDiscovery
						.getInstance());
	}

	private static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

	/*
	 * public static void setLocalSmartDevice(ISmartDevice _localSmartDevice) {
	 * localSmartDevice = _localSmartDevice; }
	 */

	protected static IJxtaServicesProvider getJxtaServicesProvider() {
		return jxtaServicesProvider;
	}

	public void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	public void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	private static String getLocalPeerID() {
		return getP2PNetworkManager().getLocalPeerID().intern().toString();
	}

	private static String getLocalPeerName() {
		return getP2PNetworkManager().getLocalPeerName().intern().toString();
	}

	private void initLocalSmartDeviceAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForServerExecutorService();
				waitForJxtaServicesProvider();
				waitForP2PNetworkManager();
				waitForLogicProcessor();
				waitForTemper();
					
				try {
					initForLocalSmartDevice();
					initLocalSmartDevice();
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		t.setName("SmartDeviceServer initLocalSmartDeviceAsync thread");
		t.setDaemon(true);
		t.start();
	}

	public void initForLocalSmartDevice() {
		localSmartDeviceListener = new SmartDeviceServerFieldDeviceListener();
		SmartDeviceServer.getInstance().getLocalSmartDevice()
				.addFieldDeviceListener(localSmartDeviceListener);
	}

	private void waitForTemper() {
		boolean loop=true;
		while(loop){
			java.util.List<String> list = ServerExecutorService.getServerExecutor().getOperatorNames(SessionManagementService.getActiveSession());
			
			for(String l : list){
				if(l.equalsIgnoreCase("temper1access")){
					loop = false;
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
	
	protected void waitForLogicProcessor() {
		while (LogicProcessor.getInstance() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private void initLocalSmartDevice() {
		/*****************************************
		 * init smart device for advertisement
		 *****************************************/

		// TODO: GUI für die folgenden Schritte bauen!

		String peerIdString = getLocalPeerID().intern().toString();
		String cleanPeerID = peerIdString.replaceAll("[-+.^:,]", "");
		String peerName = getP2PNetworkManager().getLocalPeerName().intern()
				.toString();

		LOG.debug("initSmartDeviceForAdvertisement cleanPeerID:" + cleanPeerID);

		getLocalSmartDevice().setPeerID(getLocalPeerID());
		getLocalSmartDevice().setPeerName(getLocalPeerName());
		getLocalSmartDevice().setSmartDeviceConfig(
				SmartDeviceLocalConfigurationServer.getInstance()
						.getSmartDeviceConfig());

		String propertySensors = System.getProperty(INIT_SENSORS_SYS_PROPERTY);
		if (propertySensors != null && propertySensors.equals("true")) {
			initSensors(peerName);
		}

		String propActivityInterpreters = System
				.getProperty(INIT_EXAMPLE_ACTIVITY_INTERPRETERS_SYS_PROPERTY);
		if (propActivityInterpreters != null
				&& propActivityInterpreters.equals("true")) {
			addExampleActivityInterpreters(peerName);
		}

		String propertyActors = System
				.getProperty(INIT_EXAMPLE_LOGIC_RULES_SYS_PROPERTY);
		if (propertyActors != null && propertyActors.equals("true")) {
			addExampleLogicRules(peerName);
		}

		String propActors = System.getProperty(INIT_ACTORS_SYS_PROPERTY);
		if (propActors != null && propActors.equals("true")) {
			initActors(peerName);
		}

		//
		// 3. Execute queries:
		LogicProcessor.getInstance().executeActivityInterpreterQueries(
				getLocalSmartDevice());

		// 4. SmartDevice is ready for advertisement now:
		getLocalSmartDevice().setReady(true);
		
	}

	private void initSensors(String peerName) {
		//
		// 1. Instantiate Sensors:
		// Temper1
		temper = new TemperSensor("temper", peerName, "");

		//
		// GPIO_07 as input sensor
		gpioTaste7 = new RPiGPIOSensor("RPiGPIOTaster7", peerName, "");// postfix:
																		// cleanPeerID
		gpioTaste7.setInputPin(7);
		
		
		gpioTaste0 = new RPiGPIOSensor("RPiGPIOTaster0", peerName, "");// postfix:
		// cleanPeerID
		gpioTaste0.setInputPin(0);

		// getLocalSmartDevice().addConnectedFieldDevice(temper1);
		getLocalSmartDevice().addConnectedFieldDevice(temper);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste7);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste0);
		
	}

	private void addExampleActivityInterpreters(String peerName) {
		if (temper == null) {
			initSensors(peerName);
		}

		temper.createActivityInterpreterWithCondition("hot", "Temperature > 24");
		temper.createActivityInterpreterWithCondition("cold",
				"Temperature < 24");

		gpioTaste7.createActivityInterpreterForPinState("Tasterbetaetigt",
				RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);
		
		gpioTaste0.createActivityInterpreterForPinState("Tasterbetaetigt0", RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);
	}

	private void addExampleLogicRules(String peerName) {
		if (gpioLED11 == null) {
			initActors(peerName);
		}
		//TODO: test, test, test
		gpioLED11.createLogicRuleWithState("hot", State.OFF);
		gpioLED11.createLogicRuleWithState("cold", State.ON);
		//gpioLED11.createLogicRuleWithState("Tasterbetaetigt", State.TOGGLE);
		
		//Tasterbetaetigt0
		//gpioLED10.createLogicRuleWithState("Tasterbetaetigt0", State.OFF);
		gpioLED10.createLogicRuleWithState("hot", State.ON);
		gpioLED10.createLogicRuleWithState("cold", State.OFF);
	}

	private void initActors(String peerName) {
		// //////////////////////////////////////////////////////////
		// LED GPIO_11
		gpioLED11 = new RPiGPIOActor("RPiLED11", peerName, "");
		gpioLED11.setPin(11);
		getLocalSmartDevice().addConnectedFieldDevice(gpioLED11);
		
		
		gpioLED10 = new RPiGPIOActor("RPiLED10", peerName, "");
		gpioLED10.setPin(10);
		getLocalSmartDevice().addConnectedFieldDevice(gpioLED10);
	}

	public static boolean isLocalPeer(String peerIDString) {
		String str1 = getP2PNetworkManager().getLocalPeerID().intern()
				.toString();

		if (str1 == null || peerIDString == null)
			return false;
		return str1.equals(peerIDString);
	}

	public static boolean isLocalPeer(PeerID peerID) {
		if (getP2PNetworkManager() == null
				|| getP2PNetworkManager().getLocalPeerID() == null
				|| getP2PNetworkManager().getLocalPeerID().intern() == null) {
			return false;
		} else if (peerID == null || peerID.intern() == null) {
			return false;
		}

		return isLocalPeer(peerID.intern().toString());
	}

	public void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	public void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}

	public void bindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		peerCommunicator = _peerCommunicator;
		_peerCommunicator.registerMessageType(SmartDeviceRequestMessage.class);
		_peerCommunicator.registerMessageType(SmartDeviceResponseMessage.class);

		_peerCommunicator.addListener(
				SmartDeviceServerDictionaryDiscovery.getInstance(),
				SmartDeviceRequestMessage.class);
		_peerCommunicator.addListener(
				SmartDeviceServerDictionaryDiscovery.getInstance(),
				SmartDeviceResponseMessage.class);
	}

	public void unbindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		if (peerCommunicator == _peerCommunicator) {
			_peerCommunicator.removeListener(
					SmartDeviceServerDictionaryDiscovery.getInstance(),
					SmartDeviceRequestMessage.class);
			_peerCommunicator.removeListener(
					SmartDeviceServerDictionaryDiscovery.getInstance(),
					SmartDeviceResponseMessage.class);

			// peerCommunicator.unregisterMessageType(SmartDeviceRequestMessage.class);
			// peerCommunicator.unregisterMessageType(SmartDeviceResponseMessage.class);
			peerCommunicator = null;
		}
	}

	private IPeerCommunicator peerCommunicator;

	// /private static IPQLGenerator pqlGenerator;
	// /private static IP2PDictionary p2pDictionary;
	// /private static IP2PNetworkManager p2pNetworkManager;

	/*
	 * public static IPQLGenerator getPQLGenerator() { return pqlGenerator; }
	 * 
	 * public static IPQLGenerator getPQLGenerator() { return pqlGenerator; }
	 */

	public static IPQLGenerator getPQLGenerator() {
		return pqlGenerator;
	}

	public IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}

	public void addListener(ISmartDeviceDictionaryListener listener) {
		LOG.debug(" addListener");
		SmartDeviceServerDictionaryDiscovery.getInstance()
				.addListener(listener);
	}

	public void removeListener(ISmartDeviceDictionaryListener listener) {
		SmartDeviceServerDictionaryDiscovery.getInstance().removeListener(
				listener);
	}

	protected void stopLogicRule(LogicRule rule) {
		QueryExecutor.getInstance().stopLogicRuleAsync(rule);
	}
}
