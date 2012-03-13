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
package de.uniol.inf.is.odysseus.p2p.superpeer.jxta;

import java.io.File;
import java.io.IOException;

import net.jxta.document.AdvertisementFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import de.uniol.inf.is.odysseus.p2p.superpeer.AbstractSuperPeer;
import de.uniol.inf.is.odysseus.p2p.superpeer.jxta.SuperPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.CacheTool;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryTranslationSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;

public class SuperPeerJxtaImpl extends AbstractSuperPeer {

    public static NetworkManager manager = null;

    public static PeerGroup netPeerGroup;

    public static NetworkConfigurator networkConfigurator;

    // Wieviele Millisekunden soll probiert werden eine Verbindung
    // mit dem P2P-Netzwerk aufzubauen bevor aufgegeben wird.
    static private final int CONNECTION_TIME = 12000;

    static private final int tcpPort = 10801;

    static private final int httpPort = 10802;

    static private final String name = "SuperPeer1";

    static private final boolean multicast = false;

    public SuperPeerJxtaImpl() {

        CacheTool.checkForExistingConfigurationDeletion("SuperPeer_" + name, new File(new File(".cache"), "SuperPeer_" + name));

        try {
            manager = new NetworkManager(NetworkManager.ConfigMode.SUPER, "SuperPeer_" + name, new File(new File(".cache"), "SuperPeer_" + name).toURI());

            manager.getConfigurator().clearRelaySeeds();
            manager.getConfigurator().clearRendezvousSeeds();

            manager.getConfigurator().clearRelaySeeds();
            manager.getConfigurator().clearRendezvousSeeds();

            manager.getConfigurator().setTcpPort(tcpPort);
            manager.getConfigurator().setHttpPort(httpPort);
            manager.getConfigurator().setHttpEnabled(true);
            manager.getConfigurator().setHttpOutgoing(true);
            manager.getConfigurator().setHttpIncoming(true);
            manager.getConfigurator().setTcpEnabled(true);
            manager.getConfigurator().setTcpOutgoing(true);
            manager.getConfigurator().setTcpIncoming(true);
            manager.getConfigurator().setUseMulticast(multicast);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Override
    public void startNetwork() {
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

        AdvertisementFactory.registerAdvertisementInstance(QueryTranslationSpezification.getAdvertisementType(), new QueryTranslationSpezification.Instantiator());

        manager.waitForRendezvousConnection(CONNECTION_TIME);

        AdvertisementFactory.registerAdvertisementInstance(QueryTranslationSpezification.getAdvertisementType(), new QueryTranslationSpezification.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(SourceAdvertisement.getAdvertisementType(), new SourceAdvertisement.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(QueryExecutionSpezification.getAdvertisementType(), new QueryExecutionSpezification.Instantiator());

        AdvertisementFactory.registerAdvertisementInstance(ExtendedPeerAdvertisement.getAdvertisementType(), new ExtendedPeerAdvertisement.Instantiator());

    }

    @Override
    public void startPeer() {
        startNetwork();
    }

    @Override
    public void stopPeer() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getName() {
        return name;
    }

}
