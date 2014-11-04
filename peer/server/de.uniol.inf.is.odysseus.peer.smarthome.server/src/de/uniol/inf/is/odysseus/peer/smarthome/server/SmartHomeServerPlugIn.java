package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RPiGPIOActor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Temper1Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.ServerExecutorService;

public class SmartHomeServerPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static Bundle bundle;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IPQLGenerator pqlGenerator;
	private static P2PDictionaryListener p2pDictionaryListener;

	// SmartDevice:
	
	
	private static SmartDevice localSmartDevice;
	private static ArrayList<PeerID> foundPeerIDs = new ArrayList<PeerID>();
	private static Collection<PeerID> refreshing = Lists.newLinkedList();

	public void start(BundleContext bundleContext) throws Exception {
		SmartDeviceDictionaryDiscovery.getInstance().addListener(
				LogicExecutor.getInstance());

		registerAdvertisementTypes();

		bundle = bundleContext.getBundle();
	}

	public void stop(BundleContext context) throws Exception {
		SmartDeviceDictionaryDiscovery.getInstance().removeListener(
				LogicExecutor.getInstance());
		bundle = null;
	}

	private static void registerAdvertisementTypes() {
		if (!AdvertisementFactory.registerAdvertisementInstance(
				SmartDeviceAdvertisement.getAdvertisementType(),
				new SmartDeviceAdvertisementInstantiator())) {
			LOG.error("Couldn't register advertisement type: "
					+ SmartDeviceAdvertisement.getAdvertisementType());
		}
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
		setP2pNetworkManager(serv);

		getP2PNetworkManager().addAdvertisementListener(SourceAdvertisementListener.getInstance());
	}

	private static void setP2pNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			getP2PNetworkManager().removeAdvertisementListener(
					SourceAdvertisementListener.getInstance());
			setP2pNetworkManager(null);
		}
	}

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		SmartDeviceDictionaryDiscovery.getInstance().bindPeerCommunicator(serv);
		SmartDeviceLocalConfiguration.getInstance().bindPeerCommunicator(serv);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			SmartDeviceDictionaryDiscovery.getInstance().unbindPeerCommunicator(serv);
			SmartDeviceLocalConfiguration.getInstance().unbindPeerCommunicator(serv);

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

		p2pDictionaryListener = new P2PDictionaryListener();
		p2pDictionary.addListener(p2pDictionaryListener);
		// showActualImportedSourcesAsync();
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary.removeListener(p2pDictionaryListener);
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;

		createAndPublishSmartDeviceAdvertisementWhenPossibleAsync();

		refreshFoundPeerIDsAsync();
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		LOG.debug("unbindJxtaServicesProvider");
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
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

	@SuppressWarnings("unused")
	private static void showActualImportedSourcesAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForP2PDictionary();
				waitForPQLGenerator();

				UnmodifiableIterator<SourceAdvertisement> iteratorSources = p2pDictionary
						.getImportedSources().iterator();
				while (iteratorSources.hasNext()) {
					SourceAdvertisement sourceAdv = iteratorSources.next();

					LOG.debug("SourceAdv actual imported: "
							+ sourceAdv.getName());
				}
			}
		});
		thread.setName("SmartHome showActualImportedSourcesAsync Thread");
		thread.setDaemon(true);
		thread.start();
	}

	private static void printLocalPeerID() {
		try {
			LOG.debug("SmartDevice PeerID: "
					+ SmartHomeServerPlugIn.getP2PNetworkManager()
							.getLocalPeerID().intern().toString());
		} catch (Exception ex) {
			LOG.debug("SmartDevice PeerID currently is null");
		}
	}

	public static boolean isLocalPeer(PeerID peerID) {
		if (SmartHomeServerPlugIn.getP2PNetworkManager() == null
				|| getP2PNetworkManager().getLocalPeerID() == null
				|| SmartHomeServerPlugIn.getP2PNetworkManager()
						.getLocalPeerID().intern() == null) {
			return false;
		} else if (peerID == null || peerID.intern() == null) {
			return false;
		}

		return isLocalPeer(peerID.intern().toString());
	}

	public static boolean isLocalPeer(String peerIDString) {
		String str1 = SmartHomeServerPlugIn.getP2PNetworkManager()
				.getLocalPeerID().intern().toString();

		if (str1 == null || peerIDString == null)
			return false;
		return str1.equals(peerIDString);
	}

	

	private static boolean isPeerIdAvailable(PeerID peerID) {
		if (peerID == null) {
			// LOG.debug("peerID==null");
			return false;
		} else if (isLocalPeer(peerID.intern().toString())) {
			// LOG.debug("peerID is LocalPeer: " + peerID.intern().toString());
			return false;
		} else if (foundPeerIDs == null || peerID == null
				|| peerID.intern() == null
				|| peerID.intern().toString().isEmpty()) {
			// LOG.debug("peerID is null or empty");
			return false;
		} else if (!isFoundInFoundPeers(peerID)) {
			@SuppressWarnings("unused")
			String debugMessage = "is not in the foundPeerID's peerID:";

			try {
				debugMessage += "peerID: " + peerID.intern().toString();
			} catch (Exception ex) {
				debugMessage += "peerID: null";
			}

			// LOG.debug(debugMessage);
			return false;
		} else {
			return true;
		}
	}

	static LinkedHashMap<String, String> getSourcesNeededForImport() {
		if (sourcesNeededForImport == null) {
			sourcesNeededForImport = new LinkedHashMap<String, String>();
		}
		return sourcesNeededForImport;
	}

	public static void removeSourceNeededForImport(String srcName) {
		getSourcesNeededForImport().remove(srcName);
	}

	private static boolean isFoundInFoundPeers(PeerID peerID) {
		ArrayList<PeerID> foundPeerIDArray = getFoundPeerIDs();
		for (PeerID foundPeerID : foundPeerIDArray) {
			if (foundPeerID.intern().toString()
					.equals(peerID.intern().toString())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<PeerID> getFoundPeerIDs() {
		ArrayList<PeerID> foundPeerIDArray = Lists.newArrayList();
		foundPeerIDArray.addAll(p2pDictionary.getRemotePeerIDs());
		return foundPeerIDArray;
	}

	

	

	private static void refreshFoundPeerIDsAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					waitForP2PDictionary();

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					try {
						refreshFoundPeerIDs();
					} catch (Exception ex) {
						LOG.error(ex.getMessage(), ex);
					}
				}
			}
		});
		t.setName("SmartHomeServerPlugIn refresh foundPeerIDs Thread");
		t.setDaemon(true);
		t.start();
	}

	public static void refreshFoundPeerIDs() {
		Collection<PeerID> foundPeerIDsCopy = null;
		if (foundPeerIDs != null && p2pDictionary != null
				&& p2pDictionary.getRemotePeerIDs() != null) {
			synchronized (foundPeerIDs) {
				foundPeerIDs.clear();
				foundPeerIDs.addAll(p2pDictionary.getRemotePeerIDs());
				foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
			}

			if (foundPeerIDsCopy != null) {
				for (final PeerID remotePeerID : foundPeerIDsCopy) {
					synchronized (refreshing) {
						if (refreshing.contains(remotePeerID)) {
							continue;
						}

						refreshing.add(remotePeerID);
					}
				}
			}
		}

		// printFoundPeerIDs(foundPeerIDsCopy);
	}

	@SuppressWarnings("unused")
	private static void printFoundPeerIDs(Collection<PeerID> foundPeerIDsCopy) {
		if (foundPeerIDsCopy != null) {
			LOG.debug("foundPeerIDs size:" + foundPeerIDsCopy.size());
			for (PeerID peerID : foundPeerIDsCopy) {
				LOG.debug("peerID: " + peerID.intern().toString());
			}
		} else {
			LOG.debug("foundPeerIDsCopy==null");
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

	private static void waitForP2PDictionary() {
		while (getP2PDictionary() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void waitForPQLGenerator() {
		while (getPQLGenerator() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
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

	private static String getLocalPeerID() {
		return getP2PNetworkManager().getLocalPeerID().intern().toString();
	}

	private static String getLocalPeerName() {
		return getP2PNetworkManager().getLocalPeerName().intern().toString();
	}

	

	private static LinkedHashMap<String, String> sourcesNeededForImport;

	public static SmartDevice getLocalSmartDevice() {
		return localSmartDevice;
	}

	public static void setLocalSmartDevice(SmartDevice localSmartDevice) {
		SmartHomeServerPlugIn.localSmartDevice = localSmartDevice;
	}

	private static void initSmartDevice() {
		/*****************************************
		 * TODO: init smart device for advertisement
		 *****************************************/

		// TODO: GUI für die folgenden Schritte bauen!

		String peerIdString = getLocalPeerID().intern().toString();
		String cleanPeerID = peerIdString.replaceAll("[-+.^:,]", "");
		String peerName = getP2PNetworkManager().getLocalPeerName().intern()
				.toString();

		LOG.debug("-----\ninitSmartDeviceForAdvertisement\n\tcleanPeerID:"
				+ cleanPeerID);

		//
		// 1. Instantiate Sensors:
		// Temper1
		Temper1Sensor temper1 = new Temper1Sensor("temper1", peerName, "");// cleanPeerID
		temper1.addActivityForCondition("hot", "Temperature > 24");
		//
		// GPIO_07 as input sensor
		RPiGPIOSensor gpioTaste7 = new RPiGPIOSensor("RPiGPIOTaster7",
				peerName, "");// postfix: cleanPeerID
		gpioTaste7.setInputPin(7);
		gpioTaste7.addActivityForPinState("Tasterbetaetigt",
				RPiGPIOSensor.GPIO_SENSOR_STATE.HIGH);

		//
		// 2. instantiate SmartDevice:
		setLocalSmartDevice(new SmartDevice());
		getLocalSmartDevice().setPeerID(getLocalPeerID());
		getLocalSmartDevice().setPeerName(getLocalPeerName());
		getLocalSmartDevice().setSmartDevice(SmartDeviceLocalConfiguration.getInstance().getSmartDeviceConfig());
		getLocalSmartDevice().addConnectedFieldDevice(temper1);
		getLocalSmartDevice().addConnectedFieldDevice(gpioTaste7);

		LOG.debug("LocalSmartDeviceCtx:"
				+ getLocalSmartDevice().getContextName());

		//
		// 3. Execute queries:
		executeSensorQueries(getLocalSmartDevice());

		//
		// 4. SmartDevice is ready for advertisement now:
		getLocalSmartDevice().setReady(true);

		// //////////////////////////////////////////////////////////
		// LED GPIO_11
		RPiGPIOActor gpioLED11 = new RPiGPIOActor("gpioLED11", peerName, "");// cleanPeerID
		gpioLED11.addPossibleActivityName("hot");
		// gpioLED11.addLogicRule(logicRuleLEDOnHot);

		gpioLED11.addActivityForState("hot", RPiGPIOActor.State.TOGGLE);
		gpioLED11.addActivityForState("Tasterbetaetigt",
				RPiGPIOActor.State.TOGGLE);

		/*
		 * ArrayList<String> manyActivities = new ArrayList<String>();
		 * manyActivities.add("hot"); manyActivities.add("blub");
		 * gpioLED11.addActivitiesForState(manyActivities,
		 * RPiGPIOActor.State.TOGGLE);
		 */

		getLocalSmartDevice().addConnectedFieldDevice(gpioLED11);
	}

	private static void executeSensorQueries(SmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String viewName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					LogicExecutor.getInstance();
					LogicExecutor.executeQueryNow(viewName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + viewName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}

			// stream with participating activities of the sensor
			for (Entry<String, String> activityInterpreterQuery : sensor
					.getViewForActivityInterpreterQueries().entrySet()) {
				String viewName = activityInterpreterQuery.getKey();
				String query = activityInterpreterQuery.getValue();

				try {
					LogicExecutor.executeQueryNow(viewName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + viewName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}

			// Export ActivitySources:
			for (String possibleActivity : sensor.getPossibleActivityNames()) {
				try {
					getP2PDictionary().exportSource(
							sensor.getActivitySourceName(possibleActivity));
				} catch (PeerException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private static void createAndPublishSmartDeviceAdvertisementWhenPossibleAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForJxtaServicesProvider();
				waitForServerExecutorService();

				printLocalPeerID();

				LOG.error("publishSmartDeviceAdvertisementAsync() in 5 sec.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					initSmartDevice();

					long lifetime = 30 * 2;
					long expirationTime = 30 * 3;

					LOG.debug("publishSmartDeviceAdvertisementAsync() now and every "
							+ lifetime + " sec.");

					while (true) {
						// LOG.debug("publishSmartDevice:");
						if (getP2PNetworkManager() != null
								&& getP2PNetworkManager().getLocalPeerGroupID() != null) {
							String advType = SmartDeviceAdvertisement
									.getAdvertisementType();
							ID advID = IDFactory
									.newPipeID(getP2PNetworkManager()
											.getLocalPeerGroupID());
							PeerID localPeerID = getP2PNetworkManager()
									.getLocalPeerID();

							SmartDeviceAdvertisement smartDeviceAdv = (SmartDeviceAdvertisement) AdvertisementFactory
									.newAdvertisement(advType);
							smartDeviceAdv.setID(advID);
							smartDeviceAdv.setPeerID(localPeerID);

							getJxtaServicesProvider().publish(smartDeviceAdv);
							getJxtaServicesProvider().remotePublish(
									smartDeviceAdv);

							getJxtaServicesProvider().publish(smartDeviceAdv,
									lifetime, expirationTime);
							getJxtaServicesProvider().remotePublish(
									smartDeviceAdv, expirationTime);
						}

						Thread.sleep(lifetime * 1000);
					}
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (InterruptedException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		thread.setName("SmartHomeServerPlugIn SmartDeviceAdvertisement Thread");
		thread.setDaemon(true);
		thread.start();
	}

	public static void addNeededSourceForImport(String activitySourceName,
			String peerIDString) {
		if (!getP2PDictionary().isImported(activitySourceName)) {

			getSourcesNeededForImport().put(activitySourceName, peerIDString);

			LOG.debug("addNeededSourceForImport(" + activitySourceName + ", "
					+ peerIDString + ")");
		}
	}

	static void getSmartDeviceInformations(SmartDeviceAdvertisement adv) {
		if (adv != null && isPeerIdAvailable(adv.getPeerID())) {
			try {
				SmartDeviceRequestMessage smartDevRequest = new SmartDeviceRequestMessage(
						"request");

				SmartHomeServerPlugIn.getPeerCommunicator().send(
						adv.getPeerID(), smartDevRequest);
			} catch (PeerCommunicationException e) {
				LOG.error(e.getMessage() + " PeerID:"
						+ adv.getPeerID().intern().toString(), e);
			}
		}
	}

	static void importIfNeccessary(SourceAdvertisement srcAdv) {
		// TODO: import if neccessary

		LOG.debug("importIfNeccessary srcAdv.getName(): " + srcAdv.getName());

		if (!getP2PDictionary().isImported(srcAdv.getName())
				&& getSourcesNeededForImport().containsKey(srcAdv.getName())) {
			try {
				LOG.debug("---importIfNeccessary---importSource:"
						+ srcAdv.getName());

				getP2PDictionary().importSource(srcAdv, srcAdv.getName());
				removeSourceNeededForImport(srcAdv.getName());
			} catch (PeerException e) {
				e.printStackTrace();
			} catch (InvalidP2PSource e) {
				e.printStackTrace();
			}
		}
	}
}
