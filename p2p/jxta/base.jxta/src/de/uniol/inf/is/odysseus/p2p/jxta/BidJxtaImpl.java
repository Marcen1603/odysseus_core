/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class BidJxtaImpl extends Bid{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2911499561644551179L;
	private PipeAdvertisement responseSocket;
	private PeerAdvertisement peer;
	
	public BidJxtaImpl(){}
		
	public BidJxtaImpl(PipeAdvertisement responseSocket, Date date, String peerId,
			int bidValue) {
		super(date, peerId, bidValue);
		this.responseSocket = responseSocket;
	}

	public BidJxtaImpl(PipeAdvertisement responseSocket, Date date,
			PeerAdvertisement peerAdv) {
		super(date);
		this.responseSocket = responseSocket;
		this.peer = peerAdv;
	}

	public PeerAdvertisement getPeer() {
		return peer;
	}

	public void setPeer(PeerAdvertisement peer) {
		this.peer = peer;
	}

	public PipeAdvertisement getResponseSocket() {
		return responseSocket;
	}

	public void setResponseSocket(PipeAdvertisement responseSocket) {
		this.responseSocket = responseSocket;
	}

}
