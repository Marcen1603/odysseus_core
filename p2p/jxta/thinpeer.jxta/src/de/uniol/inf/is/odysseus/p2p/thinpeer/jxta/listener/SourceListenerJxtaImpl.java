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

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.IDiscoveryServiceProvider;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceDiscoverer;

public class SourceListenerJxtaImpl implements ISourceDiscoverer,
		DiscoveryListener {

	// Wie oft soll nach neuen Quellen gesucht werden
	private int WAIT_TIME = 10000;

	// Wieviele Advertisements pro Peer
	private int ADVS_PER_PEER = 20;

	private ISourceDiscovererListener sourceDiscovererListener;
	private IDiscoveryServiceProvider discoveryServiceProvider;

	public SourceListenerJxtaImpl(ISourceDiscovererListener thinPeerJxtaImpl,
			IDiscoveryServiceProvider discoveryServiceProvider) {
		this.sourceDiscovererListener = thinPeerJxtaImpl;
		this.discoveryServiceProvider = discoveryServiceProvider;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			discoveryServiceProvider.getDiscoveryService().getRemoteAdvertisements(
					null, DiscoveryService.ADV, "sourceName", "*",
					ADVS_PER_PEER, this);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		SourceAdvertisement adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				try {
					Object temp2 = en.nextElement();
					if (temp2 instanceof SourceAdvertisement) {
						adv = (SourceAdvertisement) temp2;

						sourceDiscovererListener.foundNewSource(adv);
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

}
