package de.uniol.inf.is.odysseus.p2p_new.nogui;

import net.jxta.platform.NetworkManager;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;

public class P2PActivatorThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(P2PActivatorThread.class);

	private static final String PEER_NAME_SYS_PROPERTY = "peer.name";
	private static final String PEER_GROUP_NAME_SYS_PROPERTY = "peer.group.name";
	
	private static final String DEFAULT_PEER_NAME = "OdysseusPeer";
	private static final String DEFAULT_PEER_GROUP_NAME = "OdysseusPeerGroup";
	
	private NetworkManager manager;
	
	public P2PActivatorThread( BundleContext bundleContext ) {
		Preconditions.checkNotNull(bundleContext, "Bundle context must not be null!");
		
		setName("P2P Activator Thread");
		setDaemon(true);
	}
	
	@Override
	public void run() {
		LOG.debug("Starting p2p activator thread");
		
		waitForP2PNetworkManager();
		
		String ownPeerName = determinePeerName();
		String ownPeerGroupName = determinePeerGroupName();
		
		LOG.debug("Setting network manager data to: peerName = {}, groupName = {}", ownPeerName, ownPeerGroupName);
		P2PNetworkManagerService.get().setLocalPeerName(ownPeerName);
		P2PNetworkManagerService.get().setLocalPeerGroupName(ownPeerGroupName);
		
		try {
			LOG.debug("Starting p2p network manager");
			P2PNetworkManagerService.get().start();
		} catch (P2PNetworkException ex) {
			LOG.error("Could not start p2p network automatically", ex);
		}
	}
	
	private static void waitForP2PNetworkManager() {
		LOG.debug("Waiting for p2p network manager");
		while( !P2PNetworkManagerService.isBound() ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
		}
		LOG.debug("P2P network manager bound!");
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
	
	public NetworkManager getNetworkManager() {
		return manager;
	}
}
