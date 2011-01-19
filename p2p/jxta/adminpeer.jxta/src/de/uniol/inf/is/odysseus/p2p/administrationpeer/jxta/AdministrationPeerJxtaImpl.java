package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.AbstractAdministrationPeer;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler.AdminPeerQueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.HotPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.OperatorPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.QuerySpezificationListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy.HotPeerStrategyRandom;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.SocketServerListener;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class AdministrationPeerJxtaImpl extends AbstractAdministrationPeer {

	static Logger logger = LoggerFactory.getLogger(AdministrationPeerJxtaImpl.class);
	static Logger getLogger(){
		return logger;
	}

	
	public HashMap<String, ExtendedPeerAdvertisement> getOperatorPeers() {
		return operatorPeers;
	}

	public void setOperatorPeers(
			HashMap<String, ExtendedPeerAdvertisement> operatorPeers) {
		this.operatorPeers = operatorPeers;
	}
	
	//TODO: Daten in Config oder Umgebung auslagern.

	private static final String name = "adminPeer";

	private static final int tcpPort = 8900;

	private static final int httpPort = 8901;

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

	public PeerGroup netPeerGroup;

	public HashMap<String, SourceAdvertisement> sources = new HashMap<String, SourceAdvertisement>();

	public DiscoveryService discoveryService;

	public NetworkManager manager = null;

	public NetworkConfigurator networkConfigurator;

	public PipeService pipeService;

	private PipeAdvertisement serverPipeAdvertisement;

	public void setServerPipeAdvertisement(
			PipeAdvertisement serverPipeAdvertisement) {
		this.serverPipeAdvertisement = serverPipeAdvertisement;
	}

	public PipeAdvertisement getServerPipeAdvertisement() {
		return serverPipeAdvertisement;
	}

	public HashMap<String, ExtendedPeerAdvertisement> operatorPeers = new HashMap<String, ExtendedPeerAdvertisement>();

	public void activate() {
		
		// TODO: Read from Config-File
		startPeer();
		getDistributionProvider().initializeService();
		getLogger().info("Administration Peer started");

	}

	// für die korrekte Nutzung in OSGi muss der Konstruktor public sein. 
	public AdministrationPeerJxtaImpl() {

		super();
		
		// TODO: Nutzer auslesen
		GlobalState.setActiveUser(UserManagement.getInstance().getSuperUser());
		// TODO: M�ssen sich die Namen unterscheiden? Eigentlich nicht, ist nur ein Admin Peer to JVM ..
		GlobalState.setActiveDatadictionary(DataDictionaryFactory.getDefaultDataDictionary("AdminPeer"));
	}


	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public NetworkManager getManager() {
		return manager;
	}

	public PeerGroup getNetPeerGroup() {
		return netPeerGroup;
	}

	public NetworkConfigurator getNetworkConfigurator() {
		return networkConfigurator;
	}

	public PipeService getPipeService() {
		return pipeService;
	}

	public HashMap<String, SourceAdvertisement> getSources() {
		return sources;
	}

	@Override
	protected void initQuerySpezificationListener() {
		querySpezificationListener = new QuerySpezificationListenerJxtaImpl((JxtaMessageSender) getMessageSender(), this);

	}

	@Override
	protected void initSourceListener() {
		this.sourceListener = new SourceListenerJxtaImpl(getExecutor(), this);

	}

	public void setDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = discoveryService;
	}

	public void setManager(NetworkManager manager) {
		this.manager = manager;
	}

	public void setNetPeerGroup(PeerGroup netPeerGroup) {
		this.netPeerGroup = netPeerGroup;
	}

	public void setNetworkConfigurator(NetworkConfigurator networkConfigurator) {
		this.networkConfigurator = networkConfigurator;
	}

	public void setPipeService(PipeService pipeService) {
		this.pipeService = pipeService;
	}

	public void setSources(HashMap<String, SourceAdvertisement> sources) {
		this.sources = sources;
	}

	@Override
	protected void startNetwork() {
		getLogger().info("Starting Peer Network");
		System.setProperty("net.jxta.logging.Logging", LOGGING);
		String name = "";
		if (RANDOM_NAME) {
			name = "" + AdministrationPeerJxtaImpl.name + ""
					+ System.currentTimeMillis();
		}

		try {
			CacheTool.checkForExistingConfigurationDeletion(
					"AdministrationPeer_" + name, new File(new File(".cache"),
							"AdministrationPeer_" + name));
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
			manager = new NetworkManager(peerMode,
					"AdministrationPeer_" + name, new File(new File(".cache"),
							"AdministrationPeer_" + name).toURI());

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
	//			if (!manager.isStarted()) {
					manager.startNetwork();
//				}
			} catch (PeerGroupException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		manager.waitForRendezvousConnection(CONNECTION_TIME);
		netPeerGroup = manager.getNetPeerGroup();
		PeerGroupTool.setPeerGroup(netPeerGroup);
		discoveryService = netPeerGroup.getDiscoveryService();
		pipeService = netPeerGroup.getPipeService();
		AdvertisementFactory.registerAdvertisementInstance(
				QueryTranslationSpezification.getAdvertisementType(),
				new QueryTranslationSpezification.Instantiator());

		AdvertisementFactory
				.registerAdvertisementInstance(SourceAdvertisement
						.getAdvertisementType(),
						new SourceAdvertisement.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				QueryExecutionSpezification.getAdvertisementType(),
				new QueryExecutionSpezification.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				ExtendedPeerAdvertisement.getAdvertisementType(),
				new ExtendedPeerAdvertisement.Instantiator());
		try {
			discoveryService.publish(netPeerGroup.getPeerAdvertisement(),
					13000, 13000);
			discoveryService.remotePublish(netPeerGroup.getPeerAdvertisement(),
					13000);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void stopNetwork() {
		manager.stopNetwork();

	}

	@Override
	protected void initOperatorPeerListener() {
		operatorPeerListener = new OperatorPeerListenerJxtaImpl(this);

	}

	@Override
	protected void initAliveHandler() {
		aliveHandler = new AliveHandlerJxtaImpl(this);

	}

	@Override
	protected void initHotPeerFinder() {
		this.hotPeerFinder = new HotPeerListenerJxtaImpl();

	}


	@Override
	protected void initHotPeerStrategy() {
		this.hotPeerStrategy = new HotPeerStrategyRandom();

	}

	@Override
	protected void initServerResponseConnection() {
		setServerPipeAdvertisement(AdvertisementTools
				.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
		
	}

	@Override
	public Object getServerResponseAddress() {
		return this.serverPipeAdvertisement;
	}

	@Override
	protected void initSocketServerListener() {
		setSocketServerListener(new SocketServerListener(this));
		if(getMessageHandlerList()!=null) {
			getSocketServerListener().registerMessageHandler(getMessageHandlerList());
		}
	}

	@Override
	public void initLocalMessageHandler() {
		registerMessageHandler(new AdminPeerQueryResultHandlerJxtaImpl(this));
	}

	@Override
	public void initLocalExecutionHandler() {
		//TODO: Anders Loesen
		for(IExecutionHandler<?> h : getExecutionHandler()) {
			if(h.getProvidedLifecycle() == Lifecycle.NEW) {
				h.setPeer(this);
			}
		}
	}

	@Override
	public void initMessageSender() {
		setMessageSender(new JxtaMessageSender());
	}

}
