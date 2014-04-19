package de.uniol.inf.is.odysseus.peer.server;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Random;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;

public class PeerServerPlugIn implements BundleActivator {
	
	private static final Logger LOG = LoggerFactory.getLogger(PeerServerPlugIn.class);

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";
	private static final String RENDEVOUS_ADDRESS_SYS_PROPERTY = "peer.rdv.address";
	private static final String RENDEVOUS_ACTIVE_SYS_PROPERTY = "peer.rdv.active";
	private static final String PEER_PORT_SYS_PROPERTY = "peer.port";
	private static final String ADDRESS_PREFIX = "tcp://";

	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		
		try {
			startP2PNetwork();
		} catch (P2PNetworkException e) {
			LOG.error("Could not start network", e);
		}
	}
	
	private static void startP2PNetwork() throws P2PNetworkException {
		Integer peerPort = determinePeerPort();
		String peerName = determinePeerName();
		String groupName = determinePeerGroupName();
		String rendevousAddress = determineRendevousAddress();
		Boolean rendevousActive = determineRendevousActive();

		LOG.debug("Starting p2pNetwork with following settings");
		LOG.debug("Peername = {}", peerName);
		LOG.debug("Peergroup = {}", groupName );
		LOG.debug("RendevousAddress = {}", rendevousAddress);
		LOG.debug("RendevousActive = {}", rendevousActive);

		p2pNetworkManager.setLocalPeerGroupName(groupName);
		p2pNetworkManager.setLocalPeerName(peerName);
		p2pNetworkManager.setRendevousPeerAddress(toURI(rendevousAddress));
		p2pNetworkManager.setRendevousPeer(rendevousActive);
		p2pNetworkManager.setPort(peerPort);
		
		p2pNetworkManager.start();
	}
	
	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			if( p2pNetworkManager.isStarted() ) {
				p2pNetworkManager.stop();
			}
			
			p2pNetworkManager = null;
		}
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}


	private static URI toURI(String address) {
		if( Strings.isNullOrEmpty(address)) {
			return null;
		}
		
		try {
			return new URI(ADDRESS_PREFIX + address);
		} catch (URISyntaxException e) {
			LOG.error("Could not transform address '" + ADDRESS_PREFIX + address + "' to URI", e);
			return null;
		}
	}


	private static String determinePeerName() {
		String peerName = System.getProperty(PEER_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerName)) {
			return peerName;
		}

		Optional<String> optPeer = PeerConfiguration.get(PEER_NAME_SYS_PROPERTY);
		if (optPeer.isPresent()) {
			return optPeer.get();
		}

		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "OdysseusPeer";
		}
	}

	private static String determinePeerGroupName() {
		String peerGroupName = System.getProperty(PEER_GROUP_NAME_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(peerGroupName)) {
			return peerGroupName;
		}

		Optional<String> optPeerGroupName = PeerConfiguration.get(PEER_GROUP_NAME_SYS_PROPERTY);
		if( optPeerGroupName.isPresent() ) {
			return optPeerGroupName.get();
		}
		
		return "OdysseusGroup";
	}

	private static String determineRendevousAddress() {
		String rendevousAddress = System.getProperty(RENDEVOUS_ADDRESS_SYS_PROPERTY);
		if (!Strings.isNullOrEmpty(rendevousAddress)) {
			return rendevousAddress;
		}

		Optional<String> optRendevousAddress = PeerConfiguration.get(RENDEVOUS_ADDRESS_SYS_PROPERTY);
		if (optRendevousAddress.isPresent()) {
			return optRendevousAddress.get();
		}

		return "";
	}

	private static Boolean determineRendevousActive() {
		try {
			String rendevousActive = System.getProperty(RENDEVOUS_ACTIVE_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(rendevousActive)) {
				return Boolean.valueOf(rendevousActive);
			}

			Optional<String> optRendevousActive = PeerConfiguration.get(RENDEVOUS_ACTIVE_SYS_PROPERTY);
			if (optRendevousActive.isPresent()) {
				return Boolean.valueOf(optRendevousActive.get());
			}
		} catch (Throwable t) {
			LOG.error("Could not determine if this peer is a rendevous peer", t);
		}
		return false;
	}

	private static Integer determinePeerPort() {
		try {
			String portString = System.getProperty(PEER_PORT_SYS_PROPERTY);
			if (!Strings.isNullOrEmpty(portString)) {
				return Integer.valueOf(portString);
			}
	
			Optional<String> optPortString = PeerConfiguration.get(PEER_PORT_SYS_PROPERTY);
			if (optPortString.isPresent()) {
				return Integer.valueOf(optPortString.get());
			}
		} catch( Throwable t ) {
			LOG.error("Could not determine port", t);
		}

		return determineRandomPort();
	}

	private static int determineRandomPort() {
		return new Random().nextInt(20000) + 10000;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if( p2pNetworkManager != null && p2pNetworkManager.isStarted() ) {
			p2pNetworkManager.stop();
		}
	}

}
