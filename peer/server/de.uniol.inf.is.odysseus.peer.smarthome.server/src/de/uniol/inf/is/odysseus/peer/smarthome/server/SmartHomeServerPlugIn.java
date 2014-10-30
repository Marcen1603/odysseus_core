package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.smarthome.ISmartDeviceDictionaryListener;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfig;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceConfigurationResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceDictionary;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.FieldDevice;
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
	private static SmartDeviceAdvertisementListener smartDeviceAdvertisementListener;

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
	}

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;

		smartDeviceAdvertisementListener = new SmartDeviceAdvertisementListener();
		p2pNetworkManager
				.addAdvertisementListener(smartDeviceAdvertisementListener);
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager
					.removeAdvertisementListener(smartDeviceAdvertisementListener);
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

	private static void initSmartDeviceForAdvertisement() {
		/*****************************************
		 * TODO: init smart device for advertisement
		 *****************************************/
		String peerIdString = getLocalPeerID();
		String cleanPeerID = peerIdString.replaceAll("[-+.^:,]", "");
		String peerName = p2pNetworkManager.getLocalPeerName().intern()
				.toString();

		//
		// 1. Instantiate Sensors:
		//
		// Temper1
		Temper1Sensor temper1 = new Temper1Sensor("temper1", "temper1",
				peerName, cleanPeerID);
		temper1.addPossibleActivityName("hot");
		//
		// GPIO_07 as input sensor
		RPiGPIOSensor gpioTaste7 = new RPiGPIOSensor("RPiGPIOTaster",
				"rpigpiotaster", peerName, cleanPeerID);
		gpioTaste7.addPossibleActivityName("Tasterbetaetigt");
		gpioTaste7.setInputPin("7");
		// gpioTaste11.setPinState("high");

		//
		// 2. instantiate SmartDevice:
		//
		smartDevice = new SmartDevice();
		smartDevice.setPeerID(getLocalPeerID());
		smartDevice.setSmartDevice(getSmartDeviceConfig());
		smartDevice.addConnectedFieldDevice(temper1);
		// smartDevice.addConnectedFieldDevice(gpioTaste7);

		//
		// 3. Execute queries:
		//
		executeSensorQueries(smartDevice);

		//
		// 4. SmartDevice is ready for advertisement now:
		//
		smartDevice.setReady(true);
	}

	private static void executeSensorQueries(SmartDevice smartDevice) {
		for (FieldDevice fieldDevice : smartDevice.getConnectedFieldDevices()) {
			if (fieldDevice instanceof Sensor) {
				Sensor sensor = (Sensor) fieldDevice;

				// stream with raw values of the sensor
				executeQuery(sensor.getRawSourceName(),
						sensor.getQueryForRawValues());

				// stream with participating activities of the sensor
				executeQuery(sensor.getActivitySourceName(),
						sensor.getQueryForActivityInterpreter());
			}
		}
	}

	private static void publishSmartDeviceAdvertisementAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForJxtaServicesProvider();
				waitForServerExecutorService();

				printLocalPeerID();

				LOG.error("publishSmartDeviceAdvertisementAsync started and will be executing in 5 sec.");

				/*
				 * // wait for SmartDevice while (smartDevice == null ||
				 * !smartDevice.isReady()) { try { Thread.sleep(200); } catch
				 * (InterruptedException e) { } }
				 */

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
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

					getJxtaServicesProvider().publish(smartDeviceAdv);
					getJxtaServicesProvider().remotePublish(smartDeviceAdv);
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}

			private void printLocalPeerID() {
				try {
					LOG.debug("SmartDevice PeerID: "
							+ SmartHomeServerPlugIn.p2pNetworkManager
									.getLocalPeerID().intern().toString());
				} catch (Exception ex) {
					LOG.debug("SmartDevice PeerID currently is null");
				}
			}
		});
		thread.setName("SmartHomeServerPlugIn SmartDeviceAdvertisement Thread");
		thread.setDaemon(true);
		thread.start();
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

	public static boolean isLocalPeer(PeerID peer) {
		if (SmartHomeServerPlugIn.getP2PNetworkManager() == null
				|| p2pNetworkManager.getLocalPeerID() == null
				|| SmartHomeServerPlugIn.p2pNetworkManager.getLocalPeerID()
						.intern() == null) {
			return false;
		} else if (peer == null || peer.intern() == null) {
			return false;
		}

		return isLocalPeer(peer.intern().toString());
	}

	public static boolean isLocalPeer(String peerID) {
		String str1 = SmartHomeServerPlugIn.p2pNetworkManager.getLocalPeerID()
				.intern().toString();

		if (str1 == null || peerID == null)
			return false;
		return str1.equals(peerID);
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

	public static class SmartDeviceAdvertisementListener implements
			IAdvertisementDiscovererListener {
		@Override
		public void advertisementDiscovered(Advertisement advertisement) {
			// LOG.debug("advertisementDiscovered AdvType:"+
			// advertisement.getAdvType());

			if (advertisement != null
					&& advertisement instanceof SmartDeviceAdvertisement
					&& advertisement.getAdvType().equals(
							SmartDeviceAdvertisement.getAdvertisementType())) {

				SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) advertisement;
				getSmartDeviceInformations(adv);
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
			//LOG.debug("peerID==null");
			return false;
		} else if (isLocalPeer(peerID.intern().toString())) {
			//LOG.debug("peerID is LocalPeer: " + peerID.intern().toString());
			return false;
		} else if (foundPeerIDs == null || peerID == null
				|| peerID.intern() == null
				|| peerID.intern().toString().isEmpty()) {
			//LOG.debug("peerID is null or empty");
			return false;
		} else if (!isFoundInFoundPeers(peerID)) {
			@SuppressWarnings("unused")
			String debugMessage = "is not in the foundPeerID's peerID:";
			
			try {
				debugMessage+="peerID: "
						+ peerID.intern().toString();
			} catch (Exception ex) {
				debugMessage+="peerID: null";
			}

			//LOG.debug(debugMessage);
			return false;
		} else {
			return true;
		}
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

			// Anfragen wieder entfernen:

			if (sourceName.equals("rpigpiosrc")) {

			} else if (sourceName.equals("raspberrygpiosrc")) {

			} else if (sourceName.equals("bananagpiosrc")) {

			} else if (sourceName.equals("rpigpiosrcbuttonautoexport")) {
				/*
				 * if(forwardButtonStateToLEDQueryID!=null){
				 * ServerExecutorService
				 * .getServerExecutor().removeQuery(forwardButtonStateToLEDQueryID
				 * , SessionManagementService.getActiveSession()); }
				 */
			}
		}

		@Override
		public void sourceExported(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceExported");
		}

		@Override
		public void sourceExportRemoved(IP2PDictionary sender,
				SourceAdvertisement advertisement, String sourceName) {
			LOG.debug("sourceExportRemoved");
		}
	}

	private static void executeQuery(String viewName, String query) {
		//TODO: only one query per execution!
		
		Collection<Integer> queryIDs = ServerExecutorService
				.getServerExecutor().addQuery(query, "OdysseusScript",
						SessionManagementService.getActiveSession(),
						"Standard", Context.empty());
		Integer queryId;
		try {
			if (queryIDs == null) {
				LOG.debug("queryIDs==null ");
				return;
			}

			if (!queryIDs.iterator().hasNext()) {
				LOG.debug("!queryIDs.iterator().hasNext()");
				return;
			}

			queryId = queryIDs.iterator().next();
			IPhysicalQuery physicalQuery = ServerExecutorService
					.getServerExecutor().getExecutionPlan()
					.getQueryById(queryId);
			ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
			logicalQuery.setName(viewName);
			logicalQuery.setParserId("P2P");
			logicalQuery.setUser(SessionManagementService.getActiveSession());
			logicalQuery.setQueryText("Exporting " + viewName);
		} catch (NoSuchElementException ex) {
			LOG.error(ex.getMessage(), ex);
		}

		/*
		 * try { p2pDictionary.exportSource(viewName); } catch (PeerException
		 * ex) { LOG.error(ex.getMessage(), ex); }
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
				/*
				 * SmartDeviceMessage smessage = (SmartDeviceMessage) message;
				 * 
				 * String testMessage = "Echo! from Server";
				 * 
				 * if (!isLocalPeer(senderPeer) &&
				 * !smessage.getText().equals(testMessage)) { try {
				 * SmartHomeServerPlugIn.getPeerCommunicator() .send(senderPeer,
				 * new SmartDeviceMessage(testMessage)); } catch
				 * (PeerCommunicationException e) { e.printStackTrace(); } }
				 */
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

		//printFoundPeerIDs(foundPeerIDsCopy);
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
		private boolean ruleHotIsExecuted=false;

		@Override
		public void smartDeviceAdded(SmartDeviceDictionary sender,
				SmartDevice smartDevice) {
			LOG.debug("smartDeviceAdded: " + smartDevice.getPeerIDString());

			/*******************************
			 * TODO: Logic processing:
			 *******************************/
			if (smartDevice.getContextName().equals("Office")) {
				for (FieldDevice fieldDevice : smartDevice
						.getConnectedFieldDevices()) {
					String rawSourceName = fieldDevice.getRawSourceName();
					@SuppressWarnings("unused")
					String activitySourceName = fieldDevice
							.getActivitySourceName();

					for (String possibleActivityName : fieldDevice
							.getPossibleActivityNames()) {

						// Rule 1:
						// run async?
						if (possibleActivityName.equals("Tasterbetaetigt")) {
							// Check for peerID...
							// same rule can run for different peers/sensors

							LOG.debug("Rule for activity:Tasterbetaetigt wurde erfüllt und wird nun ausgeführt.");

							// ActivitySource muss abgefragt werden,
							// wenn die Entität an der Aktivität teilnimmt
							// dann soll der Zustand des Aktors geändert werden
							// (Lampe an/aus)

							String viewName = "rpiTest";
							StringBuilder sb = new StringBuilder();
							sb.append("#PARSER PQL\n");
							// sb.append("#ADDQUERY\n");
							// sb.append("#QNAME Exporting " + viewName + "\n");
							sb.append("#RUNQUERY\n");
							sb.append("rpigpiosinkoutput = RPIGPIOSINK({sink='rpigpiosink', pin=11, pinstate='low'},"
									+ rawSourceName + ")\n");
							// sb.append(pqlGenerator.generatePQLStatement(rpiGPIOSinkAO));
							sb.append("\n");
							String scriptText = sb.toString();

							if (p2pDictionary != null
									&& p2pDictionary.isImported(rawSourceName)
									&& !p2pDictionary
											.isSourceNameAlreadyInUse(rawSourceName)
									&& foundPeerIDs.contains(smartDevice
											.getPeerID())) {
								LOG.debug("sourceIsImported run the actor logic script now:");
								executeQuery(viewName, scriptText);

							} else {

								LOG.debug("source is not imported or the peerID was not found, the actor logic script is not running!");
							}
						} else if (possibleActivityName.equals("hot")) {
							executeRuleHotAsync(fieldDevice);
						}

						// Rule2:
						// ...
					}
				}

			} // ... other contexts
		}

		private void executeRuleHotAsync(final FieldDevice fieldDevice) {

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!ruleHotIsExecuted) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
						
						if (p2pDictionary.isImported(fieldDevice
								.getActivitySourceName())) {
							executeRuleHot(fieldDevice);
						}
					}
				}
			});
			t.setName("execute rule hot thread.");
			t.setDaemon(true);
			t.start();
		}
		
		private void executeRuleHot(FieldDevice fieldDevice) {
			if(!ruleHotIsExecuted){
				ruleHotIsExecuted=true;
				
			LOG.debug("------------------------------------------------------------");
			LOG.debug("Rule for activity:hot wurde erfüllt und wird nun ausgeführt.");
			LOG.debug("FieldDevice name:"
					+ fieldDevice.getName());
			LOG.debug("FieldDevice rawSourceName:"
					+ fieldDevice.getRawSourceName());
			LOG.debug("FieldDevice activitySourceName:"
					+ fieldDevice.getActivitySourceName());
			LOG.debug("------------------------------------------------------------");

			// TODO: Auf Aktivität: "hot" lauschen, sobald diese
			// ausgeführt wird, soll eine 1 an LED gesendet
			// werden:

			// TODO: Anfrage ausführen um einen Aktor aufgrund
			// der Aktivität zu steuern.
			
			
			
			//Eine Senke für den Aktor anlegen:
			//out = aktivityToStateTransformator(aktivität:"hot",Entität:"Temper1",out:1 , temper1_activity_datenstrom)
			//RPiGPIO(pin=11, out)
			String gpio11ActorName = "gpio11Actor";
			@SuppressWarnings("unused")
			RPiGPIOActor gpio11Actor = new RPiGPIOActor();
			
			//1. Import data stream with activities of the field device: fieldDevice.getName()
			// and listen for the activity: "hot"
			String activityImportedName = getLocalPeerName()+"_"+fieldDevice.getName()+"_Activity_imported";
			StringBuilder activityImport = new StringBuilder();
			activityImport.append("#PARSER PQL\n");
			activityImport.append("#RUNQUERY\n");
			activityImport.append(activityImportedName+" := SELECT({\n");
			activityImport.append("    predicate=\"ActivityName = 'hot'\"\n");
			activityImport.append("    },\n");
			activityImport.append("    "+fieldDevice.getActivitySourceName()+"\n");
			activityImport.append(")\n");
			activityImport.append("\n");
			
			// 2. create configuration of the actor, how it will be set if the fieldDevice.getName() is participating at the activity: "hot"
			String actorConfigIfParticipateActivity = getLocalPeerName()+"_"+fieldDevice.getName()+"_Config";
			StringBuilder sbConfig = new StringBuilder();
			sbConfig.append("#PARSER PQL\n");
			sbConfig.append("#RUNQUERY\n");
			sbConfig.append("    "+actorConfigIfParticipateActivity+" := ACCESS({\n");
			sbConfig.append("    transport = 'activityconfiguration',\n");
			sbConfig.append("    source = '"+actorConfigIfParticipateActivity+"',\n");
			sbConfig.append("    datahandler = 'Tuple',\n");
			sbConfig.append("    wrapper = 'GenericPull',\n");
			sbConfig.append("    protocol='none',\n");
			sbConfig.append("    options=[\n");
			sbConfig.append("      ['entity', '1'],\n");
			sbConfig.append("      ['activity', 'hot']\n");
			sbConfig.append("    ],\n");
			sbConfig.append("    schema=[\n");
			sbConfig.append("      ['State', 'String'],\n");
			sbConfig.append("      ['ActivityName', 'String']\n");
			sbConfig.append("    ]\n");
			sbConfig.append("  }\n");
			sbConfig.append(")\n");
			sbConfig.append("\n");
			sbConfig.append("\n");
			
			
			String setConfigStreamName = getLocalPeerName()+"_"+fieldDevice.getName()+"_SetActor_"+gpio11ActorName;
			StringBuilder sbSetConfig = new StringBuilder();
			sbSetConfig.append("#PARSER PQL\n");
			sbSetConfig.append("#RUNQUERY\n");
			sbSetConfig.append(""+setConfigStreamName+" := RENAME({\n");
			sbSetConfig.append("        aliases = ['PinNumber', 'PinState']\n");                                                             
			sbSetConfig.append("    },\n");
			sbSetConfig.append("    PROJECT({\n");
			sbSetConfig.append("        attributes = ['EntityName', 'State']\n");                                                                                       
			sbSetConfig.append("    },\n");
			sbSetConfig.append("        JOIN({\n");
			sbSetConfig.append("            predicate = '"+activityImportedName+".ActivityName = "+actorConfigIfParticipateActivity+".ActivityName'\n");                                                                                                                                                                                        
			sbSetConfig.append("        },\n");
			sbSetConfig.append("        "+activityImportedName+",\n");
			sbSetConfig.append("        "+actorConfigIfParticipateActivity+"\n");
			sbSetConfig.append("        )\n");                                                                
			sbSetConfig.append("    )\n");                                        
			sbSetConfig.append(")\n");
			sbSetConfig.append("\n");
			
			
			
			//InputSchema: schema=[['PinNumber', 'String'],['PinState', 'String']] 
			String entitySetStateStreamName = "rpigpio11";
			StringBuilder sbEntitySetState = new StringBuilder();
			sbEntitySetState.append("#PARSER PQL\n");
			sbEntitySetState.append("#RUNQUERY\n");
			sbEntitySetState.append(""+entitySetStateStreamName+" := SENDER({\n");
			sbEntitySetState.append("                    sink='rpigpio',\n");
			sbEntitySetState.append("                    wrapper='GenericPush',\n");
			sbEntitySetState.append("                    transport='RPiGPIO',\n");
			sbEntitySetState.append("                    protocol='none',\n");
			sbEntitySetState.append("                    datahandler='Tuple',\n");
			sbEntitySetState.append("                    options=[\n");
			sbEntitySetState.append("                      ['GPIO.11.mode', 'OUT'],\n");
			sbEntitySetState.append("                      ['GPIO.11.pull', 'down']\n");
			sbEntitySetState.append("                    ]         \n");
			sbEntitySetState.append("                  },\n");
			sbEntitySetState.append("                  "+setConfigStreamName+"\n");
			sbEntitySetState.append("                )\n");



			if(p2pDictionary.isImported(fieldDevice.getActivitySourceName())){
				executeQuery(activityImportedName, activityImport.toString());
				executeQuery(actorConfigIfParticipateActivity, sbConfig.toString());
				executeQuery(setConfigStreamName, sbSetConfig.toString());
				executeQuery(entitySetStateStreamName, sbEntitySetState.toString());
			}else{
				LOG.debug("the source is currently not imported source:"+fieldDevice.getActivitySourceName()+"");
			}
		  }
		}

		@Override
		public void smartDeviceRemoved(SmartDeviceDictionary sender,
				SmartDevice smartDevice) {
			LOG.debug("SmartHomeServerPlugIn smartDeviceRemoved: "
					+ smartDevice.getPeerIDString());

			// TODO: wenn eine Logik-Regel aufgrund eines SmartDevice
			// ausgeführt wurde, dann muss diese nun wieder entfernt werden!!!

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
}
