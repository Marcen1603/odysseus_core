package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.RPiGPIOSensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Temper1Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement.SmartDeviceAdvertisementInstantiator;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.peer.smarthome.server.service.SessionManagementService;
import de.uniol.inf.is.odysseus.core.collection.Context;

public class SmartHomeServerPlugIn implements BundleActivator,
		ISmartDeviceDictionaryListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static Bundle bundle;
	public static final String NO_P2P_CONNECTION_TEXT = "<no p2p connection>";
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static SmartDeviceConfigurationListener smartDeviceConfigurationListener;
	private static SmartDeviceAdvertisementListener smartDeviceAdvertisementListener;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static P2PDictionaryListener p2pDictionaryListener;
	private static IPQLGenerator pqlGenerator;

	// SmartDevice:
	public static final String SMART_DEVICE_CONFIG_FILE = "odysseusSmartDevice.conf";
	private static SmartDeviceConfig smartDeviceConfig;
	private static SmartDeviceDictionary smartDeviceDictionary;
	private static SmartDevice smartDevice;
	private static SmartDeviceListener smartDeviceListener;

	// private static SmartDeviceDictionaryListener
	// smartDeviceDictionaryListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		initSmartDeviceConfig();
		initSmartDeviceDictionary();

		registerAdvertisementTypes();

		bundle = bundleContext.getBundle();
	}

	private static void initSmartDevice() {
		String peerIdString = p2pNetworkManager.getLocalPeerID().intern().toString();
		String cleanPeerID = peerIdString.replaceAll("[-+.^:,]","");
		
		Sensor temper0 = new Temper1Sensor("Temper0", "temper0source");
		Sensor temper1 = new Temper1Sensor("Temper1", "temper1source");
		RPiGPIOSensor gpioTaste11 = new RPiGPIOSensor("RPiGPIOTaster", "rpigpiotastersource_"+cleanPeerID);
		//TODO:...
		gpioTaste11.addPossibleActivityName("Tasterbetaetigt");
		gpioTaste11.setPin("11");
		//gpioTaste11.setPinState("high");
		
		smartDevice = new SmartDevice();
		smartDevice.setPeerID(p2pNetworkManager.getLocalPeerID().intern()
				.toString());
		smartDevice.setSmartDevice(getSmartDeviceConfig());
		smartDevice.addConnectedFieldDevice(temper0);
		smartDevice.addConnectedFieldDevice(temper1);
		smartDevice.addConnectedFieldDevice(gpioTaste11);

		executeQueryAsync(temper0.getRawSourceName(),
				temper0.getQueryForRawValues());
		executeQueryAsync(temper1.getRawSourceName(),
				temper1.getQueryForRawValues());
		
		executeQueryAsync(gpioTaste11.getRawSourceName(), gpioTaste11.getQueryForRawValues());
	}

	private void initSmartDeviceDictionary() {
		smartDeviceDictionary = new SmartDeviceDictionary();
		smartDeviceDictionary.addListener(this);
	}

	private static void initSmartDeviceConfig() {
		if (smartDeviceConfig == null) {
			smartDeviceConfig = new SmartDeviceConfig();
			smartDeviceConfig.overwriteWith(SmartDeviceConfiguration
					.getSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
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

		showActualImportedSourcesAsync();
	}

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
	}

	private static void publishSmartDeviceAdvertisementAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForJxtaServicesProvider();
				waitForServerExecutorService();

				if (getP2PNetworkManager() != null
						&& getJxtaServicesProvider() != null) {
					LOG.debug("publishSmartDeviceAdvertisementAsync():");

					try {

						initSmartDevice();

						SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) AdvertisementFactory
								.newAdvertisement(SmartDeviceAdvertisement
										.getAdvertisementType());
						adv.setID(IDFactory.newPipeID(getP2PNetworkManager()
								.getLocalPeerGroupID()));
						adv.setPeerID(p2pNetworkManager.getLocalPeerID());
						// adv.setSmartDevice(smartDevice);

						getJxtaServicesProvider().publish(adv);
						getJxtaServicesProvider().remotePublish(adv);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			private void waitForJxtaServicesProvider() {
				while (getJxtaServicesProvider() == null
						|| !getJxtaServicesProvider().isActive()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}

		});
		thread.setName("SmartHomeServerPlugIn SmartDeviceAdvertisement Thread");
		thread.setDaemon(true);
		thread.start();
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

	public static boolean isLocalPeer(PeerID peer) {
		if (SmartHomeServerPlugIn.getP2PNetworkManager() == null
				|| SmartHomeServerPlugIn.getP2PNetworkManager()
						.getLocalPeerName() == null
				|| SmartHomeServerPlugIn.getP2PNetworkManager()
						.getLocalPeerName().intern() == null) {
			return false;
		} else if (peer == null || peer.intern() == null) {
			return false;
		}

		String str1 = SmartHomeServerPlugIn.getP2PNetworkManager()
				.getLocalPeerName().intern().toString();
		String str2 = peer.intern().toString();

		if (str1 == null || str2 == null)
			return false;
		return str1.equals(str2);
	}

	public static SmartDeviceConfig getSmartDeviceConfig() {
		initSmartDeviceConfig();
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
			if (advertisement != null
					&& advertisement instanceof SmartDeviceAdvertisement
					&& advertisement.getAdvType().equals(
							SmartDeviceAdvertisement.getAdvertisementType())) {

				SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) advertisement;
				getSmartDeviceInformations(adv);
			}
		}

		private void getSmartDeviceInformations(SmartDeviceAdvertisement adv) {
			// LOG.debug("getSmartDeviceInformations advPeerID:"+adv.getPeerID().intern().toString());

			if(adv==null){
				LOG.debug("getSmartDeviceInformations adv==null");
				return;
			}
			
			try {
				SmartDeviceRequestMessage smartDevRequest = new SmartDeviceRequestMessage(
						"request");

				SmartHomeServerPlugIn.getPeerCommunicator().send(
						adv.getPeerID(), smartDevRequest);
			} catch (PeerCommunicationException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		@Override
		public void updateAdvertisements() {
			// System.out.println("SmartHomeServerPlugIn updateAdvertisements");

		}
	}

	public static class P2PDictionaryListener implements IP2PDictionaryListener {
		// private Integer forwardButtonStateToLEDQueryID;

		P2PDictionaryListener() {
			// /LOG.debug("P2PDictionaryListener()");
		}

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

			// Wenn der Name der Quelle "rpigpiosrc" lautet, dann starte
			// Anfrage:
			// #PARSER PQL
			// #RUNQUERY
			// testSinkOutput = RPIGPIOSINK({sink='rpigpiosink',
			// pin=7},rpigpiosrc)

			if (advertisement.isLocal()) {
				LOG.debug("advertisement.isLocal()");
				return;
			}

			if (sourceName.equals("rpigpiosrc")) {

			} else if (sourceName.equals("raspberrygpiosrc")) {
				LOG.debug("raspberrygpiosrc");

			} else if (sourceName.equals("bananagpiosrc")) {

			} else if (sourceName.equals("rpigpiosrcbuttonautoexport")) {
				// IPQLGenerator
				// getPQLGenerator().generatePQLStatement();

				// RPiGPIOSinkAO rpiGPIOSinkAO = new RPiGPIOSinkAO();
				// rpiGPIOSinkAO.set

				// ILogicalOperator test = new ILogicalOperator();

				String viewName = "rpiTest";

				StringBuilder sb = new StringBuilder();
				sb.append("#PARSER PQL\n");
				// sb.append("#ADDQUERY\n");
				// sb.append("#QNAME Exporting " + viewName + "\n");
				sb.append("#RUNQUERY\n");
				sb.append("rpigpiosinkoutput = RPIGPIOSINK({sink='rpigpiosink', pin=7},rpigpiosrcbuttonautoexport)\n");
				// sb.append(pqlGenerator.generatePQLStatement(rpiGPIOSinkAO));
				sb.append("\n");
				String scriptText = sb.toString();

				executeQueryAsync(viewName, scriptText);
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

	static Integer onlyOneExecution = new Integer(1);

	private static void executeQueryAsync(final String viewName,
			final String query) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForServerExecutorService();
				waitForPQLGenerator();

				synchronized (onlyOneExecution) {
					executeQuery(viewName, query);
				}
			}

			private void executeQuery(String viewName, String query) {
				Collection<Integer> queryIDs = ServerExecutorService
						.getServerExecutor().addQuery(query, "OdysseusScript",
								SessionManagementService.getActiveSession(),
								"Standard", Context.empty());
				Integer queryId;
				try {
					queryId = queryIDs.iterator().next();
					IPhysicalQuery physicalQuery = ServerExecutorService
							.getServerExecutor().getExecutionPlan()
							.getQueryById(queryId);
					ILogicalQuery logicalQuery = physicalQuery
							.getLogicalQuery();
					logicalQuery.setName(viewName);
					logicalQuery.setParserId("P2P");
					logicalQuery.setUser(SessionManagementService
							.getActiveSession());
					logicalQuery.setQueryText("Exporting " + viewName);
				} catch (NoSuchElementException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		});
		t.setName("SmartHomeServerPlugIn execute query thread");
		t.setDaemon(true);
		t.start();

	}

	public static void saveSmartDeviceConfig() {
		SmartDeviceConfiguration.setSmartDeviceConfig(SMART_DEVICE_CONFIG_FILE,
				smartDeviceConfig);
	}

	public static void overWriteSmartDeviceConfigWith(SmartDeviceConfig config) {
		initSmartDeviceConfig();
		smartDeviceConfig.overwriteWith(config);
	}

	public static class SmartDeviceListener implements
			IPeerCommunicatorListener {
		@Override
		public void receivedMessage(IPeerCommunicator communicator,
				PeerID senderPeer, IMessage message) {
			if (message instanceof SmartDeviceRequestMessage) {
				try {
					SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
					smartDeviceResponse.setSmartDevice(smartDevice);

					try {
						SmartHomeServerPlugIn.getPeerCommunicator().send(
								senderPeer, smartDeviceResponse);
					} catch (PeerCommunicationException e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (message instanceof SmartDeviceResponseMessage) {
				SmartDeviceResponseMessage smartDeviceResponse = (SmartDeviceResponseMessage) message;

				if (!isLocalPeer(senderPeer)) {
					SmartDevice smartDevice = smartDeviceResponse
							.getSmartDevice();

					if (smartDevice != null) {
						smartDeviceDictionary.addSmartDevice(smartDevice);
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

	
	boolean runningRule1=false;
	/*
	 * 
	 * SmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDeviceDictionary sender,
			SmartDevice smartDevice) {
		LOG.debug("smartDeviceAdded: " + smartDevice.getPeerIDString());

		if(smartDevice.getContextName().equals("Office")){
			for(FieldDevice fieldDevice: smartDevice.getConnectedFieldDevices()){
				String rawSourceName = fieldDevice.getRawSourceName();
				@SuppressWarnings("unused")
				String activitySourceName = fieldDevice.getActivitySourceName();
				
				for(String activityName : fieldDevice.getPossibleActivityNames()){
					String possibleActivityName = activityName;
					
					
					//Rule 1:
					if(possibleActivityName.equals("Tasterbetaetigt")){
						//Check for peerID...
						// same rule can run for different peers/sensors
						
						if(runningRule1){
							//return;
						}else{
							runningRule1 = true;
						}
						
						LOG.debug("Rule1 getroffen und wird ausgeführt.");
						
						String viewName = "rpiTest";

						StringBuilder sb = new StringBuilder();
						sb.append("#PARSER PQL\n");
						// sb.append("#ADDQUERY\n");
						// sb.append("#QNAME Exporting " + viewName + "\n");
						sb.append("#RUNQUERY\n");
						sb.append("rpigpiosinkoutput = RPIGPIOSINK({sink='rpigpiosink', pin=7, pinstate='high'},"+rawSourceName+")\n");
						// sb.append(pqlGenerator.generatePQLStatement(rpiGPIOSinkAO));
						sb.append("\n");
						String scriptText = sb.toString();

						executeQueryAsync(viewName, scriptText);
						
						
					}
					
					//Rule2:
					//...
				}
			}

			
		} //... other contexts
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceDictionary sender,
			SmartDevice smartDevice) {
		LOG.debug("smartDeviceRemoved: " + smartDevice.getPeerIDString());

		
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceDictionary sender,
			SmartDevice smartDevice) {
		// LOG.debug("smartDeviceUpdated: " + smartDevice.getPeerIDString());

	}
}
