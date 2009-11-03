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
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGenerator;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkGeneratorConfiguration;
import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.QueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler.SourceHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.QuerySpezificationListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.strategy.bidding.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.CacheTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;

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

	public OperatorPeerJxtaImpl() {
		instance = this;
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
				System.out.println("Aufruf startnetwork");
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
		PeerGroupTool.setPeerGroup(netPeerGroup);
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
	protected void initSources(AbstractOperatorPeer aPeer) {
//		NexmarkServer server = new NexmarkServer(<personPort>, <auctionPort>, <bidPort>, <categoryPort>, <elementLimit>);
		
//		NexmarkServer server = null;
//		try {
//			server = new NexmarkServer(65430,65431,65432,65433,10000,true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	    server.start();
	    
		getSources().put("nexmark:person", "CREATE STREAM nexmark:person (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65430");
//		getSources().put("nexmark:bid", "CREATE STREAM nexmark:bid (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65432");
//		String[] q = new String[4];
//		q[0] = "CREATE STREAM nexmark:person (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65430";
//		q[1] = "CREATE STREAM nexmark:bid (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65432";
//		q[2] = "CREATE STREAM nexmark:auction (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65431";
//		q[3] = "CREATE STREAM nexmark:category (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65433";
		for (String s : getSources().values()) {
			try {		
				System.out.println("füge Quellenquery hinzu");
				aPeer.getExecutor().addQuery(s, "CQL", new ParameterPriority(2) );
				
			} catch (PlanManagementException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Beispielhaft Nexmark Server starten und Sourcen hinzufügen
		

//		sources.add("nexmark:auction");
//		sources.add("nexmark:bid");
//		sources.add("nexmark:category");
	}


	@Override
	protected void initSourceHandler(AbstractOperatorPeer aPeer) {
		System.out.println("SourceHandler initialisieren");
		this.sourceHandler = new SourceHandlerJxtaImpl((OperatorPeerJxtaImpl)aPeer);
		
	}}