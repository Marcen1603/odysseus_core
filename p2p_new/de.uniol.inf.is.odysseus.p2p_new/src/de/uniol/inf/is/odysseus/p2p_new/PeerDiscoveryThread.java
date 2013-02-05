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

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class PeerDiscoveryThread extends RepeatingJobThread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerDiscoveryThread.class);
	private static final String THREAD_NAME = "Peer discovery thread";
	
	private final DiscoveryService discoveryService;
	
	public PeerDiscoveryThread(DiscoveryService discoveryService, int discoverIntervalMillis) {
		super(discoverIntervalMillis, THREAD_NAME);
		this.discoveryService = Preconditions.checkNotNull(discoveryService);
	}

	@Override
	public void beforeJob() {
	}
	
	@Override
	public void doJob() {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 1, this);
	}
	
	@Override
	public void afterJob() {
		discoveryService.removeDiscoveryListener(this);
	}
	
	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		DiscoveryResponseMsg response = event.getResponse();
		Enumeration<Advertisement> advs = response.getAdvertisements();
		while( advs.hasMoreElements() ) {
			Advertisement adv = advs.nextElement();
			
			if( adv instanceof PeerAdvertisement) {
				PeerAdvertisement peerAdv = (PeerAdvertisement)adv;
				LOG.info(peerAdv.getName());
			}
			
		}
		LOG.info("\n");
	}
}
