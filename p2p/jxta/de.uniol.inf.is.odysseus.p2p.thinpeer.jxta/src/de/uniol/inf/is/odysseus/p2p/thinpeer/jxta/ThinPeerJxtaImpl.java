package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.MessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.SocketServerListener;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.thinpeer.AbstractThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.GuiUpdaterJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryNegotiationMessageHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryPublisherHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.AdministrationPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy.RandomIdGenerator;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class ThinPeerJxtaImpl extends AbstractThinPeer {

	private String name = "ThinPeer";

	private int tcpPort = 7900;

	private int httpPort = 7901;

	// Logging an oder aus
	private static final String LOGGING = "OFF";

	// Zum Testen um mehrere Peers gleichzeitig zu starten,
	// dem Namen des Peers wird eine zufällge Nummernfolgen angehängt, um
	// Den Peer im Netzwerk eindeutig zu machen.
	private static final boolean RANDOM_NAME = true;

	// Wieviele Millisekunden soll probiert werden eine Verbindung
	// mit dem P2P-Netzwerk aufzubauen bevor aufgegeben wird.
	private static final int CONNECTION_TIME = 12000;

	// Soll Multicasting für Peers im lokalen Netzwertk aktiviert
	// werden.
	private static final boolean MULTICAST = true;

	// Soll HTTP für den Peer aktiviert werden ? Unbedingt notwendig
	// wenn Peer im WAN arbeiten soll.
	private static final boolean HTTP = true;

	// Soll TCP für den Peer aktiviert werden.
	private static final boolean TCP = true;

	private static final boolean USE_SUPER_PEER = false;

	private static final String TCP_RENDEZVOUS_URI = "tcp://hurrikan.informatik.uni-oldenburg.de:10801";

	private static final String HTTP_RENDEZVOUS_URI = "http://hurrikan.informatik.uni-oldenburg.de:10802";

	private static final String TCP_RELAY_URI = "tcp://hurrikan.informatik.uni-oldenburg.de:10801";

	private static final String HTTP_RELAY_URI = "http://hurrikan.informatik.uni-oldenburg.de:10802";

	private static final String TCP_INTERFACE_ADDRESS = "";

	private static final String HTTP_INTERFACE_ADDRESS = "";

	private DiscoveryService discoveryService;
	
	private PipeAdvertisement serverResponseAddress;

	private NetworkManager manager = null;

	private PeerGroup netPeerGroup;

	private HashMap<String, SourceAdvertisement> sources = new HashMap<String, SourceAdvertisement>();

	private static ThinPeerJxtaImpl instance = null;

	public static ThinPeerJxtaImpl getInstance() {
		if (instance == null)
			instance = new ThinPeerJxtaImpl();
		return instance;
	}


	@Override
	public HashMap<String, Object> getAdminPeers() {
		return adminPeers;
	}

	private ThinPeerJxtaImpl() {
		// TODO: Nutzer auslesen
		GlobalState.setActiveUser(UserManagement.getInstance().getSuperUser());
		// TODO: M�ssen sich die Namen unterscheiden? Eigentlich nicht, ist nur ein Admin Peer to JVM ..
		GlobalState.setActiveDatadictionary(null);
	}

	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public PeerGroup getNetPeerGroup() {
		return netPeerGroup;
	}

	@Override
	public void startNetwork() {

		System.setProperty("net.jxta.logging.Logging", LOGGING);
		if (RANDOM_NAME) {
			name = "" + name + "" + System.currentTimeMillis();
		}
		try {
			CacheTool.checkForExistingConfigurationDeletion("ThinPeer_" + name,
					new File(new File(".cache"), "ThinPeer_" + name));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		ConfigMode peerMode = null;

		if (USE_SUPER_PEER) {
			peerMode = NetworkManager.ConfigMode.EDGE;
		} else {
			peerMode = NetworkManager.ConfigMode.ADHOC;
		}

		try {
			manager = new NetworkManager(peerMode, "ThinPeer_" + name,
					new File(new File(".cache"), "ThinPeer_" + name).toURI());
			manager.getConfigurator().clearRelaySeeds();
			manager.getConfigurator().clearRendezvousSeeds();
			if (USE_SUPER_PEER) {
				manager.getConfigurator().clearRelaySeeds();
				manager.getConfigurator().clearRendezvousSeeds();

				try {
					manager.getConfigurator().addSeedRendezvous(
							new URI(HTTP_RENDEZVOUS_URI));
					manager.getConfigurator().addSeedRelay(
							new URI(HTTP_RELAY_URI));
					manager.getConfigurator().addSeedRendezvous(
							new URI(TCP_RENDEZVOUS_URI));
					manager.getConfigurator().addSeedRelay(
							new URI(TCP_RELAY_URI));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			manager.getConfigurator().setTcpPort(tcpPort);
			manager.getConfigurator().setHttpPort(httpPort);
			manager.getConfigurator().setHttpEnabled(HTTP);
			manager.getConfigurator().setHttpOutgoing(HTTP);
			manager.getConfigurator().setHttpIncoming(HTTP);
			manager.getConfigurator().setTcpEnabled(TCP);
			manager.getConfigurator().setTcpOutgoing(TCP);
			manager.getConfigurator().setTcpIncoming(TCP);
			manager.getConfigurator().setUseMulticast(MULTICAST);

			if (!TCP_INTERFACE_ADDRESS.equals("")) {
				manager.getConfigurator().setTcpInterfaceAddress(
						TCP_INTERFACE_ADDRESS);
			}

			if (!HTTP_INTERFACE_ADDRESS.equals("")) {
				manager.getConfigurator().setTcpInterfaceAddress(
						HTTP_INTERFACE_ADDRESS);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (manager.getNetPeerGroup() == null) {
			try {
				manager.startNetwork();
			} catch (PeerGroupException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		netPeerGroup = manager.getNetPeerGroup();
		PeerGroupTool.setPeerGroup(netPeerGroup);
		this.discoveryService = netPeerGroup.getDiscoveryService();

		try {
			discoveryService.publish(netPeerGroup.getPeerAdvertisement());
			discoveryService.remotePublish(netPeerGroup.getPeerAdvertisement());
		} catch (IOException e) {
			e.printStackTrace();
		}

		AdvertisementFactory.registerAdvertisementInstance(
				QueryTranslationSpezification.getAdvertisementType(),
				new QueryTranslationSpezification.Instantiator());

		AdvertisementFactory
				.registerAdvertisementInstance(SourceAdvertisement
						.getAdvertisementType(),
						new SourceAdvertisement.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement
				.getAdvertisementType(),
				new QueryExecutionSpezification.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				ExtendedPeerAdvertisement.getAdvertisementType(),
				new ExtendedPeerAdvertisement.Instantiator());

		manager.waitForRendezvousConnection(CONNECTION_TIME);

	}

	@Override
	public void stopNetwork() {
		netPeerGroup.stopApp();
		manager.stopNetwork();
		System.exit(0);
	}

	@Override
	public void publishQuerySpezification(String query, String language) {
		queryPublisher.publishQuerySpezification(idGenerator.generateId(),
				query, language);
	}

	@Override
	protected void initSocketServerListener() {
		if (getSocketServerListener() == null) {
			setSocketServerListener(new SocketServerListener(this));
			if(getMessageHandlerList()!=null) {
				getSocketServerListener().registerMessageHandler(getMessageHandlerList());
			}
		}
	}

	public HashMap<String, SourceAdvertisement> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, SourceAdvertisement> sources) {
		this.sources = sources;
	}

	@Override
	protected void initGuiUpdater() {
		this.guiUpdater = new GuiUpdaterJxtaImpl();
	}

//	@Override
//	protected void initQueryBiddingHandler() {
//		this.queryBiddingHandler = new BiddingHandlerJxtaImpl();
//	}

	@Override
	protected void initAdministrationPeerListener() {
		administrationPeerListener = new AdministrationPeerListenerJxtaImpl();
	}

	@Override
	protected void initSourceListener() {
		sourceListener = new SourceListenerJxtaImpl();
	}

	@Override
	protected void initQueryPublisher() {
		queryPublisher = new QueryPublisherHandlerJxtaImpl(this);
	}

	@Override
	public void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, String adminPeerName) {
		String queryId = idGenerator.generateId();
		queryPublisher.sendQuerySpezificationToAdminPeer(queryId, query,
				language, adminPeer);
		for(Query q : ThinPeerJxtaImpl.getInstance().getQueries().keySet()) {
			q.setStatus(Lifecycle.NEW);
			Log.addAdminPeer(queryId,
					adminPeerName);
			Log.addStatus(
					queryId, 
					q.getStatus().toString());
		}

	}

//	@Override
//	protected void initBiddingHandlerStrategy() {
//		this.biddingHandlerStrategy = new BiddingHandlerStrategyStandard();
//
//	}
	
	@Override
	protected void initIdGenerator() {
		this.idGenerator = new RandomIdGenerator();
	}

	@Override
	protected void initAdminPeerList() {
		this.adminPeers = new HashMap<String, Object>();
		
	}

	private void setServerPipeAdvertisement(
			PipeAdvertisement serverPipeAdvertisement) {
		this.serverResponseAddress = serverPipeAdvertisement;
	}

	@Override
	public Object getServerResponseAddress() {
		return this.serverResponseAddress;
	}
	
	@Override
	protected void initServerResponseConnection() {
		setServerPipeAdvertisement(AdvertisementTools
				.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
	}


	@Override
	public void initLocalMessageHandler() {
			registerMessageHandler(new QueryNegotiationMessageHandler());


	}


	@Override
	public void initLocalExecutionHandler() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initMessageSender() {
		setMessageSender(new MessageSender<PeerGroup, Message, PipeAdvertisement>());
	}




}
