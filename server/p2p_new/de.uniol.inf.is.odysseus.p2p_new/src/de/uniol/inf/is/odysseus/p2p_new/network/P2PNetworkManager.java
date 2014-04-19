package de.uniol.inf.is.odysseus.p2p_new.network;

import java.io.File;
import java.net.URI;

import net.jxta.id.IDFactory;
import net.jxta.impl.cm.CacheManager;
import net.jxta.impl.peergroup.StdPeerGroup;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.Module;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.rendezvous.RendezvousEvent;
import net.jxta.rendezvous.RendezvousListener;

import org.apache.log4j.PropertyConfigurator;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNetworkException;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.peer.config.PeerConfiguration;

public final class P2PNetworkManager implements IP2PNetworkManager, RendezvousListener {

	private static final Logger LOG = LoggerFactory.getLogger(P2PNetworkManager.class);
	private static final String LOG_PROPERTIES_FILENAME = "log4j.properties";
	private static final String JXTA_LOGGER_NAME = "net.jxta";
	private static final java.util.logging.Level JXTA_LOG_LEVEL = java.util.logging.Level.SEVERE;

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
	
	private final AdvertisementDiscoverer advDiscoverer = new AdvertisementDiscoverer();

	// called by OSGi-DS
	public void activate() {
		instance = this;
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;

		if (started) {
			stop();
		}
	}
	
	public static void waitFor() {
		while( !isActivated() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	public static P2PNetworkManager getInstance() {
		return instance;
	}

	public static boolean isActivated() {
		return instance != null;
	}

	@Override
	public void setLocalPeerName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Peer name must not be null or empty!");

		peerName = name;
	}

	@Override
	public String getLocalPeerName() {
		return peerName;
	}

	@Override
	public void setLocalPeerGroupName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Peer group name must not be null or empty!");

		groupName = name;
	}

	@Override
	public String getLocalPeerGroupName() {
		return groupName;
	}

	@Override
	public void setRendevousPeerAddress(URI addressURI) {
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
	public void setRendevousPeer(boolean isRendevousPeer) {
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
	
		System.setProperty("net.jxta.impl.cm.cache.impl", "net.jxta.impl.cm.sql.H2AdvertisementCache");
		System.setProperty("net.jxta.impl.cm.SrdiIndex.backend.impl", "net.jxta.impl.cm.InMemorySrdi");

		configureLogging(P2PNewPlugIn.getBundle());

		File conf = new File(PeerConfiguration.ODYSSEUS_HOME_DIR + "peers" + File.separator + peerName);
		NetworkManager.RecursiveDelete(conf);

		peerID = IDFactory.newPeerID(PeerGroupID.defaultNetPeerGroupID);
		LOG.debug("Starting p2p network. peerName = {}, groupName = {}, peerID = {}", new Object[] { peerName, groupName, peerID });

		try {
			manager = new NetworkManager(determineConfigMode(), peerName, conf.toURI());

			configureNetwork(manager.getConfigurator(), peerID, peerName, port, rendevousPeerURI);

			PeerGroup netPeerGroup = manager.startNetwork();
			if (!isRendevousPeer) {
				LOG.debug("Deactivating rendevous service");
				netPeerGroup.getRendezVousService().setAutoStart(false);
			}

			PeerGroupID peerGroupID = IDFactory.newPeerGroupID(PeerGroupID.defaultNetPeerGroupID, groupName.getBytes());
			LOG.debug("Peer Group ID is {}", peerGroupID.toString());
			peerGroup = createSubGroup(netPeerGroup, peerGroupID, groupName);

			if (peerGroup.startApp(new String[0]) != Module.START_OK) {
				throw new P2PNetworkException("Could not start child group");
			}

			if (isRendevousPeer) {
				LOG.debug("Starting rendevous service");
				peerGroup.getRendezVousService().startRendezVous();
			}
			peerGroup.getRendezVousService().addListener(this);

			deactiveDeltaTracker(netPeerGroup);
			deactiveDeltaTracker(peerGroup);

			advDiscoverer.start();
			started = true;
			LOG.debug("P2P network started");

			fireStartEvent();

		} catch (Exception ex) {
			manager = null;

			throw new P2PNetworkException("Could not initialize/connect p2p network", ex);
		}
	}

	private static void deactiveDeltaTracker(PeerGroup peerGroup) {
		CacheManager cacheManager = ((StdPeerGroup) peerGroup).getCacheManager();
		cacheManager.setTrackDeltas(false);
	}

	private static void configureLogging(Bundle bundle) {
		java.util.logging.Logger jxtaLogger = java.util.logging.Logger.getLogger(JXTA_LOGGER_NAME);
		jxtaLogger.setLevel(JXTA_LOG_LEVEL);

		PropertyConfigurator.configure(bundle.getResource(LOG_PROPERTIES_FILENAME));
	}

	private ConfigMode determineConfigMode() {
		if (isRendevousPeer) {
			LOG.debug("Peer is RENDEVOUS");
			return NetworkManager.ConfigMode.RENDEZVOUS;
		}
		if (rendevousPeerURI != null) {
			LOG.debug("Peer is EDGE using rendervous address " + rendevousPeerURI);
			return NetworkManager.ConfigMode.EDGE;
		}
		LOG.debug("Peer is ADHOC");
		return NetworkManager.ConfigMode.ADHOC;
	}

	private void fireStartEvent() {
		ImmutableCollection<IP2PNetworkListener> listeners = P2PNetworkListenerRegistry.getInstance().getAllListeners();
		for (IP2PNetworkListener listener : listeners) {
			try {
				listener.networkStarted(this);
			} catch (Throwable t) {
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

		if (rdvAddress != null) {
			configurator.clearRendezvousSeeds();
			configurator.addSeedRendezvous(rdvAddress);
			configurator.setUseOnlyRendezvousSeeds(true);
			configurator.setUseOnlyRendezvousSeeds(true);
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

		advDiscoverer.stopRunning();
		
		LOG.debug("Stopping p2p network");
		if (manager != null) {
			manager.stopNetwork();
			manager = null;
		}

		started = false;
		LOG.debug("P2P network stopped");

		fireStopEvent();
	}

	private void fireStopEvent() {
		ImmutableCollection<IP2PNetworkListener> listeners = P2PNetworkListenerRegistry.getInstance().getAllListeners();
		for (IP2PNetworkListener listener : listeners) {
			try {
				listener.networkStopped(this);
			} catch (Throwable t) {
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

	@Override
	public void rendezvousEvent(RendezvousEvent event) {
		if (event == null) {
			return;
		}

		if (event.getType() == RendezvousEvent.RDVCONNECT) {
			LOG.debug("Connection to RDV");
		} else if (event.getType() == RendezvousEvent.RDVRECONNECT) {
			LOG.debug("Reconnection to RDV");
		} else if (event.getType() == RendezvousEvent.CLIENTCONNECT) {
			LOG.debug("EDGE client connection");
		} else if (event.getType() == RendezvousEvent.CLIENTRECONNECT) {
			LOG.debug("EDGE client reconnection");
		} else if (event.getType() == RendezvousEvent.RDVDISCONNECT) {
			LOG.debug("Disconnection from RDV");
		} else if (event.getType() == RendezvousEvent.RDVFAILED) {
			LOG.debug("Connection to RDV failed");
		} else if (event.getType() == RendezvousEvent.CLIENTDISCONNECT) {
			LOG.debug("EDGE client disconnection from RDV");
		} else if (event.getType() == RendezvousEvent.CLIENTFAILED) {
			LOG.debug("EDGE client connection to RDV failed");
		} else if (event.getType() == RendezvousEvent.BECAMERDV) {
			LOG.debug("This peer became RDV");
		} else if (event.getType() == RendezvousEvent.BECAMEEDGE) {
			LOG.debug("This peer became EDGE");
		}

		// BAD HACK
		deactiveDeltaTracker(manager.getNetPeerGroup());
		deactiveDeltaTracker(peerGroup);
	}
	
	@Override
	public void addListener(IP2PNetworkListener listener) {
		P2PNetworkListenerRegistry.getInstance().add(listener);
	}
	
	@Override
	public void removeListener(IP2PNetworkListener listener) {
		P2PNetworkListenerRegistry.getInstance().remove(listener);
	}
	
	@Override
	public void addAdvertisementListener(IAdvertisementDiscovererListener listener) {
		advDiscoverer.addListener(listener);
	}
	
	@Override
	public void removeAdvertisementListener(IAdvertisementDiscovererListener listener) {
		advDiscoverer.removeListener(listener);
	}
}
