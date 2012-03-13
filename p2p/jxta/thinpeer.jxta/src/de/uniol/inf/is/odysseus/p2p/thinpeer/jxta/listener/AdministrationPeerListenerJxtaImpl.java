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
package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.listener;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.IDiscoveryServiceProvider;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerDiscoverer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;

/**
 * Class to find admin peers in network
 * @author  Mart Koehler, Marco Grawunder
 *
 */
public class AdministrationPeerListenerJxtaImpl implements DiscoveryListener, IAdministrationPeerDiscoverer {

	private int WAIT_TIME = 10000;
	// Advertisments per Peer
	private int ADVS_PER_PEER = 6;

	private IDiscoveryServiceProvider discoveryServiceProvider;
	private IAdministrationPeerListener peerListener;

	public AdministrationPeerListenerJxtaImpl(IAdministrationPeerListener peerListener,
			IDiscoveryServiceProvider discoveryServiceProvider) {
		this.peerListener = peerListener;
		this.discoveryServiceProvider = discoveryServiceProvider;
	}

	@Override
    public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
//			discoveryServiceProvider.getAdminPeers().clear();
			try {
				discoveryServiceProvider.getDiscoveryService()
						.getLocalAdvertisements(DiscoveryService.ADV, "type",
								"AdministrationPeer");
			} catch (IOException e) {
				e.printStackTrace();
			}
			discoveryServiceProvider.getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "AdministrationPeer", ADVS_PER_PEER, this);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				Object o = en.nextElement();
				if (!(o instanceof ExtendedPeerAdvertisement)) {
					continue;
				}
				ExtendedPeerAdvertisement adv = (ExtendedPeerAdvertisement) o;
				peerListener.foundAdminPeers(adv);
			}
		}

	}

}
