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

import net.jxta.discovery.DiscoveryService;

import com.google.common.base.Preconditions;

public class PeerDiscoveryThread extends RepeatingJobThread {

	private static final String THREAD_NAME = "Peer discovery thread";
	
	private final DiscoveryService discoveryService;
	
	public PeerDiscoveryThread(DiscoveryService discoveryService, int discoverIntervalMillis) {
		super(discoverIntervalMillis, THREAD_NAME);
		this.discoveryService = Preconditions.checkNotNull(discoveryService);
	}

	@Override
	public void doJob() {
		discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, null);
	}
}
