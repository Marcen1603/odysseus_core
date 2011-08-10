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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import de.uniol.inf.is.odysseus.collection.IPair;
import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.SocketServerListener;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.JxtaConfiguration;
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

	static Logger logger = LoggerFactory.getLogger(OperatorPeerJxtaImpl.class);

	static Logger getLogger() {
		return logger;
	}
	JxtaConfiguration configuration;
	public DiscoveryService discoveryService;
	public NetworkManager manager = null;
	public PeerGroup netPeerGroup;
	public NetworkConfigurator networkConfigurator;
	public PipeService pipeService;
	private PipeAdvertisement serverResponseAddress;

	public void activate() {
		getLogger().info("OSGi Services loaded");

		String configFile = System.getenv("PeerConfig");
		// If no file given try first Odysseus-Home
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = OdysseusDefaults.getHomeDir()
					+ "/OperatorPeer1Config.xml";
			try {
				configuration = new JxtaConfiguration(configFile);
			} catch (IOException e) {
				configFile = null;
			}

		}
		// If still no configuration found try default config-File
		// TODO: Does not work currently ...
		if (configFile == null || configFile.trim().length() == 0) {
			configFile = "/config/OperatorPeer1Config.xml";
		}

		// JxtaConfiguration einlesen
		try {
			configuration = new JxtaConfiguration(configFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not init peer");
		}

		setName(configuration.getName());
		
		// TODO: User einlesen
		GlobalState.setActiveUser("",UserManagement.getInstance().getSuperUser());
		// TODO: Unterschiedliche Namen notwendig?
		GlobalState.setActiveDatadictionary(DataDictionaryFactory
				.getDefaultDataDictionary("OperatorPeer"));
		startPeer();
		getDistributionClient().initializeService();

	}

	public OperatorPeerJxtaImpl() {
		super(new SocketServerListener(), Log.getInstance());
		getSocketServerListener().setPeer(this);
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

		String name = configuration.getName();

		if (configuration.isRandomName()) {
			name = "" + name + "" + System.currentTimeMillis();
		}
		System.setProperty("net.jxta.logging.Logging",
				configuration.getLogging());

		try {
			CacheTool.checkForExistingConfigurationDeletion("OperatorPeer_"
					+ name,
					new File(new File(".cache"), "OperatorPeer_" + name));
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
			manager = new NetworkManager(peerMode, "OperatorPeer_" + name,
					new File(new File(".cache"), "OperatorPeer_" + name)
							.toURI());

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
			manager.getConfigurator().setUseMulticast(
					configuration.isMulticast());

			if (!configuration.getTcpInterfaceAddress().equals("")) {
				manager.getConfigurator().setTcpInterfaceAddress(
						configuration.getTcpInterfaceAddress());
			}

			if (!configuration.getHttpInterfaceAddress().equals("")) {
				manager.getConfigurator().setHttpInterfaceAddress(
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

		manager.waitForRendezvousConnection(configuration.getConnectionTime());

		netPeerGroup = manager.getNetPeerGroup();
		PeerGroupTool.setPeerGroup(netPeerGroup);
		this.discoveryService = this.getNetPeerGroup().getDiscoveryService();
		AdvertisementFactory.registerAdvertisementInstance(
				QueryTranslationSpezification.getAdvertisementType(),
				new QueryTranslationSpezification.Instantiator());

		AdvertisementFactory.registerAdvertisementInstance(
				SourceAdvertisement.getAdvertisementType(),
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
		this.aliveHandler = new AliveHandlerJxtaImpl(this);

	}

	@Override
	protected void initSources(AbstractOperatorPeer aPeer) {

		getSources()
				.put("nexmark:person2",
						new Pair<String, String>(
								"CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440",
								"CQL"));
		getSources()
				.put("nexmark:auction2",
						new Pair<String, String>(
								"CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441",
								"CQL"));
		getSources()
				.put("nexmark:bid2",
						new Pair<String, String>(
								"CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442",
								"CQL"));
		List<IQueryBuildSetting<?>> cfg = aPeer.getExecutor()
				.getQueryBuildConfiguration("Standard");
		;
		if (cfg == null) {
			getLogger().debug("No Query Build Configuration found!!!");
			return;
		}

		for (IPair<String, String> s : getSources().values()) {
			try {
				User user = GlobalState.getActiveUser("");
				IDataDictionary dd = GlobalState.getActiveDatadictionary();
				aPeer.getExecutor().addQuery(s.getE1(), s.getE2(), user, dd,
						cfg.toArray(new IQueryBuildSetting[0]));
			} catch (PlanManagementException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void initSourceHandler(AbstractOperatorPeer aPeer) {
		this.sourceHandler = new SourceHandlerJxtaImpl(
				(OperatorPeerJxtaImpl) aPeer);

	}

	@Override
	protected void initServerResponseConnection() {
		setServerPipeAdvertisement(AdvertisementTools
				.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
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
	}

	@Override
	public void initLocalExecutionHandler() {
	}

	@Override
	public void initMessageSender() {
		setMessageSender(new JxtaMessageSender());
	}

}