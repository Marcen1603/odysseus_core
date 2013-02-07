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

import java.io.IOException;
import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class PeerManager implements IPeerManager, DiscoveryListener, IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerManager.class);
	private static final int PEER_DISCOVER_INTERVAL_MILLIS = 30 * 1000;

	private DiscoveryService discoveryService;
	private PeerDiscoveryThread peerDiscoveryThread;
	private IDataDictionary dataDictionary;

	public void activate() {
		this.discoveryService = P2PNewPlugIn.getDiscoveryService();

		discoveryService.addDiscoveryListener(this);
		peerDiscoveryThread = new PeerDiscoveryThread(discoveryService, PEER_DISCOVER_INTERVAL_MILLIS);
		peerDiscoveryThread.start();
	}

	public void deactivate() {
		peerDiscoveryThread.stopRunning();
		discoveryService.removeDiscoveryListener(this);
	}

	public final void bindDataDictionary(IDataDictionary dd) {
		dataDictionary = dd;
		dataDictionary.addListener(this);

		LOG.info("Data dictionary bound: {}", dd);
	}

	public final void unbindDataDictionary(IDataDictionary dd) {
		if (dd == dataDictionary) {
			dataDictionary.removeListener(this);
			dataDictionary = null;

			LOG.info("Data dictionary unbound: {}", dd);
		}
	}

	@Override
	public final void discoverPeers() {
		peerDiscoveryThread.doJob();
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
				} else {
					processOtherAdvertisement(adv);
				}

			}
		} catch (IOException exception) {
			LOG.error("Could not process discovery event", exception);
		}
	}

	@Override
	public ImmutableList<String> getPeers() {
		try {
			Enumeration<Advertisement> localAdvs = discoveryService.getLocalAdvertisements(DiscoveryService.PEER, null, null);
			return extractPeerNames(localAdvs);
		} catch (IOException ex) {
			LOG.error("Could not get list of peers", ex);
			return ImmutableList.of();
		}

	}

	protected void processPeerAdvertisement(PeerAdvertisement adv) throws IOException {
		LOG.debug("Got PeerAdvertisement from peer {}", adv.getName());
		discoveryService.publish(adv, PEER_DISCOVER_INTERVAL_MILLIS, PEER_DISCOVER_INTERVAL_MILLIS);
	}

	protected void processOtherAdvertisement(Advertisement adv) {
		LOG.debug("Got advertisement of class {} : {} ", adv.getClass(), adv);
	}

	private static ImmutableList<String> extractPeerNames(Enumeration<Advertisement> localAdvs) {
		ImmutableList.Builder<String> names = ImmutableList.builder();

		while (localAdvs.hasMoreElements()) {
			PeerAdvertisement peerAdvertisement = (PeerAdvertisement) localAdvs.nextElement();
			names.add(peerAdvertisement.getName());
		}

		return names.build();
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		LOG.info("Added view: name={}, op={}", name, op);
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		LOG.info("Removed view: name={}, op={}", name, op);
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		LOG.info("Data dictionary changed");
	}
}
