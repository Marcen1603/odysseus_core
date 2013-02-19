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

package de.uniol.inf.is.odysseus.p2p_new.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;

import net.jxta.content.ContentTransfer;
import net.jxta.content.TransferException;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.ContentShareAdvertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class PeerManager implements IPeerManager, DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);
	private static final int PEER_DISCOVER_INTERVAL_MILLIS = 5 * 1000;

	private DiscoveryService discoveryService;
	private PeerDiscoveryThread peerDiscoveryThread;

	public final void activate() {
		this.discoveryService = P2PNewPlugIn.getDiscoveryService();

		discoveryService.addDiscoveryListener(this);
		peerDiscoveryThread = new PeerDiscoveryThread(discoveryService, PEER_DISCOVER_INTERVAL_MILLIS);
		peerDiscoveryThread.start();

		LOG.debug("PeerManager activated");
	}

	public final void deactivate() {
		peerDiscoveryThread.stopRunning();
		discoveryService.removeDiscoveryListener(this);

		LOG.debug("PeerManager deactivated");
	}

	@Override
	public final void discoverPeers() {
		peerDiscoveryThread.doJob();
	}

	@Override
	public final ImmutableList<String> getPeers() {
		try {
			Enumeration<Advertisement> localAdvs = discoveryService.getLocalAdvertisements(DiscoveryService.PEER, null, null);
			return extractPeerNames(localAdvs);
		} catch (IOException ex) {
			LOG.error("Could not get list of peers", ex);
			return ImmutableList.of();
		}

	}

	@Override
	public final void discoveryEvent(DiscoveryEvent event) {
		try {
			DiscoveryResponseMsg response = event.getResponse();
			Enumeration<Advertisement> advs = response.getAdvertisements();
			while (advs.hasMoreElements()) {
				Advertisement adv = advs.nextElement();

				if (adv instanceof PeerAdvertisement) {
					processPeerAdvertisement((PeerAdvertisement) adv);
				} else if (adv instanceof ContentShareAdvertisement) {
					processContentShareAdvertisement((ContentShareAdvertisement) adv);
				} else {
					processOtherAdvertisement(adv);
				}

			}
		} catch (IOException exception) {
			LOG.error("Could not process discovery event", exception);
		}
	}

	protected void processPeerAdvertisement(PeerAdvertisement adv) throws IOException {
		LOG.debug("Got PeerAdvertisement from peer {}", adv.getName());
	}

	protected void processOtherAdvertisement(Advertisement adv) {
	}

	protected synchronized void processContentShareAdvertisement(ContentShareAdvertisement adv) throws IOException {
		LOG.debug("Got content share advertisement {}", adv.getContentID());

		try {
			ContentTransfer transfer = P2PNewPlugIn.getContentService().retrieveContent(adv.getContentID());
			if (transfer != null) {
				transfer.startSourceLocation();
				File f = new File("test.tst");
				transfer.startTransfer(f);
				transfer.waitFor();
				
				byte[] data = Files.readAllBytes(f.toPath());
				String srcName = new String(data);
				LOG.debug("Got source: {}", srcName);
				
			} else {
				LOG.error("Could not retrieve content of ContentID {}", adv.getContentID());
			}
		} catch (InterruptedException | TransferException ex) {
			LOG.error("Could not retrieve content of ContentID {}", adv.getContentID());
		}
	}

	private static ImmutableList<String> extractPeerNames(Enumeration<Advertisement> localAdvs) {
		ImmutableList.Builder<String> names = ImmutableList.builder();

		while (localAdvs.hasMoreElements()) {
			PeerAdvertisement peerAdvertisement = (PeerAdvertisement) localAdvs.nextElement();
			names.add(peerAdvertisement.getName());
		}

		return names.build();
	}
}
