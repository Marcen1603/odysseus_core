package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.SocketServerListener;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.JxtaConfiguration;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.thinpeer.AbstractThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.GuiUpdaterJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryNegotiationMessageHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryPublisherHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.AdministrationPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy.StandardIdGenerator;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class ThinPeerJxtaImpl extends AbstractThinPeer {

	private DiscoveryService discoveryService;
	
	private PipeAdvertisement serverResponseAddress;

	private NetworkManager manager = null;

	private PeerGroup netPeerGroup;

	private HashMap<String, SourceAdvertisement> sources = new HashMap<String, SourceAdvertisement>();

//	private static ThinPeerJxtaImpl instance = null;
//
//	public static ThinPeerJxtaImpl getInstance() {
//		if (instance == null)
//			instance = new ThinPeerJxtaImpl();
//		return instance;
//	}


	@Override
	public HashMap<String, Object> getAdminPeers() {
		return adminPeers;
	}

	ThinPeerJxtaImpl() {
		super(new SocketServerListener());
		getSocketServerListener().setPeer(this);
		// TODO: Nutzer auslesen
		GlobalState.setActiveUser(UserManagement.getInstance().getSuperUser());
		// TODO: Müssen sich die Namen unterscheiden? Eigentlich nicht, ist nur ein Admin Peer to JVM ..
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
		
		String configFile = System.getenv("PeerConfig");
		JxtaConfiguration configuration = null;
		
		// If no file given try first Odysseus-Home
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = OdysseusDefaults.getHomeDir()
					+ "/ThinPeer1Config.xml";
			try {
				configuration = new JxtaConfiguration(configFile);
			} catch (IOException e) {
				configFile = null;
			}

		}
		// If still no configuration found try default config-File
		// TODO: Does not work currently ...
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = "/config/ThinPeer1Config.xml";
		}

		// JxtaConfiguration einlesen
		try {
			configuration = new JxtaConfiguration(configFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		

		System.setProperty("net.jxta.logging.Logging", configuration.getLogging());
		String name = configuration.getName();
		if (configuration.isRandomName()) {
			name = "" + name + "" + System.currentTimeMillis();
		}
		try {
			CacheTool.checkForExistingConfigurationDeletion("ThinPeer_" + name,
					new File(new File(".cache"), "ThinPeer_" + name));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		ConfigMode peerMode = null;

		if (configuration.isUseSuperPeer()) {
			peerMode = NetworkManager.ConfigMode.EDGE;
		} else {
			peerMode = NetworkManager.ConfigMode.ADHOC;
		}

		try {
			manager = new NetworkManager(peerMode, "ThinPeer_" + name,
					new File(new File(".cache"), "ThinPeer_" + name).toURI());
			manager.getConfigurator().clearRelaySeeds();
			manager.getConfigurator().clearRendezvousSeeds();
			if (configuration.isUseSuperPeer()) {
				manager.getConfigurator().clearRelaySeeds();
				manager.getConfigurator().clearRendezvousSeeds();

				try {
					manager.getConfigurator().addSeedRendezvous(
							new URI(configuration.getHttpRendezvousUri()));
					manager.getConfigurator().addSeedRelay(
							new URI(configuration.getHttpRelayUri()));
					manager.getConfigurator().addSeedRendezvous(
							new URI(configuration.getTcpRendezvousUri()));
					manager.getConfigurator().addSeedRelay(
							new URI(configuration.getTcpRelayUri()));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			manager.getConfigurator().setTcpPort(configuration.getTcpPort());
			manager.getConfigurator().setHttpPort(configuration.getHttpPort());
			manager.getConfigurator().setHttpEnabled(configuration.isHttp());
			manager.getConfigurator().setHttpOutgoing(configuration.isHttp());
			manager.getConfigurator().setHttpIncoming(configuration.isHttp());
			manager.getConfigurator().setTcpEnabled(configuration.isTcp());
			manager.getConfigurator().setTcpOutgoing(configuration.isTcp());
			manager.getConfigurator().setTcpIncoming(configuration.isTcp());
			manager.getConfigurator().setUseMulticast(configuration.isMulticast());

			if (!configuration.getTcpInterfaceAddress().equals("")) {
				manager.getConfigurator().setTcpInterfaceAddress(
						configuration.getTcpInterfaceAddress());
			}

			if (!configuration.getHttpInterfaceAddress().equals("")) {
				manager.getConfigurator().setTcpInterfaceAddress(
						configuration.getHttpInterfaceAddress());
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

		manager.waitForRendezvousConnection(configuration.getConnectionTime());

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

	public HashMap<String, SourceAdvertisement> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, SourceAdvertisement> sources) {
		this.sources = sources;
	}

	@Override
	protected void initGuiUpdater() {
		this.guiUpdater = new GuiUpdaterJxtaImpl(this);
	}

	@Override
	protected void initAdministrationPeerListener() {
		administrationPeerListener = new AdministrationPeerListenerJxtaImpl(this);
	}

	@Override
	protected void initSourceListener() {
		sourceListener = new SourceListenerJxtaImpl(this);
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
	}
	
	@Override
	protected void initIdGenerator() {
		//this.idGenerator = new RandomIdGenerator();
		this.idGenerator = new StandardIdGenerator(this.serverResponseAddress.getID()+"");
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
			registerMessageHandler(new QueryNegotiationMessageHandler(this));


	}


	@Override
	public void initLocalExecutionHandler() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initMessageSender() {
		setMessageSender(new JxtaMessageSender());
	}




}
