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

import java.io.IOException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.PeerAdvertisement;

import com.google.common.base.Preconditions;

public class PeerAdvertisementThread extends RepeatingJobThread {

	private final DiscoveryService discoveryService;
	private final PeerID peerID;
	private final PeerGroupID groupID;
	
	private PeerAdvertisement peerAdv;
	
	public PeerAdvertisementThread(int executionIntervalMillis, DiscoveryService discoveryService, PeerID peerID, PeerGroupID groupID) {
		super(executionIntervalMillis, "Peer advertisement");
		this.discoveryService = Preconditions.checkNotNull(discoveryService, "Discovery service must not be null!");
		this.peerID =Preconditions.checkNotNull(peerID, "Peer id must not be null!");
		this.groupID = Preconditions.checkNotNull(groupID, "Group id must not be null!");
	}
	
	@Override
	public void beforeJob() {
		peerAdv = (PeerAdvertisement)AdvertisementFactory.newAdvertisement(PeerAdvertisement.getAdvertisementType());
		peerAdv.setPeerGroupID(groupID);
		peerAdv.setName("PEER_ADV:HELLO");
		peerAdv.setDescription("Discovering peer advertisement");
		peerAdv.setPeerID(peerID);
	}
	
	@Override
	public void doJob() {
		try {
			discoveryService.publish(peerAdv, 3000, 3000);
			discoveryService.remotePublish(peerAdv, 3000);		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
