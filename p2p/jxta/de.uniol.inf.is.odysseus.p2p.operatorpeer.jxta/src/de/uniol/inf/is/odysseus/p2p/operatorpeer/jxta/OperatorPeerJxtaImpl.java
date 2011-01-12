package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.MessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.SocketServerListener;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler.SourceHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;


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
	
	
	public DiscoveryService discoveryService;

	public NetworkManager manager = null;

	public PeerGroup netPeerGroup;

	public NetworkConfigurator networkConfigurator;

	public  PipeService pipeService;

	private PipeAdvertisement serverResponseAddress;
	
	
	public void activate() {
		getLogger().info("OSGi Services loaded");
		
		// TODO: User einlesen
		GlobalState.setActiveUser(UserManagement.getInstance().getSuperUser());
		// TODO: Unterschiedliche Namen notwendig?
		GlobalState.setActiveDatadictionary(DataDictionaryFactory.getDefaultDataDictionary("OperatorPeer")); 
		startPeer();
		getDistributionClient().initializeService();
	}
	
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


//	@Override
//	protected void initQuerySpezificationFinder() {
//		this.querySpezificationFinder = new QuerySpezificationListenerJxtaImpl();
//	}

	@Override
	protected void initSocketServerListener(AbstractOperatorPeer aPeer) {
		setSocketServerListener(new SocketServerListener(aPeer));
		if(getMessageHandlerList()!=null) {
			getSocketServerListener().registerMessageHandler(getMessageHandlerList());
		}
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


	@Override
	public void startNetwork() {
		
		if (RANDOM_NAME) {
			name = "" + name + "" + System.currentTimeMillis();
		}
		System.setProperty("net.jxta.logging.Logging", LOGGING);

		try {
			CacheTool.checkForExistingConfigurationDeletion("OperatorPeer_" + name,
					new File(new File(".cache"), "OperatorPeer_" + name));
		} catch (IOException e2) {
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
				manager.getConfigurator().setHttpInterfaceAddress(HTTP_INTERFACE_ADDRESS);
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

	@Override
	public void stopNetwork() {
		manager.stopNetwork();
	}

	@Override
	protected void initAliveHandler() {
		this.aliveHandler = new AliveHandlerJxtaImpl();
		
	}

	@Override
	protected void initSources(AbstractOperatorPeer aPeer) {
	    		
		getSources().put("nexmark:person2", "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440");
		getSources().put("nexmark:auction2", "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441");
		getSources().put("nexmark:bid2", "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442");
		List<IQueryBuildSetting<?>> cfg = aPeer.getExecutor().getQueryBuildConfiguration("Standard");;
		if (cfg == null){
			  getLogger().debug("No Query Build Configuration found!!!");
			  return;
		}

		for (String s : getSources().values()) {
			try {		
				User user = GlobalState.getActiveUser();
				IDataDictionary dd = GlobalState.getActiveDatadictionary();
				aPeer.getExecutor().addQuery(s, "CQL", user,dd, cfg.toArray(new IQueryBuildSetting[0])  );		
			} catch (PlanManagementException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}



	
	@Override
	protected void initSourceHandler(AbstractOperatorPeer aPeer) {
		this.sourceHandler = new SourceHandlerJxtaImpl((OperatorPeerJxtaImpl)aPeer);
		
	}
	
	@Override
	protected void initServerResponseConnection() {
		setServerPipeAdvertisement(AdvertisementTools.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
//		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
//		.newAdvertisement(PipeAdvertisement.getAdvertisementType());
//		advertisement.setPipeID(IDFactory.newPipeID(OperatorPeerJxtaImpl.getInstance()
//		.getNetPeerGroup().getPeerGroupID()));
//		advertisement.setType(PipeService.UnicastType);
//		advertisement.setName("serverPipe");
//		return advertisement;
		
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
	public void initLocalMessageHandler() {
		// TODO Auto-generated method stub
		
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