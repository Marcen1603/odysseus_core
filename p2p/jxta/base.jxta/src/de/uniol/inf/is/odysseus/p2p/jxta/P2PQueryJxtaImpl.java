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
package de.uniol.inf.is.odysseus.p2p.jxta;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class P2PQueryJxtaImpl extends P2PQuery{
	
	private static final long serialVersionUID = -8784146315565652081L;
	private PipeAdvertisement responseSocketThinPeer;
	private PipeAdvertisement adminPeerPipe;
	private Map<String,BidJxtaImpl> adminPeerBidding;


	public Map<String, BidJxtaImpl> getAdminPeerBidding() {
		return adminPeerBidding;
	}

	public P2PQueryJxtaImpl() {
		this.adminPeerBidding = new HashMap<String, BidJxtaImpl>();
	}

	public PipeAdvertisement getResponseSocketThinPeer() {
		return responseSocketThinPeer;
	}

	public void setResponseSocketThinPeer(PipeAdvertisement responseSocketThinPeer) {
		this.responseSocketThinPeer = responseSocketThinPeer;
	}


	public void addBidding(PipeAdvertisement socket, String peerId, String subPlanId, String bid){
		BidJxtaImpl bidElem = new BidJxtaImpl(socket, new Date(), peerId, bid);
		getSubPlans().get(subPlanId).addBit(bidElem);
	}
	
	public void addAdminBidding(PipeAdvertisement socket, PeerAdvertisement peerAdv){
		synchronized(this.adminPeerBidding){
			BidJxtaImpl bid = new BidJxtaImpl(socket, new Date(), peerAdv);
			this.adminPeerBidding.put(""+socket.getPipeID(), bid);
		}
	}
		
	

	
	public void setAdminPeerPipe(PipeAdvertisement adminPipe) {
		this.adminPeerPipe = adminPipe;
	}

	public PipeAdvertisement getAdminPeerPipe() {
		return adminPeerPipe;
	}
	


}
