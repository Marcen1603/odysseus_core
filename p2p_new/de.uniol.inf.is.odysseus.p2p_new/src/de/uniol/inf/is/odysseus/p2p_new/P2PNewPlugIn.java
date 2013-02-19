package de.uniol.inf.is.odysseus.p2p_new;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.impl.DataSourcePublisherThread;

public class P2PNewPlugIn implements BundleActivator {

	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.OFF;
	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);

	private static final int PORT = new Random().nextInt(20000) + 10000;
	private static final String PEER_NAME = "Odysseus Peer " + PORT;
	private static final String SUBGROUP_NAME = "Odysseus Peer Group";
	private static final PeerGroupID SUBGROUP_ID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, SUBGROUP_NAME.getBytes());
	private static final PeerID PEER_ID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID, PEER_NAME.getBytes());

	private static DiscoveryService discoveryService;
	private static ContentService contentService;
	private static PeerGroup ownPeerGroup;
	
	private static IDataDictionary dataDictionary;
	private static ISessionManagement sessionManagement;
	private static ISession activeSession;

	private NetworkManager manager;
	private DataSourcePublisherThread dataSourcePublisherThread;

	public void start(BundleContext bundleContext) throws Exception {
		configureLogging(bundleContext.getBundle());

		File conf = new File("." + System.getProperty("file.separator") + PEER_NAME);
		manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, PEER_NAME, conf.toURI());

		configureNetwork(manager.getConfigurator(), PEER_ID);

		PeerGroup netPeerGroup = manager.startNetwork();
		ownPeerGroup = createSubGroup(netPeerGroup, SUBGROUP_ID, SUBGROUP_NAME);

		discoveryService = ownPeerGroup.getDiscoveryService();
		contentService = ownPeerGroup.getContentService();
		
		LOG.debug("JXTA-Network started. Peer {} is in group '{}'", PEER_NAME, ownPeerGroup);
	}

	public void stop(BundleContext bundleContext) throws Exception {	
		discoveryService = null;

		manager.stopNetwork();
		LOG.debug("JXTA-Network stopped");
	}

	public final void bindDataDictionary(IDataDictionary dd) {
		dataDictionary = dd;
		dataSourcePublisherThread = new DataSourcePublisherThread(dataDictionary, discoveryService);
		dataSourcePublisherThread.start();

		LOG.info("Data dictionary bound: {}", dd);
	}

	public final void unbindDataDictionary(IDataDictionary dd) {
		if (dd == dataDictionary) {
			dataSourcePublisherThread.stopRunning();
			dataDictionary = null;

			LOG.info("Data dictionary unbound: {}", dd);
		}
	}
	
	public final void bindSessionManagement( ISessionManagement sm ) {
		sessionManagement = sm;
		LOG.info("Session management bound: {}", sm);

		activeSession = sessionManagement.login("System", "manager".getBytes());
		LOG.info("Got session for user System");
	}
	
	public final void unbindSessionManagement( ISessionManagement sm ) {
		if( sm == sessionManagement ) {
			sessionManagement = null;
			LOG.info("Session management unbound: {}", sm);
		}
	}
	
	public static IDataDictionary getDataDictionary() {
		return dataDictionary;
	}
	
	public static ISessionManagement getSessionManagement() {
		return sessionManagement;
	}
	
	public static ISession getActiveSession() {
		return activeSession;
	}

	public static DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public static ContentService getContentService() {
		return contentService;
	}

	public static PeerGroup getOwnPeerGroup() {
		return ownPeerGroup;
	}

	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		if (jxtaLogger != null) {
			jxtaLogger.setLevel(JXTA_LOG_LEVEL);
		} else {
			LOG.warn("Could not get JXTA-Logger for setting level to {}", JXTA_LOG_LEVEL);
		}
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}

	private static void configureNetwork(NetworkConfigurator configurator, PeerID peerID) throws IOException {
		configurator.setTcpPort(PORT);
		configurator.setTcpEnabled(true);
		configurator.setTcpIncoming(true);
		configurator.setTcpOutgoing(true);
		configurator.setUseMulticast(true);
		configurator.setPeerID(peerID);
	}

	private static PeerGroup createSubGroup(PeerGroup parentPeerGroup, PeerGroupID subGroupID, String subGroupName) throws PeerGroupException, IOException, Exception {
		return parentPeerGroup.newGroup(subGroupID, parentPeerGroup.getAllPurposePeerGroupImplAdvertisement(), subGroupName, "");
	}

}
