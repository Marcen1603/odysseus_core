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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;

public class PeerDiscoveryThread extends Thread implements DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerDiscoveryThread.class);
	private static final String THREAD_NAME = "Peer discovery thread";
	
	private final DiscoveryService discoveryService;
	private final int discoverInterval;
	
	private boolean isRunning = true;
	
	public PeerDiscoveryThread(DiscoveryService discoveryService, int discoverIntervalMillis) {
		this.discoveryService = Preconditions.checkNotNull(discoveryService);
		Preconditions.checkArgument(discoverIntervalMillis > 0, "Discover interval for other peers must be positive!");
		
		this.discoverInterval = discoverIntervalMillis;
		
		this.discoveryService.addDiscoveryListener(this);		
		setName(THREAD_NAME);
		setDaemon(true);
	}

	@Override
	public void run() {
		LOG.info("Beginning discovering peers");
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 1, null);

		while( isRunning ) {
			
			trySleep(discoverInterval);
			
			discoveryService.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", "Odysseus Peer", 1);
		}
		LOG.info("Stopping discovering peers");
	}
	
	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		LOG.info("Got discovery event: {}", event);
	}
	
	public final void stopRunning() {
		discoveryService.removeDiscoveryListener(this);
		isRunning = false;
	}
	
	private static void trySleep(int lengthMillis) {
		try {
			Thread.sleep(lengthMillis);
		} catch (InterruptedException ex) {}
	}
	
}
