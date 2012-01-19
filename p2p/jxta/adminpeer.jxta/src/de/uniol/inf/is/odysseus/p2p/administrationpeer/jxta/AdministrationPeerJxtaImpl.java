/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.AbstractAdministrationPeer;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler.APQueryBitResultHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler.AliveHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.APQuerySpezificationListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.HotPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.OperatorPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy.HotPeerStrategyRandom;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy.MaxQueryBiddingStrategyJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
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
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class AdministrationPeerJxtaImpl extends AbstractAdministrationPeer {

	static Logger logger = LoggerFactory
			.getLogger(AdministrationPeerJxtaImpl.class);

	static Logger getLogger() {
		return logger;
	}

	public HashMap<String, ExtendedPeerAdvertisement> getOperatorPeers() {
		return operatorPeers;
	}

	public void setOperatorPeers(
			HashMap<String, ExtendedPeerAdvertisement> operatorPeers) {
		this.operatorPeers = operatorPeers;
	}

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

	private MaxQueryBiddingStrategyJxtaImpl biddingStrategy;

	public void activate() {
		getLogger().debug("Activate Admin Peer");

		// TODO: Nutzer auslesen
		String username = "";
		String password = "";
		ISession user = UserManagement.getSessionmanagement().login(username, password.getBytes());
		GlobalState.setActiveSession("",user);
		// TODO: Müssen sich die Namen unterscheiden? Eigentlich nicht, ist nur
		// ein Admin Peer to JVM ..
		GlobalState.setActiveDatadictionary(DataDictionaryFactory
				.getDefaultDataDictionary("AdminPeer"));

		// TODO: Read from Config-File
		startPeer();
		getDistributionProvider().initializeService();
		getLogger().info("Administration Peer started");
	}

	// fuer die korrekte Nutzung in OSGi muss der Konstruktor public sein.
	public AdministrationPeerJxtaImpl() {
		super(new SocketServerListener(), Log.getInstance());
		getSocketServerListener().setPeer(this);
		getLogger().debug("Created Admin Peer");
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
		// TODO: Make the strategy and parameter configurable
		Random rand = new Random(System.currentTimeMillis());
		this.biddingStrategy = new MaxQueryBiddingStrategyJxtaImpl(
				this, rand.nextInt(10)+1, (rand.nextInt(10)+1)*10);
		querySpezificationListener = new APQuerySpezificationListenerJxtaImpl(
				(JxtaMessageSender) getMessageSender(), this,
				biddingStrategy, Log.getInstance());

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
	protected synchronized void startNetwork() {
		getLogger().info("Starting Peer Network");

		JxtaConfiguration configuration = null;
		String configFile = System.getenv("PeerConfig");
		if (configFile == null && System.getenv("PeerConfigFile") != null && System.getenv("PeerConfigFile").length() > 0){
			configFile = OdysseusDefaults.getHomeDir()+"/"+System.getenv("PeerConfigFile");
		}
		// If no file given try first Odysseus-Home
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = OdysseusDefaults.getHomeDir()
					+ "/AdminPeer1Config.xml";
			try {
				configuration = new JxtaConfiguration(configFile);
			} catch (IOException e) {
				configFile = null;
			}

		}
		// If still no configuration found try default config-File
		// TODO: Does not work currently ...
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = "/config/AdminPeer1Config.xml";
		}

		if (configuration == null) {
			// JxtaConfiguration einlesen
			try {
				configuration = new JxtaConfiguration(configFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot initialize peer");
			}
		}

		setName(configuration.getName());

		System.setProperty("net.jxta.logging.Logging",
				configuration.getLogging());
		String name = configuration.getName();
		if (configuration.isRandomName()) {
			name = "" + name + "" + System.currentTimeMillis();
		}

		try {
			CacheTool.checkForExistingConfigurationDeletion(
					"AdministrationPeer_" + name, new File(new File(".cache"),
							"AdministrationPeer_" + name));
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
			manager = new NetworkManager(peerMode,
					"AdministrationPeer_" + name, new File(new File(".cache"),
							"AdministrationPeer_" + name).toURI());

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
			manager.getConfigurator().setUseMulticast(
					configuration.isMulticast());

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
				if (!manager.isStarted()) {
					manager.startNetwork();
				}
			} catch (PeerGroupException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		manager.waitForRendezvousConnection(configuration.getConnectionTime());
		netPeerGroup = manager.getNetPeerGroup();
		PeerGroupTool.setPeerGroup(netPeerGroup);
		discoveryService = netPeerGroup.getDiscoveryService();
		pipeService = netPeerGroup.getPipeService();
		AdvertisementFactory.registerAdvertisementInstance(
				QueryTranslationSpezification.getAdvertisementType(),
				new QueryTranslationSpezification.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				SourceAdvertisement.getAdvertisementType(),
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
	public void initLocalMessageHandler() {
		registerMessageHandler(new APQueryBitResultHandlerJxtaImpl(this));
	}

	@Override
	public void initLocalExecutionHandler() {
		// TODO: Anders Loesen
		for (IExecutionHandler<?> h : getExecutionHandler()) {
			if (h.getProvidedLifecycle() == Lifecycle.NEW) {
				h.setPeer(this);
			}
		}
	}

	@Override
	public void initMessageSender() {
		setMessageSender(new JxtaMessageSender());
	}

	@Override
	protected void afterQueryRemoval(String queryId) {
		// TODO Auto-generated method stub
		
	}

}
