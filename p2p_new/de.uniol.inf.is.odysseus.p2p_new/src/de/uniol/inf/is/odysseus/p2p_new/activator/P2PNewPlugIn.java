package de.uniol.inf.is.odysseus.p2p_new.activator;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.jxta.document.AdvertisementFactory;
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

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.sources.SourceAdvertisementInstantiator;

public class P2PNewPlugIn implements BundleActivator {

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";
	
	private static final String DEFAULT_PEER_NAME = "OdysseusPeer";
	private static final String DEFAULT_PEER_GROUP_NAME = "OdysseusPeerGroup";
	
	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final int PORT = new Random().nextInt(20000) + 10000;
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.SEVERE;
	private static final Logger LOG = LoggerFactory.getLogger(P2PNewPlugIn.class);

	private NetworkManager manager;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		configureLogging(bundleContext.getBundle());

		String ownPeerName = determinePeerName();
		PeerID ownPeerID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);
		P2PDictionary.setLocalPeerID(ownPeerID);
		P2PDictionary.setLocalPeerName(ownPeerName);
		
		final File conf = new File("." + System.getProperty("file.separator") + ownPeerName);
		NetworkManager.RecursiveDelete(conf);
		manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, ownPeerName, conf.toURI());

		configureNetwork(manager.getConfigurator(), ownPeerID, ownPeerName);

		final PeerGroup netPeerGroup = manager.startNetwork();
		final String peerGroupName = determinePeerGroupName();
		final PeerGroupID peerGroupID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, peerGroupName.getBytes());
		final PeerGroup localPeerGroup = createSubGroup(netPeerGroup, peerGroupID, peerGroupName);
		P2PDictionary.setLocalPeerGroup(localPeerGroup);

		registerAdvertisementTypes();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		manager.stopNetwork();
		LOG.debug("JXTA-Network stopped");
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
		return parentPeerGroup.newGroup(subGroupID, parentPeerGroup.getAllPurposePeerGroupImplAdvertisement(), subGroupName, "");
	}

	private static void registerAdvertisementTypes() {
		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisementInstantiator());
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

		return DEFAULT_PEER_NAME + "_" + System.getProperty("user.name");
	}
	
	private static String determinePeerGroupName() {
		String peerGroupName = System.getProperty(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		peerGroupName = OdysseusConfiguration.get(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		return DEFAULT_PEER_GROUP_NAME + "_" + System.getProperty("user.name");
	}
}
