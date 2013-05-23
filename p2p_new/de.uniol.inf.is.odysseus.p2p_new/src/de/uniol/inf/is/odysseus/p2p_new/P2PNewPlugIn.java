package de.uniol.inf.is.odysseus.p2p_new;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.EndpointService;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.SEVERE;
	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);

	private static final int PORT = new Random().nextInt(20000) + 10000;
	private static final String SUBGROUP_NAME = "Odysseus Peer Group";
	private static final PeerGroupID SUBGROUP_ID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, SUBGROUP_NAME.getBytes());
	private static final String DEFAULT_PEER_NAME = "OdysseusPeer";

	private static DiscoveryService discoveryService;
	private static ContentService contentService;
	private static PipeService pipeService;
	private static EndpointService endpointService;

	private static PeerGroup ownPeerGroup;
	private static PeerID ownPeerID;
	private static String ownPeerName;

	private NetworkManager manager;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		configureLogging(bundleContext.getBundle());

		ownPeerName = determinePeerName();
		ownPeerID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);
		
		final File conf = new File("." + System.getProperty("file.separator") + ownPeerName);
		NetworkManager.RecursiveDelete(conf);
		manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, ownPeerName, conf.toURI());
		manager.setPeerID(ownPeerID);

		configureNetwork(manager.getConfigurator(), ownPeerID, ownPeerName);

		final PeerGroup netPeerGroup = manager.startNetwork();
		ownPeerGroup = createSubGroup(netPeerGroup, SUBGROUP_ID, SUBGROUP_NAME);

		discoveryService = ownPeerGroup.getDiscoveryService();
		contentService = ownPeerGroup.getContentService();
		pipeService = ownPeerGroup.getPipeService();
		endpointService = ownPeerGroup.getEndpointService();

		registerAdvertisementTypes();

		LOG.debug("JXTA-Network started. Peer {} is in group '{}'", ownPeerName, ownPeerGroup);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		
		discoveryService = null;
		contentService = null;
		pipeService = null;

		manager.stopNetwork();
		LOG.debug("JXTA-Network stopped");
	}

	public static ContentService getContentService() {
		return contentService;
	}

	public static DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public static EndpointService getEndpointService() {
		return endpointService;
	}

	public static PeerGroup getOwnPeerGroup() {
		return ownPeerGroup;
	}

	public static PeerID getOwnPeerID() {
		return ownPeerID;
	}
	
	public static String getOwnPeerName() {
		return ownPeerName;
	}

	public static PipeService getPipeService() {
		return pipeService;
	}
	
	private static void configureNetwork(NetworkConfigurator configurator, PeerID peerID, String peerName) {
		configurator.setTcpPort(PORT);
		configurator.setTcpEnabled(true);
		configurator.setTcpIncoming(true);
		configurator.setTcpOutgoing(true);
		configurator.setUseMulticast(true);
		configurator.setPeerID(peerID);
		configurator.setName(peerName);
	}

	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		if (jxtaLogger != null) {
			jxtaLogger.setLevel(JXTA_LOG_LEVEL);
		} else {
			LOG.warn("Could not get JXTA-Logger for setting level to {}", JXTA_LOG_LEVEL);
		}
		jxtaLogger = java.util.logging.Logger.getLogger("net.jxta.impl.endpoint.router.EndpointRouter");
		jxtaLogger.setLevel(java.util.logging.Level.OFF);
		
		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}

	private static PeerGroup createSubGroup(PeerGroup parentPeerGroup, PeerGroupID subGroupID, String subGroupName) throws PeerGroupException, IOException, Exception {
//		return parentPeerGroup.newGroup(subGroupID, parentPeerGroup.getAllPurposePeerGroupImplAdvertisement(), subGroupName, "", true);
		return parentPeerGroup.newGroup(subGroupID, parentPeerGroup.getAllPurposePeerGroupImplAdvertisement(), subGroupName, "");
	}

	private static String determinePeerName() {
		String peerName = System.getProperty(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return peerName;
		}

		peerName = OdysseusConfiguration.get(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return peerName;
		}

		return DEFAULT_PEER_NAME + "_" + PORT;
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
	}
}
