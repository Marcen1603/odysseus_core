/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.p2p_new;

import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

public class PeerManager implements IPeerManager, DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);
	private static final int PEER_DISCOVER_INTERVAL_MILLIS = 30 * 1000;

	private final DiscoveryService discoveryService;
	private final PeerDiscoveryThread peerDiscoveryThread;
	private final List<String> peerList = Lists.newArrayList(); 

	public PeerManager(DiscoveryService discoveryService) {
		this.discoveryService = Preconditions.checkNotNull(discoveryService);

		discoveryService.addDiscoveryListener(this);
		peerDiscoveryThread = new PeerDiscoveryThread(discoveryService, PEER_DISCOVER_INTERVAL_MILLIS);
		peerDiscoveryThread.start();
	}

	public void stop() {
		discoveryService.removeDiscoveryListener(this);
		peerDiscoveryThread.stopRunning();
	}

	@Override
	public void discoverPeers() {
		peerDiscoveryThread.doJob();
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		DiscoveryResponseMsg response = event.getResponse();
		Enumeration<Advertisement> advs = response.getAdvertisements();
		while (advs.hasMoreElements()) {
			Advertisement adv = advs.nextElement();

			if (adv instanceof PeerAdvertisement) {
				processPeerAdvertisement((PeerAdvertisement) adv);
			} else {
				processOtherAdvertisement(adv);
			}

		}
	}

	@Override
	public ImmutableList<String> getPeers() {
		return ImmutableList.copyOf(peerList);
	}

	protected void processPeerAdvertisement(PeerAdvertisement adv) {
		LOG.info("Got PeerAdvertisement {}", adv);
	}

	protected void processOtherAdvertisement(Advertisement adv) {
		LOG.info("Got advertisement of class {} : {} ", adv.getClass(), adv);
	}

}
