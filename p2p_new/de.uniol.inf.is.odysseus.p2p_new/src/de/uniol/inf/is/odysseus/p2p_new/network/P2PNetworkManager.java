package de.uniol.inf.is.odysseus.p2p_new.network;

import java.io.File;
import java.util.Random;

import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;

public final class P2PNetworkManager implements IP2PNetworkManager {

	private static final Logger LOG = LoggerFactory.getLogger(P2PNetworkManager.class);
	private static final int PORT = new Random().nextInt(20000) + 10000;
	
	private static P2PNetworkManager instance;
	
	private String peerName;
	private String groupName;
	private PeerID peerID;
	private PeerGroup peerGroup;
	
	private boolean started;
	private NetworkManager manager;
	
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
	public void start() throws P2PNetworkException {
		Preconditions.checkState(!started, "P2P network already started");
		
		final File conf = new File(System.getProperty("user.home") + System.getProperty("file.separator") + peerName);
		NetworkManager.RecursiveDelete(conf);
		
		peerID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);
		
		try {
			manager = new NetworkManager(NetworkManager.ConfigMode.ADHOC, peerName, conf.toURI());
	
			configureNetwork(manager.getConfigurator(), peerID, peerName);
	
			PeerGroup netPeerGroup = manager.startNetwork();
			PeerGroupID peerGroupID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, groupName.getBytes());
			
			peerGroup = createSubGroup(netPeerGroup, peerGroupID, groupName);
			started = true;
			
			fireStartEvent();
	
		} catch( Exception ex ) {
			manager = null;
			
			throw new P2PNetworkException("Could not initialize/connect p2p network", ex);
		}		
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

	private static void configureNetwork(NetworkConfigurator configurator, PeerID peerID, String peerName) {
		configurator.setTcpPort(PORT);
		configurator.setTcpEnabled(true);
		configurator.setTcpIncoming(true);
		configurator.setTcpOutgoing(true);
		configurator.setUseMulticast(true);
		configurator.setPeerID(peerID);
		configurator.setName(peerName);
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
		
		if( manager != null ) {
			manager.stopNetwork();
			manager = null;
		}
		
		started = false;
		
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
