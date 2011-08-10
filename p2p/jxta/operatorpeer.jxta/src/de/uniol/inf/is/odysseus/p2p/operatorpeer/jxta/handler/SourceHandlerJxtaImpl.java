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
package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * Alle Streams und Views werden hier fuer die Verwendung im P2P Netzwerk vorbereitet und
 * in regelmaessigen Abstaenden ausgeschrieben.
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class SourceHandlerJxtaImpl implements ISourceHandler {

	private int LIFETIME = 60000;
	private OperatorPeerJxtaImpl peer = null;

	public SourceHandlerJxtaImpl(OperatorPeerJxtaImpl aPeer) {
		this.setPeer(aPeer);
	}

	@Override
	public void run() {

		ArrayList<SourceAdvertisement> advList = new ArrayList<SourceAdvertisement>();

		// Publish all sources
		while (true) {
			advList.clear();
			User user = GlobalState.getActiveUser(""); 
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			for (Entry<String, ILogicalOperator> v : dd
					.getStreamsAndViews(user)) {
				// Create source advertisement and add to publish list
				SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
						.newAdvertisement(SourceAdvertisement
								.getAdvertisementType());
				adv.setID(IDFactory.newPipeID(getPeer().getNetPeerGroup()
						.getPeerGroupID()));
				adv.setSourceName(v.getKey());
				adv.setPeer(getPeer().getNetPeerGroup().getPeerAdvertisement()
						.toString());
				String peerID = getPeer().getNetPeerGroup().getPeerAdvertisement().getID().toString();
				adv.setPeerID(peerID);
				adv.setSourceId(v.getKey());
				adv.setLogicalPlan(AdvertisementTools.toBase64String(v
						.getValue()));
				adv.setEntity(AdvertisementTools.toBase64String(dd.getEntity(
						v.getKey(), user)));
				adv.setSourceType(dd.getSourceType(v.getKey()));

				adv.setUser(AdvertisementTools.toBase64String(user));
				advList.add(adv);
			}

			for (SourceAdvertisement adv : advList) {
				try {
					getPeer().getDiscoveryService().publish(adv, LIFETIME,
							LIFETIME);
				} catch (IOException e) {
					e.printStackTrace();
				}
				getPeer().getDiscoveryService().remotePublish(adv, LIFETIME);
			}
			try {
				Thread.sleep(LIFETIME - 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;
		socketID = (PipeID) IDFactory.newPipeID(getPeer().getNetPeerGroup()
				.getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Source Distribution Socket");

		return advertisement;
	}

	public void setPeer(OperatorPeerJxtaImpl oPeer) {
		this.peer = oPeer;
	}

	public OperatorPeerJxtaImpl getPeer() {
		return peer;
	}

}
