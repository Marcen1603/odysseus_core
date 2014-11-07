package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.TemperSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.RPiGPIOActor.State;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service.ServerExecutorService;

public class SmartDeviceServer {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static SmartDeviceServer instance;
	private static IP2PDictionary p2pDictionary;
	// private static ISmartDevice localSmartDevice;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IJxtaServicesProvider jxtaServicesProvider;

	final long SmartDeviceAdvertisement_LIFETIME = 30 * 2;
	final long SmartDeviceAdvertisement_EXPIRATION_TIME = 30 * 3;

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
			p2pNetworkManager = null;
		}
	}

	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
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

				try {
					LogicProcessor.getInstance().initForLocalSmartDevice();
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

		LOG.debug("initSmartDeviceForAdvertisement cleanPeerID:"
				+ cleanPeerID);

		//
		// 1. Instantiate Sensors:
		// Temper1
		TemperSensor temper = new TemperSensor("temper", peerName, "");
		temper.createActivityInterpreterWithCondition("hot", "Temperature > 24");
		temper.createActivityInterpreterWithCondition("cold",
				"Temperature < 18");

		//
		// GPIO_07 as input sensor
		RPiGPIOSensor gpioTaste7 = new RPiGPIOSensor("RPiGPIOTaster7",
				peerName, "");// postfix: cleanPeerID
		gpioTaste7.setInputPin(7);
		gpioTaste7.createActivityInterpreterForPinState("Tasterbetaetigt",
				RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);

		//
		// 2. instantiate SmartDevice:
		getLocalSmartDevice().setPeerID(getLocalPeerID());
		getLocalSmartDevice().setPeerName(getLocalPeerName());
		getLocalSmartDevice().setSmartDeviceConfig(
				SmartDeviceLocalConfigurationServer.getInstance()
						.getSmartDeviceConfig());
		// getLocalSmartDevice().addConnectedFieldDevice(temper1);
		getLocalSmartDevice().addConnectedFieldDevice(temper);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste7);

		temper.setSmartDevice(getLocalSmartDevice());
		gpioTaste7.setSmartDevice(getLocalSmartDevice());

		// //////////////////////////////////////////////////////////
		// LED GPIO_11
		RPiGPIOActor gpioLED11 = new RPiGPIOActor("RPiLED11", peerName, "");
		gpioLED11.createLogicRuleWithState("hot", State.TOGGLE);
		gpioLED11.createLogicRuleWithState("cold", State.TOGGLE);
		// TODO: gpioLED11.createLogicRuleWithState("Tasterbetaetigt",
		// State.TOGGLE);
		getLocalSmartDevice().addConnectedFieldDevice(gpioLED11);

		//
		// 3. Execute queries:
		executeActivityInterpreterQueries(getLocalSmartDevice());

		// 4. SmartDevice is ready for advertisement now:
		getLocalSmartDevice().setReady(true);
	}

	void executeActivityInterpreterQueries(ASmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String viewName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					QueryExecutor.getInstance()
							.executeQueryNow(viewName, query);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			for (ActivityInterpreter activityInterpreter : sensor
					.getActivityInterpreters()) {
				String activityName = activityInterpreter.getActivityName();
				String activitySourceName = activityInterpreter
						.getActivitySourceName();

				for (Entry<String, String> entry : activityInterpreter
						.getActivityInterpreterQueries(activityName).entrySet()) {
					String viewName = entry.getKey();
					String query = entry.getValue();

					try {
						QueryExecutor.getInstance().executeQueryNow(viewName,
								query);

					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}

				QueryExecutor.getInstance().exportWhenPossibleAsync(
						activitySourceName);
			}
		}
	}
}
