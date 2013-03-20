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

package de.uniol.inf.is.odysseus.p2p_new.adv;

import net.jxta.discovery.DiscoveryService;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class DiscoveryThread extends RepeatingJobThread {

	private static final String THREAD_NAME = "Advertisement discovery";

	public DiscoveryThread(long discoverIntervalMillis) {
		super(discoverIntervalMillis, THREAD_NAME);
	}

	@Override
	public void doJob() {
		P2PNewPlugIn.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 0, null);
		P2PNewPlugIn.getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99, null);
	}
}
