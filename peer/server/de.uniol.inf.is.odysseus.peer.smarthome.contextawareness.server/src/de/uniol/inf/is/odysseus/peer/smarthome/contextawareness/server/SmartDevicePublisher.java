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
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.RPiGPIOActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.RPiGPIOActor.State;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.TemperSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement.SmartDeviceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.SessionManagementService;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceResponseMessage;

public class SmartDevicePublisher {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static final String INIT_SENSORS_SYS_PROPERTY = "smartdevice.init.sensors";
	private static final String INIT_ACTORS_SYS_PROPERTY = "smartdevice.init.actors";
	private static final String INIT_EXAMPLE_ACTIVITY_INTERPRETERS_SYS_PROPERTY = "smartdevice.example.activityinterpreters";
	private static final String INIT_EXAMPLE_LOGIC_RULES_SYS_PROPERTY = "smartdevice.example.logicrules";
	
	private static SmartDevicePublisher instance;
	
	private static IP2PDictionary p2pDictionary;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IPQLGenerator pqlGenerator;

	final long SmartDeviceAdvertisement_LIFETIME_SEC = 30;
	final long SmartDeviceAdvertisement_EXPIRATION_TIME_SEC = 40;
	
	private TemperSensor temper;
	private RPiGPIOSensor gpioTaste7;
	private RPiGPIOActor gpioLED11;
	private RPiGPIOActor gpioLED10;
	private RPiGPIOSensor gpioTaste3;

	SmartDevicePublisher() {
		registerAdvertisementTypes();
		
		initLocalSmartDeviceAsync();
		createAndPublishSmartDeviceAdvertisementWhenPossibleAsync();
	}
	
	private static void registerAdvertisementTypes() {
		if (!AdvertisementFactory.registerAdvertisementInstance(
				SmartDeviceAdvertisement.getAdvertisementType(),
				new SmartDeviceAdvertisementInstantiator())) {
			LOG.error("Couldn't register advertisement type: "
					+ SmartDeviceAdvertisement.getAdvertisementType());
		}
	}

	@SuppressWarnings("unused")
	private void createAndPublishSmartDeviceAdvertisementWhenPossibleAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForJxtaServicesProvider();
				waitForServerExecutorService();

				// printLocalPeerID();
				registerAdvertisementTypes();
				

				LOG.debug("publishSmartDeviceAdvertisementAsync() in 5 sec.");
				threadSleep(5000);

				try {
					LOG.debug("publishSmartDeviceAdvertisementAsync() now and every "
							+ SmartDeviceAdvertisement_LIFETIME_SEC + " sec.");

					while (true) {
						waitForP2PNetworkManager();
						waitForJxtaServicesProvider();
						waitForServerExecutorService();

						SmartDeviceAdvertisement smartDeviceAdv = getLocalSmartDeviceAdvertisement();

						getJxtaServicesProvider().publish(smartDeviceAdv);
						getJxtaServicesProvider().remotePublish(smartDeviceAdv);

						getJxtaServicesProvider().publish(smartDeviceAdv,
								SmartDeviceAdvertisement_LIFETIME_SEC,
								SmartDeviceAdvertisement_EXPIRATION_TIME_SEC);
						getJxtaServicesProvider().remotePublish(smartDeviceAdv,
								SmartDeviceAdvertisement_EXPIRATION_TIME_SEC);

						Thread.sleep(SmartDeviceAdvertisement_LIFETIME_SEC * 1000);
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

	private static SmartDeviceAdvertisement getLocalSmartDeviceAdvertisement() {
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

	private static void threadSleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized static SmartDevicePublisher getInstance() {
		if (instance == null) {
			instance = new SmartDevicePublisher();
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

	public synchronized ASmartDevice getLocalSmartDevice() {
		return SmartDevice.getInstance();
	}

	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	private static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

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
				
				registerAdvertisementTypes();
				
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
		LOG.debug("initForLocalSmartDevice add listener...");
		
		getLocalSmartDevice().addSmartDeviceListener(ActivityInterpreterProcessor.getInstance());
		getLocalSmartDevice().addSmartDeviceListener(LogicRuleProcessor.getInstance());
	}

	private static void waitForTemper() {
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
		while (LogicRuleProcessor.getInstance() == null) {
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
		
		String propActors = System.getProperty(INIT_ACTORS_SYS_PROPERTY);
		if (propActors != null && propActors.equals("true")) {
			initActors(peerName);
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

		

		// SmartDevice is ready for advertisement now:
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
		
		
		gpioTaste3 = new RPiGPIOSensor("RPiGPIOTaster3", peerName, "");// postfix:
		// cleanPeerID
		gpioTaste3.setInputPin(3);

		// getLocalSmartDevice().addConnectedFieldDevice(temper1);
		//getLocalSmartDevice().addConnectedFieldDevice(temper);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste7);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste3);
		
		
		
		
		
		// LED GPIO_11
		gpioLED11 = new RPiGPIOActor("RPiLED11", peerName, "");
		gpioLED11.setPin(11);
		getLocalSmartDevice().addConnectedFieldDevice(gpioLED11);
		
		
		gpioLED10 = new RPiGPIOActor("RPiLED10", peerName, "");
		gpioLED10.setPin(10);
		getLocalSmartDevice().addConnectedFieldDevice(gpioLED10);
	}

	private void addExampleActivityInterpreters(String peerName) {
		if (temper == null) {
			initSensors(peerName);
		}

		//temper.createActivityInterpreterWithCondition("hot", "Temperature > 24");
		//temper.createActivityInterpreterWithCondition("cold", "Temperature < 24");

		gpioTaste7.createActivityInterpreterForPinState("pin7down",//pin7down
				RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);
		
		gpioTaste3.createActivityInterpreterForPinState("pin3down", RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);////pin0down
	}

	private void addExampleLogicRules(String peerName) {
		if (gpioLED11 == null) {
			initActors(peerName);
		}
		
		//TODO: test, test, test
		//gpioLED11.createLogicRuleWithState("hot", State.OFF);
		//gpioLED11.createLogicRuleWithState("cold", State.ON);
		//gpioLED11.createLogicRuleWithState("pindown", State.ON);
		//gpioLED11.createLogicRuleWithState("pin0down", State.OFF);
		
		//Tasterbetaetigt0
		////gpioLED10.createLogicRuleWithState("hot", State.ON);
		////gpioLED10.createLogicRuleWithState("cold", State.OFF);
		gpioLED10.createLogicRuleWithState("pin7down", State.ON);
		//gpioLED10.createLogicRuleWithState("pin3down", State.OFF);
		
		
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

	public boolean isLocalPeer(PeerID peerID) {
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
				SmartDeviceDiscovery.getInstance(),
				SmartDeviceRequestMessage.class);
		_peerCommunicator.addListener(
				SmartDeviceDiscovery.getInstance(),
				SmartDeviceResponseMessage.class);
	}

	public void unbindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		if (peerCommunicator == _peerCommunicator) {
			_peerCommunicator.removeListener(
					SmartDeviceDiscovery.getInstance(),
					SmartDeviceRequestMessage.class);
			_peerCommunicator.removeListener(
					SmartDeviceDiscovery.getInstance(),
					SmartDeviceResponseMessage.class);

			peerCommunicator = null;
		}
	}

	private IPeerCommunicator peerCommunicator;

	public static IPQLGenerator getPQLGenerator() {
		return pqlGenerator;
	}

	public IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}
	
	protected void stopLogicRule(AbstractLogicRule rule) {
		QueryExecutor.getInstance().stopLogicRuleAsync(rule);
	}
}
