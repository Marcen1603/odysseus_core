package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta;

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
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.AbstractAdministrationPeer;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler.BiddingHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler.QueryResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.EventListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.HotPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.OperatorPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.QuerySpezificationListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy.BiddingHandlerStrategyStandard;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy.HotPeerStrategyRandom;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy.bidding.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.CacheTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;

public class AdministrationPeerJxtaImpl extends AbstractAdministrationPeer {

	public HashMap<String, ExtendedPeerAdvertisement> getOperatorPeers() {
		return operatorPeers;
	}

	public void setOperatorPeers(HashMap<String,ExtendedPeerAdvertisement> operatorPeers) {
		this.operatorPeers = operatorPeers;
	}

	private static final String name = "adminPeer";
	
	private static final int tcpPort= 8900;
	
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

	private HashMap<String, QueryJxtaImpl> queries = new HashMap<String, QueryJxtaImpl>();
	
	public HashMap<String, ExtendedPeerAdvertisement> operatorPeers = new HashMap<String, ExtendedPeerAdvertisement>();
	
	private static AdministrationPeerJxtaImpl instance = null;
	
//	
//	private ICompiler compiler;
//	
//	public ICompiler getCompiler() {
//		return compiler;
//	}
//	
//	public void bindCompiler(ICompiler compiler) {
//		this.compiler = compiler;
//		System.out.println("macht den bind beim administrationspeer" +compiler);
//		startPeer();
//	}
//	
//	public void unbindCompiler(ICompiler compiler) {
//		if(this.compiler == compiler){
//			System.out.println("macht leider unbind");
//			this.compiler = null;
//		}
//	}
//
//	

	public void activate() {
		System.out.println("Starte Admin-Peer");
		startPeer();
	}
	
	//für die korrekte Nutzung in OSGi muss der Konstruktor public sein. Damit der "Singleton" in weiteren Programmaufrufen unberührt bleibt, wurde noch die Instanz der Static Variablen zugewiesen. 
	public AdministrationPeerJxtaImpl(){
		instance = this;
	}
	
	/**
	 * Entspricht nicht mehr dem Singleton, wird allerdings in vielen Teilen gebraucht, so dass der Workaround über den public Konstruktor gewählt wurde.
	 * @return
	 */
	public synchronized static AdministrationPeerJxtaImpl getInstance(){
		if (instance==null) {
			instance = new AdministrationPeerJxtaImpl();
		}
		return instance;
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

	public HashMap<String, QueryJxtaImpl> getQueries() {
		System.out.println("HOLE QUERIES");
		return queries;
	}

	public HashMap<String, SourceAdvertisement> getSources() {
		return sources;
	}

	@Override
	protected void initBiddingHandler() {
		this.biddingHandler = new BiddingHandlerJxtaImpl();
	}

	@Override
	protected void initQuerySpezificationListener() {
		querySpezificationListener = new QuerySpezificationListenerJxtaImpl(this.biddingStrategy );

	}

	@Override
	protected void initSocketServerListener() {
		this.socketServerListener = new SocketServerListenerJxtaImpl();

	}

	@Override
	protected void initSourceListener() {
		this.sourceListener = new SourceListenerJxtaImpl(getExecutor());

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
		
		System.setProperty("net.jxta.logging.Logging", LOGGING);
		String name = "";
		if (RANDOM_NAME) {
			name = "" + AdministrationPeerJxtaImpl.name + "" + System.currentTimeMillis();
		}

		try {
			CacheTool.checkForExistingConfigurationDeletion("AdministrationPeer_" + name,
					new File(new File(".cache"), "AdministrationPeer_" + name));
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
		
		manager.waitForRendezvousConnection(CONNECTION_TIME);

		netPeerGroup = manager.getNetPeerGroup();
		System.out.println("Setze peergroup auf "+netPeerGroup);
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
			discoveryService.publish(netPeerGroup.getPeerAdvertisement(),13000,13000);
			discoveryService.remotePublish(netPeerGroup.getPeerAdvertisement(),13000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void stopNetwork() {
		manager.stopNetwork();

	}

	@Override
	protected void initOperatorPeerListener() {
		operatorPeerListener = new OperatorPeerListenerJxtaImpl();
		
	}

	@Override
	protected void initAliveHandler() {
		aliveHandler = new AliveHandlerJxtaImpl();
		
	}

	@Override
	protected void initBiddingStrategy() {
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl();
		
	}

	@Override
	protected void initHotPeerFinder() {
		this.hotPeerFinder = new HotPeerListenerJxtaImpl();
		
	}

	@Override
	protected void initQueryResultHandler(ICompiler compiler) {
		this.queryResultHandler = new QueryResultHandlerJxtaImpl(compiler);
		
	}

	@Override
	protected void initHotPeerStrategy() {
		this.hotPeerStrategy = new HotPeerStrategyRandom();
		
	}

	@Override
	protected void initBiddingHandlerStrategy() {
		this.biddingHandlerStrategy = new BiddingHandlerStrategyStandard();
	}

	@Override
	protected void initEventListener() {
		PipeAdvertisement adv = AdvertisementTools.getServerPipeAdvertisement(getNetPeerGroup());
		eventListener = new EventListenerJxtaImpl(adv);
	}


}
