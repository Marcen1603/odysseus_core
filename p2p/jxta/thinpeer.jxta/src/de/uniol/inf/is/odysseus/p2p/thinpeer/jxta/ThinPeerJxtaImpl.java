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
package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
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
import de.uniol.inf.is.odysseus.p2p.thinpeer.AbstractThinPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.GuiUpdaterJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryNegotiationMessageHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler.QueryPublisherHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.AdministrationPeerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.ISourceDiscovererListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener.SourceListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.strategy.StandardIdGenerator;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;

public class ThinPeerJxtaImpl extends AbstractThinPeer implements IAdministrationPeerListener, ISourceDiscovererListener, IDiscoveryServiceProvider {

    private DiscoveryService discoveryService;

    private PipeAdvertisement serverResponseAddress;

    private NetworkManager manager = null;

    private PeerGroup netPeerGroup;

    public ThinPeerJxtaImpl() {
        super(new SocketServerListener(), Log.getInstance());
        getSocketServerListener().setPeer(this);
        new Thread() {

            @Override
            public void run() {
                startPeer();
            }
        }.start();

    }

    @Override
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

        if (configFile == null && System.getenv("PeerConfigFile") != null && System.getenv("PeerConfigFile").length() > 0) {
            configFile = OdysseusConfiguration.getHomeDir() + "/" + System.getenv("PeerConfigFile");
        }

        // If no file given try first Odysseus-Home
        if (configFile == null || configFile.trim().length() == 0) {
            configFile = OdysseusConfiguration.getHomeDir() + "/ThinPeer1Config.xml";
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

        if (configuration == null) {
            // JxtaConfiguration einlesen
            try {
                configuration = new JxtaConfiguration(configFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not inint Thin Peer");
            }
        }

        setName(configuration.getName());

        System.setProperty("net.jxta.logging.Logging", configuration.getLogging());
        String name = configuration.getName();
        if (configuration.isRandomName()) {
            name = "" + name + "" + System.currentTimeMillis();
        }
        CacheTool.checkForExistingConfigurationDeletion("ThinPeer_" + name, new File(new File(".cache"), "ThinPeer_" + name));

        ConfigMode peerMode = null;

        if (configuration.isUseSuperPeer()) {
            peerMode = NetworkManager.ConfigMode.EDGE;
        } else {
            peerMode = NetworkManager.ConfigMode.ADHOC;
        }

        try {
            manager = new NetworkManager(peerMode, "ThinPeer_" + name, new File(new File(".cache"), "ThinPeer_" + name).toURI());
            manager.getConfigurator().clearRelaySeeds();
            manager.getConfigurator().clearRendezvousSeeds();
            if (configuration.isUseSuperPeer()) {
                manager.getConfigurator().clearRelaySeeds();
                manager.getConfigurator().clearRendezvousSeeds();

                try {
                    manager.getConfigurator().addSeedRendezvous(new URI(configuration.getHttpRendezvousUri()));
                    manager.getConfigurator().addSeedRelay(new URI(configuration.getHttpRelayUri()));
                    manager.getConfigurator().addSeedRendezvous(new URI(configuration.getTcpRendezvousUri()));
                    manager.getConfigurator().addSeedRelay(new URI(configuration.getTcpRelayUri()));
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
                manager.getConfigurator().setTcpInterfaceAddress(configuration.getTcpInterfaceAddress());
            }

            if (!configuration.getHttpInterfaceAddress().equals("")) {
                manager.getConfigurator().setTcpInterfaceAddress(configuration.getHttpInterfaceAddress());
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

        AdvertisementFactory.registerAdvertisementInstance(QueryTranslationSpezification.getAdvertisementType(), new QueryTranslationSpezification.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisement.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new QueryExecutionSpezification.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(ExtendedPeerAdvertisement.getAdvertisementType(), new ExtendedPeerAdvertisement.Instantiator());

        manager.waitForRendezvousConnection(configuration.getConnectionTime());

    }

    @Override
    public void stopNetwork() {
        netPeerGroup.stopApp();
        manager.stopNetwork();
    }

    @Override
    public void publishQuerySpezification(String query, String language, ISession user) {
        queryPublisher.publishQuerySpezification(idGenerator.generateId(), query, language, user);
    }

    @Override
    protected void initGuiUpdater() {
        this.guiUpdater = new GuiUpdaterJxtaImpl(this);
    }

    @Override
    protected void initAdministrationPeerListener() {
        administrationPeerDiscoverer = new AdministrationPeerListenerJxtaImpl(this, this);
    }

    @Override
    protected void initSourceListener() {
        // FIXME:
        // There must be a user associated to the source listener!
        ISession caller = null;
        sourceListener = new SourceListenerJxtaImpl(this, this, caller);
    }

    @Override
    protected void initQueryPublisher() {
        queryPublisher = new QueryPublisherHandlerJxtaImpl(this);
    }

    @Override
    public void sendQuerySpezificationToAdminPeer(String query, String language, String adminPeer, ISession user) {
        String queryId = idGenerator.generateId();
        queryPublisher.sendQuerySpezificationToAdminPeer(queryId, query, language, user, adminPeer);
    }

    @Override
    protected void initIdGenerator() {
        // this.idGenerator = new RandomIdGenerator();
        this.idGenerator = new StandardIdGenerator(this.serverResponseAddress.getID() + "");
    }

    private void setServerPipeAdvertisement(PipeAdvertisement serverPipeAdvertisement) {
        this.serverResponseAddress = serverPipeAdvertisement;
    }

    @Override
    public Object getServerResponseAddress() {
        return this.serverResponseAddress;
    }

    @Override
    protected void initServerResponseConnection() {
        setServerPipeAdvertisement(AdvertisementTools.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
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

    @Override
    public void foundAdminPeers(IExtendedPeerAdvertisement adv) {
        addOrUpdateAdminPeer(adv);
    }

    @Override
    public void foundNewSource(SourceAdvertisement adv, ISession caller) {
        addOrUpdateSources(adv, caller);
    }

    @Override
    public void addToDD(ISourceAdvertisement adv, IDataDictionary dd, ISession caller) {
        dd.addEntitySchema(adv.getSourceName(), null, caller);

        AccessAO source = new AccessAO(adv.getPeerID() + ":" + adv.getSourceName(), "P2PSource", null);
        try {
            dd.setStream(adv.getSourceName(), source, caller);
        } catch (DataDictionaryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
