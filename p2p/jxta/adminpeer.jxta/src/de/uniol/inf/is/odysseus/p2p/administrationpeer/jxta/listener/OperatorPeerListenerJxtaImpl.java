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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IOperatorPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;

/**
 * Hier werden im Netzwerk gefundene Operator-Peers zu den bekannten hinzugefügt
 * 
 * @author Mart Köhler
 *
 */
public class OperatorPeerListenerJxtaImpl implements IOperatorPeerListener,
		DiscoveryListener {
	// Wie oft soll nach AdminPeers gesucht werden
	private int WAIT_TIME = 10000;

	// Wieviel Advertisements pro Peer
	private int ADVS_PER_PEER = 6;

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public OperatorPeerListenerJxtaImpl(AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			administrationPeerJxtaImpl.getOperatorPeers().clear();
			try {
				administrationPeerJxtaImpl.getDiscoveryService()
						.getLocalAdvertisements(DiscoveryService.ADV, "type",
								"OperatorPeer");
			} catch (IOException e) {
				e.printStackTrace();
			}

			administrationPeerJxtaImpl.getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "OperatorPeer", ADVS_PER_PEER, this);
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
				synchronized (administrationPeerJxtaImpl
						.getOperatorPeers()) {
					administrationPeerJxtaImpl.getOperatorPeers()
							.put(adv.getPeerId().toString(), adv);
				}

			}
		}

	}

}
