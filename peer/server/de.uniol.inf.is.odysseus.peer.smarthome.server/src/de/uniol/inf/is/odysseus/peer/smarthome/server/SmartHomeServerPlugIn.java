package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import net.jxta.document.Advertisement;
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

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.ISmartDeviceDictionaryListener;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceDictionary;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RPiGPIOActor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Temper1Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.SessionManagementService;
import de.uniol.inf.is.odysseus.core.collection.Context;

public class SmartHomeServerPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static Bundle bundle;
	public static final String NO_P2P_CONNECTION_TEXT = "<no p2p connection>";
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IPQLGenerator pqlGenerator;
	private static P2PDictionaryListener p2pDictionaryListener;

	private static SmartDeviceConfigurationListener smartDeviceConfigurationListener;
	private static AdvertisementListener advertisementListener;

	// SmartDevice:
	public static final String SMART_DEVICE_CONFIG_FILE = "odysseusSmartDevice.conf";
	private static SmartDeviceConfig smartDeviceConfig;
	private static SmartDeviceDictionary smartDeviceDictionary;
	private static SmartDeviceDictionaryListener smartDeviceDictionaryListener;
	private static SmartDevice smartDevice;
	private static SmartDeviceListener smartDeviceListener;

	boolean runningRule1 = false;
	private static ArrayList<PeerID> foundPeerIDs = new ArrayList<PeerID>();
	private static Collection<PeerID> refreshing = Lists.newLinkedList();

	public void start(BundleContext bundleContext) throws Exception {
		initSmartDeviceConfig();
		initSmartDeviceDictionary();

		registerAdvertisementTypes();

		bundle = bundleContext.getBundle();
	}

	public void stop(BundleContext context) throws Exception {
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

	private static void registerAdvertisementTypes() {
		if (!AdvertisementFactory.registerAdvertisementInstance(
				SmartDeviceAdvertisement.getAdvertisementType(),
				new SmartDeviceAdvertisementInstantiator())) {
			LOG.error("Couldn't register advertisement type: "
					+ SmartDeviceAdvertisement.getAdvertisementType());
		}

		AdvertisementFactory.registerAdvertisementInstance(
				SourceAdvertisement.getAdvertisementType(),
				new SourceAdvertisementInstantiator());
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;

		advertisementListener = new AdvertisementListener();
		p2pNetworkManager.addAdvertisementListener(advertisementListener);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager
					.removeAdvertisementListener(advertisementListener);
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(SmartDeviceRequestMessage.class);
		peerCommunicator.registerMessageType(SmartDeviceResponseMessage.class);
		peerCommunicator
				.registerMessageType(SmartDeviceConfigurationRequestMessage.class);
		peerCommunicator
				.registerMessageType(SmartDeviceConfigurationResponseMessage.class);

		smartDeviceListener = new SmartDeviceListener();
		peerCommunicator.addListener(smartDeviceListener,
				SmartDeviceRequestMessage.class);
		peerCommunicator.addListener(smartDeviceListener,
				SmartDeviceResponseMessage.class);

		smartDeviceConfigurationListener = new SmartDeviceConfigurationListener();
		peerCommunicator.addListener(smartDeviceConfigurationListener,
				SmartDeviceConfigurationRequestMessage.class);
		peerCommunicator.addListener(smartDeviceConfigurationListener,
				SmartDeviceConfigurationResponseMessage.class);
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(smartDeviceConfigurationListener,
					SmartDeviceRequestMessage.class);
			peerCommunicator.removeListener(smartDeviceConfigurationListener,
					SmartDeviceResponseMessage.class);
			peerCommunicator
					.unregisterMessageType(SmartDeviceRequestMessage.class);
			peerCommunicator
					.unregisterMessageType(SmartDeviceResponseMessage.class);

			peerCommunicator.removeListener(smartDeviceConfigurationListener,
					SmartDeviceConfigurationResponseMessage.class);
			peerCommunicator.removeListener(smartDeviceConfigurationListener,
					SmartDeviceConfigurationRequestMessage.class);

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

		publishSmartDeviceAdvertisementAsync();

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

	private static void initSmartDeviceForAdvertisement() {
		/*****************************************
		 * TODO: init smart device for advertisement
		 *****************************************/

		// TODO: GUI für die folgenden Schritte bauen!

		String peerIdString = getLocalPeerID().intern().toString();
		String cleanPeerID = peerIdString.replaceAll("[-+.^:,]", "");
		String peerName = p2pNetworkManager.getLocalPeerName().intern()
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
		smartDevice = new SmartDevice();
		smartDevice.setPeerID(getLocalPeerID());
		smartDevice.setPeerName(getLocalPeerName());
		smartDevice.setSmartDevice(getSmartDeviceConfig());
		smartDevice.addConnectedFieldDevice(temper1);
		smartDevice.addConnectedFieldDevice(gpioTaste7);

		LOG.debug("LocalSmartDeviceCtx:" + smartDevice.getContextName());

		//
		// 3. Execute queries:
		executeSensorQueries(smartDevice);

		//
		// 4. SmartDevice is ready for advertisement now:
		smartDevice.setReady(true);

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

		smartDevice.addConnectedFieldDevice(gpioLED11);
	}

	private static void executeSensorQueries(SmartDevice smartDevice) {
		for (Sensor sensor : smartDevice.getConnectedSensors()) {
			for (Entry<String, String> queryForRawValue : sensor
					.getQueriesForRawValues().entrySet()) {
				String queryName = queryForRawValue.getKey();
				String query = queryForRawValue.getValue();

				try {
					executeQueryNow(queryName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + queryName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}

			// stream with participating activities of the sensor
			for (Entry<String, String> activityInterpreterQuery : sensor
					.getQueryForActivityInterpreterQueries().entrySet()) {
				String queryName = activityInterpreterQuery.getKey();
				String query = activityInterpreterQuery.getValue();

				try {
					executeQueryNow(queryName, query);

				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				} catch (Exception ex) {
					LOG.error("EXCEPTION - viewName:" + queryName + " query:"
							+ query);
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private static void publishSmartDeviceAdvertisementAsync() {
		Thread thread = new Thread(new Runnable() {
			@SuppressWarnings("unused")
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				LOG.error("publishSmartDeviceAdvertisementAsync() now:");

				try {
					initSmartDeviceForAdvertisement();

					String advType = SmartDeviceAdvertisement
							.getAdvertisementType();
					ID advID = IDFactory.newPipeID(getP2PNetworkManager()
							.getLocalPeerGroupID());
					PeerID localPeerID = p2pNetworkManager.getLocalPeerID();

					SmartDeviceAdvertisement smartDeviceAdv = (SmartDeviceAdvertisement) AdvertisementFactory
							.newAdvertisement(advType);
					smartDeviceAdv.setID(advID);
					smartDeviceAdv.setPeerID(localPeerID);

					long lifetime = 60 * 2;
					long expirationTime = 60 * 3;

					// TODO: one advertisement has an expirationTime. Publish
					// every
					// X seconds or minutes:

					getJxtaServicesProvider().publish(smartDeviceAdv);
					getJxtaServicesProvider().remotePublish(smartDeviceAdv);

					// getJxtaServicesProvider().publish(smartDeviceAdv,
					// lifetime,expirationTime);
					// getJxtaServicesProvider().remotePublish(smartDeviceAdv,expirationTime);

					// getJxtaServicesProvider().publishInfinite(smartDeviceAdv);
					// getJxtaServicesProvider().remotePublishInfinite(smartDeviceAdv);
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		thread.setName("SmartHomeServerPlugIn SmartDeviceAdvertisement Thread");
		thread.setDaemon(true);
		thread.start();
	}

	private static void printLocalPeerID() {
		try {
			LOG.debug("SmartDevice PeerID: "
					+ SmartHomeServerPlugIn.p2pNetworkManager.getLocalPeerID()
							.intern().toString());
		} catch (Exception ex) {
			LOG.debug("SmartDevice PeerID currently is null");
		}
	}

	public static boolean isLocalPeer(PeerID peerID) {
		if (SmartHomeServerPlugIn.getP2PNetworkManager() == null
				|| p2pNetworkManager.getLocalPeerID() == null
				|| SmartHomeServerPlugIn.p2pNetworkManager.getLocalPeerID()
						.intern() == null) {
			return false;
		} else if (peerID == null || peerID.intern() == null) {
			return false;
		}

		return isLocalPeer(peerID.intern().toString());
	}

	public static boolean isLocalPeer(String peerIDString) {
		String str1 = SmartHomeServerPlugIn.p2pNetworkManager.getLocalPeerID()
				.intern().toString();

		if (str1 == null || peerIDString == null)
			return false;
		return str1.equals(peerIDString);
	}

	public static SmartDeviceConfig getSmartDeviceConfig() {
		if (smartDeviceConfig == null) {
			initSmartDeviceConfig();
		}
		return smartDeviceConfig;
	}

	public static class SmartDeviceConfigurationListener implements
			IPeerCommunicatorListener {
		@Override
		public void receivedMessage(IPeerCommunicator communicator,
				PeerID senderPeer, IMessage message) {
			if (message instanceof SmartDeviceConfigurationRequestMessage) {
				try {
					SmartDeviceConfigurationResponseMessage configResponse = new SmartDeviceConfigurationResponseMessage(
							"Test config");
					configResponse.setSmartDeviceConfig(getSmartDeviceConfig());

					try {
						SmartHomeServerPlugIn.getPeerCommunicator().send(
								senderPeer, configResponse);
					} catch (PeerCommunicationException e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (message instanceof SmartDeviceConfigurationResponseMessage) {
				SmartDeviceConfigurationResponseMessage configResponse = (SmartDeviceConfigurationResponseMessage) message;

				if (!isLocalPeer(senderPeer)) { // if the config was not send
												// out from this peer, then save
												// it:
					if (smartDeviceConfig.getContextname() != null) {
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
	}

	public static class AdvertisementListener implements
			IAdvertisementDiscovererListener {
		@Override
		public void advertisementDiscovered(Advertisement advertisement) {
			// LOG.debug("advertisementDiscovered AdvType:"
			// + advertisement.getAdvType());

			if (advertisement != null
					&& advertisement instanceof SmartDeviceAdvertisement
					&& advertisement.getAdvType().equals(
							SmartDeviceAdvertisement.getAdvertisementType())) {

				SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) advertisement;
				getSmartDeviceInformations(adv);

			} else if (advertisement != null
					&& advertisement instanceof SourceAdvertisement
					&& advertisement.getAdvType().equals(
							SourceAdvertisement.getAdvertisementType())) {
				SourceAdvertisement srcAdv = (SourceAdvertisement) advertisement;

				importIfNeccessary(srcAdv);
			} else if (advertisement != null
					&& advertisement instanceof MultipleSourceAdvertisement
					&& advertisement.getAdvType().equals(
							MultipleSourceAdvertisement.getAdvertisementType())) {
				MultipleSourceAdvertisement multiSourceAdv = (MultipleSourceAdvertisement) advertisement;

				String peerIDStr = multiSourceAdv.getPeerID().intern()
						.toString();

				// TODO:
				if (getSourcesNeededForImport().containsValue(peerIDStr)) {
					for (SourceAdvertisement sAdv : multiSourceAdv
							.getSourceAdvertisements()) {
						try {
							LOG.debug("---importIfNeccessary---Source:"
									+ sAdv.getName());

							if (!getP2PDictionary().isImported(sAdv.getName())) {
								LOG.debug("---importIfNeccessary---importSource:"
										+ sAdv.getName());

								getP2PDictionary().importSource(sAdv,
										sAdv.getName());

								removeSourceNeededForImport(sAdv.getName());
							}
						} catch (PeerException e) {
							e.printStackTrace();
						} catch (InvalidP2PSource e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		private void importIfNeccessary(SourceAdvertisement srcAdv) {
			// TODO: import if neccessary

			// LOG.debug("importIfNeccessary srcAdv.getName(): " +
			// srcAdv.getName());

			if (!getP2PDictionary().isImported(srcAdv.getName())
					&& getSourcesNeededForImport()
							.containsKey(srcAdv.getName())) {
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

		private void getSmartDeviceInformations(SmartDeviceAdvertisement adv) {
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

		@Override
		public void updateAdvertisements() {

		}
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

	private static LinkedHashMap<String, String> getSourcesNeededForImport() {
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

	public static class P2PDictionaryListener implements IP2PDictionaryListener {
		@Override
		public void sourceAdded(IP2PDictionary sender,
				SourceAdvertisement advertisement) {
			LOG.debug("sourceAdded");
		}

		@Override
		public void sourceRemoved(IP2PDictionary sender,
				SourceAdvertisement advertisement) {
			LOG.debug("sourceRemoved");
		}

		@Override
		public void sourceImported(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceImported sourceName:" + sourceName);

			if (advertisement.isLocal()) {
				LOG.debug("advertisement.isLocal()");
				return;
			}

		}

		@Override
		public void sourceImportRemoved(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceImportRemoved sourceName:" + sourceName);

			if (isSourceNameUsingByLogicRule(sourceName)) {
				LOG.debug("-----");
				LOG.debug("you must remove logicRule who depends on sourceName:"
						+ sourceName + "!!!");
				LOG.debug("-----");

				// TODO: Anfragen wieder entfernen:

			}
		}

		@Override
		public void sourceExported(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceExported: " + sourceName);
		}

		@Override
		public void sourceExportRemoved(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceExportRemoved: " + sourceName);
		}
	}

	private static void executeQueryNow(String viewName, String query)
			throws Exception {
		// LOG.error("viewName:" + viewName + " query:" + query);

		Collection<Integer> queryIDs = ServerExecutorService
				.getServerExecutor().addQuery(query, "OdysseusScript",
						SessionManagementService.getActiveSession(),
						"Standard", Context.empty());
		Integer queryId;

		if (queryIDs == null) {
			LOG.debug("queryIDs==null @viewName:" + viewName);
			return;
		}

		if (!queryIDs.iterator().hasNext()) {
			LOG.debug("!queryIDs.iterator().hasNext() @viewName:" + viewName);
			return;
		}

		queryId = queryIDs.iterator().next();
		IPhysicalQuery physicalQuery = ServerExecutorService
				.getServerExecutor().getExecutionPlan().getQueryById(queryId);
		ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
		logicalQuery.setName(viewName);
		logicalQuery.setParserId("P2P");
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText("Exporting " + viewName);

		LOG.debug("QueryId:" + queryId + " viewName:" + viewName);

		/*
		 * exportSource: try { p2pDictionary.exportSource(viewName); } catch
		 * (PeerException ex) { LOG.error(ex.getMessage(), ex); }
		 */
	}

	public static void saveSmartDeviceConfig() {
		SmartDeviceConfiguration.setSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE,
				smartDeviceConfig);
	}

	public static void overWriteSmartDeviceConfigWith(SmartDeviceConfig config) {
		if (smartDeviceConfig == null) {
			initSmartDeviceConfig();
		}
		smartDeviceConfig.overwriteWith(config);
	}

	public static class SmartDeviceListener implements
			IPeerCommunicatorListener {
		@Override
		public void receivedMessage(IPeerCommunicator communicator,
				PeerID senderPeer, IMessage message) {
			if (message instanceof SmartDeviceRequestMessage) {
				// if a peer request information for this smart device, then
				// send out this information
				try {
					SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
					smartDeviceResponse.setSmartDevice(smartDevice);

					try {
						SmartHomeServerPlugIn.getPeerCommunicator().send(
								senderPeer, smartDeviceResponse);
					} catch (PeerCommunicationException ex) {
						LOG.error(ex.getMessage(), ex);
					}
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
				}
			} else if (message instanceof SmartDeviceResponseMessage) {
				SmartDeviceResponseMessage smartDeviceResponse = (SmartDeviceResponseMessage) message;

				if (!isLocalPeer(senderPeer)) {
					SmartDevice smartDevice = smartDeviceResponse
							.getSmartDevice();

					if (smartDevice != null) {
						smartDeviceDictionary.addSmartDevice(smartDevice);
					} else {
						LOG.debug("The smart device response is null!!!");
					}
				}
			} else if (message instanceof SmartDeviceMessage) {
				LOG.debug("receivedMessage instanceof SmartDeviceMessage");
			}
		}
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
		return p2pNetworkManager.getLocalPeerID().intern().toString();
	}

	private static String getLocalPeerName() {
		return p2pNetworkManager.getLocalPeerName().intern().toString();
	}

	private void initSmartDeviceDictionary() {
		if (smartDeviceDictionary == null) {
			smartDeviceDictionary = new SmartDeviceDictionary();
			smartDeviceDictionaryListener = new SmartDeviceDictionaryListener();
			smartDeviceDictionary.addListener(smartDeviceDictionaryListener);
		}
	}

	private static void initSmartDeviceConfig() {
		if (smartDeviceConfig == null) {
			smartDeviceConfig = new SmartDeviceConfig();
			// overwrite the initital object with information from saved file
			smartDeviceConfig.overwriteWith(SmartDeviceConfiguration
					.getSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE));
		}
	}

	public static class SmartDeviceDictionaryListener implements
			ISmartDeviceDictionaryListener {

		@Override
		public void smartDeviceAdded(SmartDeviceDictionary sender,
				SmartDevice newSmartDevice) {
			LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
					+ " processLogic(...");

			processLogic(newSmartDevice);
		}

		@Override
		public void smartDeviceRemoved(SmartDeviceDictionary sender,
				SmartDevice remoteSmartDevice) {

			LOG.debug("SmartHomeServerPlugIn smartDeviceRemoved: "
					+ smartDevice.getPeerID());

			if (isRunningLogicRule(remoteSmartDevice)) {
				removeAllLogicRules(remoteSmartDevice);
			}
		}

		@Override
		public void smartDeviceUpdated(SmartDeviceDictionary sender,
				SmartDevice smartDevice) {
			// LOG.debug("smartDeviceUpdated: " +
			// smartDevice.getPeerIDString());

			// TODO: Nachschauen was sich am SmartDevice geändert hat.
			// Falls Sensoren hinzugefügt oder entfernt wurden, dann müssen die
			// Logik-Regeln überprüft und anschließend ausgeführt oder entfernt
			// werden!

		}
	}

	private static void processLogic(SmartDevice newSmartDevice) {
		/*******************************
		 * TODO: Logic processing: 1. find matches
		 *******************************/

		String smartDeviceContextName = "";
		if (smartDevice == null) {
			return;
		} else {
			smartDeviceContextName = smartDevice.getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {
			for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
				for (String possibleActivityName : remoteSensor
						.getPossibleActivityNames()) {
					for (Actor localActor : smartDevice.getConnectedActors()) {
						if (localActor != null
								&& localActor.getPossibleActivityNames()
										.contains(possibleActivityName)) {

							addNeededSourceForImport(
									remoteSensor
											.getActivitySourceName(possibleActivityName),
									newSmartDevice.getPeerID());

							executeLogicRuleWhenPossibleAsync(
									possibleActivityName,
									(Sensor) remoteSensor, (Actor) localActor,
									newSmartDevice);
						}
					}
				}
			}
		} else {
			// The context does not matter:

		}

	}

	private static LinkedHashMap<String, String> sourcesNeededForImport;

	public static void addNeededSourceForImport(String activitySourceName,
			String peerIDString) {
		if (!getP2PDictionary().isImported(activitySourceName)) {
			if (sourcesNeededForImport == null) {
				sourcesNeededForImport = new LinkedHashMap<String, String>();
			}
			sourcesNeededForImport.put(activitySourceName, peerIDString);
		}
	}

	public static void removeAllLogicRules(SmartDevice remoteSmartDevice) {
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			// iterate backward:
			ListIterator<String> iterator = new ArrayList<String>(sensor
					.getQueryForActivityInterpreterQueries().keySet())
					.listIterator(sensor
							.getQueryForActivityInterpreterQueries().size());
			while (iterator.hasPrevious()) {
				String queryName = iterator.previous();

				stopQuery(queryName);
			}

			// iterate backward:
			ListIterator<String> iteratorRawValues = new ArrayList<String>(
					sensor.getQueriesForRawValues().keySet())
					.listIterator(sensor.getQueriesForRawValues().size());
			while (iteratorRawValues.hasPrevious()) {
				String queryName = iteratorRawValues.previous();

				stopQuery(queryName);
			}
		}
	}

	private static void stopQuery(String queryName) {
		LOG.debug("stop and removeQueryName:" + queryName);

		ServerExecutorService.getServerExecutor().removeQuery(queryName,
				SessionManagementService.getActiveSession());
	}

	public static boolean isSourceNameNeddedByLogicRule(String sourceName) {
		// TODO: check correctness

		for (Actor actor : smartDevice.getConnectedActors()) {
			for (String activity : actor.getPossibleActivityNames()) {
				try {
					for (Entry<String, String> map : actor
							.getLogicRuleOfActivity(activity).entrySet()) {
						String queryName = map.getKey();
						// String query = map.getValue();

						if (sourceName.equals(queryName)) {
							return true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static boolean isSourceNameUsingByLogicRule(String sourceName) {
		return getP2PDictionary().isSourceNameAlreadyInUse(sourceName);
	}

	public static boolean isRunningLogicRule(SmartDevice remoteSmartDevice) {
		// TODO: check correctness
		for (Sensor sensor : remoteSmartDevice.getConnectedSensors()) {
			for (Entry<String, String> entry : sensor
					.getQueryForActivityInterpreterQueries().entrySet()) {
				String queryName = entry.getKey();
				if (getP2PDictionary().isSourceNameAlreadyInUse(queryName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void executeLogicRuleWhenPossibleAsync(
			final String possibleActivityName, final Sensor sensor,
			final Actor actor, final SmartDevice smartDeviceWithSensor) {
		Thread t = new Thread(new Runnable() {
			boolean ruleExecuted = false;

			@Override
			public void run() {
				String message = "executeLogicRuleAsync activity:"
						+ possibleActivityName + " sensor:" + sensor.getName()
						+ " actor:" + actor.getName() + " smartDevice:"
						+ smartDeviceWithSensor.getPeerName();
				LOG.debug(message);

				while (!ruleExecuted) {
					try {
						Thread.sleep(2000);
					} catch (Exception ex) {
					}

					try {

						if (isLogicExecutionPossible(possibleActivityName,
								sensor, actor, smartDeviceWithSensor)) {

							executeLogicRuleNow(possibleActivityName, sensor,
									actor, smartDeviceWithSensor);
							ruleExecuted = true;
						} else {
							// LOG.debug("!isLogicExecutionPossible: " +
							// message);
							// TODO: import neccessary source:

						}
					} catch (Exception ex) {
						LOG.error(ex.getMessage(), ex);
					}
				}
			}
		});
		t.setName("Logic execution thread. smartDevice:"
				+ smartDeviceWithSensor + " activityName:"
				+ possibleActivityName + " sensor:" + sensor + " actor:"
				+ actor);
		t.setDaemon(true);
		t.start();
	}

	private static void executeLogicRuleNow(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDeviceWithSensor) {
		LOG.debug("executeLogicRule(" + possibleActivityName + "," + sensor
				+ "," + actor + "," + smartDeviceWithSensor.getPeerName() + ")");

		if (isLogicExecutionPossible(possibleActivityName, sensor, actor,
				smartDeviceWithSensor)) {

			LOG.debug("sourceIsImported run the actor logic script now:");

			try {
				actor.setActivitySourceName(sensor
						.getActivitySourceName(possibleActivityName));

				LinkedHashMap<String, String> logicRuleQueries = actor
						.getLogicRuleOfActivity(possibleActivityName);
				for (Entry<String, String> entry : logicRuleQueries.entrySet()) {
					String viewName = entry.getKey();
					String ruleQuery = entry.getValue();

					LOG.debug("executeLogicRule(" + possibleActivityName + ","
							+ sensor + "," + actor + ") viewName:" + viewName);

					executeQueryNow(viewName, ruleQuery);

				}
			} catch (Exception ex) {
				LOG.debug(ex.getMessage(), ex);
			}
		} else {
			String sName;
			if (sensor != null
					&& sensor.getActivitySourceName(possibleActivityName) != null) {
				sName = sensor.getActivitySourceName(possibleActivityName);
			} else {
				sName = "null";
			}

			LOG.debug("source is not imported or the peerID was not found, the actor logic script is not running! sourceName:"
					+ sName);
		}
	}

	private static boolean isLogicExecutionPossible(
			String possibleActivityName, Sensor sensor, Actor actor,
			SmartDevice smartDeviceWithSensor) {
		// TODO: überprüfen ob die Logic-Regel bereits ausgeführt wird/wurde!

		return p2pDictionary != null
				&& p2pDictionary.isImported(sensor.getRawSourceName())
				&& p2pDictionary.isImported(sensor
						.getActivitySourceName(possibleActivityName));
	}
}
