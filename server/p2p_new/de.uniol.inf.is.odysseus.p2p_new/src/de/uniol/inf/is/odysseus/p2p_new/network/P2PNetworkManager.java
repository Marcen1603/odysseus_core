package de.uniol.inf.is.odysseus.p2p_new.network;

import java.io.File;
import java.net.URI;

import net.jxta.id.IDFactory;
import net.jxta.impl.cm.CacheManager;
import net.jxta.impl.peergroup.StdPeerGroup;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;

public final class P2PNetworkManager implements IP2PNetworkManager {

	private static final Logger LOG = LoggerFactory.getLogger(P2PNetworkManager.class);

	private static P2PNetworkManager instance;
	
	private String peerName;
	private int port;
	private String groupName;
	private PeerID peerID;
	private PeerGroup peerGroup;
	
	private boolean started;
	private NetworkManager manager;
	private URI rendevousPeerURI;
	private boolean isRendevousPeer;
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
		
		if( started ) {
			stop();
		}
	}
	
	public static P2PNetworkManager getInstance() {
		return instance;
	}
	
	public static boolean isActivated() {
		return instance != null;
	}
	
	@Override
	public void setLocalPeerName( String name ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Peer name must not be null or empty!");
		
		peerName = name;
	}
	
	@Override
	public String getLocalPeerName() {
		return peerName;
	}
	
	@Override
	public void setLocalPeerGroupName( String name ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Peer group name must not be null or empty!");
		
		groupName = name;
	}
	
	@Override
	public String getLocalPeerGroupName() {
		return groupName;
	}
	
	@Override
	public void setRendevousPeerAddress( URI addressURI ) {
		rendevousPeerURI = addressURI;
	}
	
	@Override
	public URI getRendevousPeerAddress() {
		return rendevousPeerURI;
	}
	
	@Override
	public boolean isRendevousPeer() {
		return isRendevousPeer;
	}
	
	@Override
	public void setRendevousPeer( boolean isRendevousPeer ) {
		this.isRendevousPeer = isRendevousPeer;
	}
	
	@Override
	public int getPort() {
		return port;
	}
	
	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void start() throws P2PNetworkException {
		Preconditions.checkState(!started, "P2P network already started");
		
		LOG.debug("Starting p2p network. peerName = {}, groupName = {}", peerName, groupName);
		
		final File conf = new File(OdysseusConfiguration.getHomeDir() + "peers" + File.separator + peerName);
		NetworkManager.RecursiveDelete(conf);
		
		peerID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);
		
		try {
			manager = new NetworkManager(determineConfigMode(), peerName, conf.toURI());
	
			configureNetwork(manager.getConfigurator(), peerID, peerName, port, rendevousPeerURI);
	
			PeerGroup netPeerGroup = manager.startNetwork();
			
//			if( rendevousPeerURI != null ) {
//				LOG.debug("Waiting for rendevous connection...");
//				if( !manager.waitForRendezvousConnection(10000) ) {
//					throw new P2PNetworkException("Could not connect to rendevous peer " + rendevousPeerURI);
//				}
//				LOG.debug("Sucessful connected to rendevous peer");
//			}
			
			PeerGroupID peerGroupID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, groupName.getBytes());
			
			peerGroup = createSubGroup(netPeerGroup, peerGroupID, groupName);
			if( !isRendevousPeer && rendevousPeerURI == null ) {
		        CacheManager cacheManager = ((StdPeerGroup) peerGroup).getCacheManager();
		        cacheManager.setTrackDeltas(false);
			}

			if( isRendevousPeer ) {
				LOG.debug("Starting rendevous");
				peerGroup.getRendezVousService().startRendezVous();				
			}
			
	        started = true;
			LOG.debug("P2P network started");
			
			fireStartEvent();
	
		} catch( Exception ex ) {
			manager = null;
			
			throw new P2PNetworkException("Could not initialize/connect p2p network", ex);
		}		
	}

	private ConfigMode determineConfigMode() {
		if( isRendevousPeer ) {
			return NetworkManager.ConfigMode.RENDEZVOUS;
		} 
//		if( rendevousPeerURI != null ) {
//			return NetworkManager.ConfigMode.EDGE;
//		}
		return NetworkManager.ConfigMode.ADHOC;
	}
	
	private void fireStartEvent() {
		ImmutableCollection<IP2PNetworkListener> listeners = P2PNetworkListenerRegistry.getInstance().getAllListeners();
		for( IP2PNetworkListener listener : listeners ) {
			try {
				listener.networkStarted(this);
			} catch( Throwable t ) {
				LOG.error("Exception in p2p network listener", t);
			}
		}
	}

	private static void configureNetwork(NetworkConfigurator configurator, PeerID peerID, String peerName, int port, URI rdvAddress) {
		configurator.setTcpPort(port);
		configurator.setTcpEnabled(true);
		configurator.setTcpIncoming(true);
		configurator.setTcpOutgoing(true);
		configurator.setUseMulticast(true);
		configurator.setPeerID(peerID);
		configurator.setName(peerName);
		if( rdvAddress != null ) {
			configurator.addSeedRendezvous(rdvAddress);
		}
	}

	private static PeerGroup createSubGroup(PeerGroup parentPeerGroup, PeerGroupID subGroupID, String subGroupName) throws Exception {
		return parentPeerGroup.newGroup(subGroupID, parentPeerGroup.getAllPurposePeerGroupImplAdvertisement(), subGroupName, "");
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}
	
	@Override
	public void stop() {
		Preconditions.checkState(started, "P2P network already stopped!");
		
		LOG.debug("Stopping p2p network");
		if( manager != null ) {
			manager.stopNetwork();
			manager = null;
		}
		
		started = false;
		LOG.debug("P2P network stopped");
		
		fireStopEvent();
	}
	
	private void fireStopEvent() {
		ImmutableCollection<IP2PNetworkListener> listeners = P2PNetworkListenerRegistry.getInstance().getAllListeners();
		for( IP2PNetworkListener listener : listeners ) {
			try {
				listener.networkStopped(this);
			} catch( Throwable t ) {
				LOG.error("Exception in p2p network listener", t);
			}
		}
	}
	
	@Override
	public PeerID getLocalPeerID() {
		return peerID;
	}
	
	@Override
	public PeerGroup getLocalPeerGroup() {
		return peerGroup;
	}
	
	@Override
	public PeerGroupID getLocalPeerGroupID() {
		return peerGroup.getPeerGroupID();
	}
}
