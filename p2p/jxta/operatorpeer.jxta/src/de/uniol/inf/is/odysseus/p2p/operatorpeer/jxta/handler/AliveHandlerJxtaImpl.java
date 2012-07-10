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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler;

import java.io.IOException;

import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.ExtendedPeerAdvertisement;

public class AliveHandlerJxtaImpl implements IAliveHandler {
	static private final int WAIT_TIME = 10000;
	private OperatorPeerJxtaImpl peer;

	public AliveHandlerJxtaImpl(OperatorPeerJxtaImpl operatorPeerJxtaImpl) {
		peer = operatorPeerJxtaImpl;
	}

	@Override
	public void run() {

		ExtendedPeerAdvertisement adv = new ExtendedPeerAdvertisement();
		adv.setPeerName(peer.getNetPeerGroup()
				.getPeerName());
		adv.setPipe( peer.getServerResponseAddress().toString());
		adv.setType("OperatorPeer");
		adv.setPeerId(peer.getNetPeerGroup()
				.getPeerID().toString());
		while (true) {
			try {
				peer.getDiscoveryService()
						.publish(adv, 13000, 13000);
				peer.getDiscoveryService()
						.remotePublish(adv, 13000);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
