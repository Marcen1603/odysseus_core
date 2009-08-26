package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.QueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.SourceHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.QuerySpezificationListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.strategy.bidding.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.CacheTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public class OperatorPeerJxtaImpl extends AbstractOperatorPeer {
	
	private String name = "OperatorPeer";
	
	private int tcpPort=9900;
	
	private int httpPort=10000;

	// Logging an oder aus
	private static final String LOGGING = "OFF";

	// Zum Testen um mehrere Peers gleichzeitig zu starten,
	// dem Namen des Peers wird eine zufällge Nummernfolgen angehängt, um
	// den Peer im Netzwerk eindeutig zu machen.
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
	
//	private static final String trafoMode = "PNID";
	
	private static final String trafoMode = "TI";
	
	public DiscoveryService discoveryService;

	public NetworkManager manager = null;

	public PeerGroup netPeerGroup;

	public NetworkConfigurator networkConfigurator;

	public  PipeService pipeService;

	public  HashMap<String, ILogicalOperator> plans = new HashMap<String, ILogicalOperator>();

	public  HashMap<String, Query> queries = new HashMap<String, Query>();
	
	
	public static OperatorPeerJxtaImpl instance = null;
	
	public static OperatorPeerJxtaImpl getInstance(){
		if (instance == null){
			instance = new OperatorPeerJxtaImpl();
		}
		return instance;
	}

	private OperatorPeerJxtaImpl() {
		
	}
	
	
	
	public static String getTrafoMode() {
		return trafoMode;
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

	public HashMap<String, ILogicalOperator> getPlans() {
		return plans;
	}

	public HashMap<String, Query> getQueries() {
		return queries;
	}

	@Override
	protected void initQuerySpezificationFinder() {
		this.querySpezificationFinder = new QuerySpezificationListenerJxtaImpl();
	}

	@Override
	protected void initSocketServerListener(AbstractOperatorPeer aPeer) {
		this.socketServerListener = new SocketServerListenerJxtaImpl(aPeer);
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

	public void setPlans(HashMap<String, ILogicalOperator> plans) {
		this.plans = plans;
	}

	public void setQueries(HashMap<String, Query> queries) {
		this.queries = queries;
	}

	public void startNetwork() {
		
		if (RANDOM_NAME) {
			name = "" + name + "" + System.currentTimeMillis();
		}
		System.setProperty("net.jxta.logging.Logging", LOGGING);

		try {
			CacheTool.checkForExistingConfigurationDeletion("OperatorPeer_" + name,
					new File(new File(".cache"), "OperatorPeer_" + name));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		ConfigMode peerMode = null;
		
		if (USE_SUPER_PEER){
			peerMode = NetworkManager.ConfigMode.EDGE;
		}
		else{
			peerMode = NetworkManager.ConfigMode.ADHOC;
		}
		
		try {
			manager = new NetworkManager(peerMode,
					"OperatorPeer_" + name, new File(new File(".cache"),
							"OperatorPeer_" + name).toURI());

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
					// TODO Auto-generated catch block
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
			
			if (!TCP_INTERFACE_ADDRESS.equals("")){
				manager.getConfigurator().setTcpInterfaceAddress(TCP_INTERFACE_ADDRESS);
			}
			
			if (!HTTP_INTERFACE_ADDRESS.equals("")){
				manager.getConfigurator().setTcpInterfaceAddress(HTTP_INTERFACE_ADDRESS);
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		if (manager.getNetPeerGroup() == null) {
			try {
				manager.startNetwork();
			} catch (PeerGroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		netPeerGroup = manager.getNetPeerGroup();
		this.discoveryService = this.getNetPeerGroup().getDiscoveryService();
		AdvertisementFactory.registerAdvertisementInstance(
				QueryTranslationSpezification.getAdvertisementType(),
				new QueryTranslationSpezification.Instantiator());

		manager.waitForRendezvousConnection(CONNECTION_TIME);

		AdvertisementFactory
				.registerAdvertisementInstance(SourceAdvertisement
						.getAdvertisementType(),
						new SourceAdvertisement.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				QueryExecutionSpezification.getAdvertisementType(),
				new QueryExecutionSpezification.Instantiator());

	}

	public void stopNetwork() {
		manager.stopNetwork();
	}

	@Override
	protected void initAliveHandler() {
		this.aliveHandler = new AliveHandlerJxtaImpl();
		
	}

	@Override
	protected void initBiddingStrategy() {
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl();
		
	}

	@Override
	protected void initQueryResultHandler() {
		this.queryResultHandler = new QueryResultHandlerJxtaImpl(getTrafo());
		
	}

	@Override
	protected void initSources() {
		sources.add("nexmark:person");
		sources.add("nexmark:auction");
	}


	@Override
	protected void initSourceHandler(AbstractOperatorPeer aPeer) {
		this.sourceHandler = new SourceHandlerJxtaImpl(aPeer);
		
	}}