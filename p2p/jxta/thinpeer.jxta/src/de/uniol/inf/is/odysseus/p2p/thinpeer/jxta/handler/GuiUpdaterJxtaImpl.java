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
package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.handler;

import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;


// TODO: Diese Klasse vollständig entfernen!

public class GuiUpdaterJxtaImpl implements IGuiUpdater {

	// Wie oft wird die GUI aktualisiert
	private int UPDATE_TIME = 1000;

	public GuiUpdaterJxtaImpl(ThinPeerJxtaImpl thinPeerJxtaImpl) {
	}

	@Override
	public void run() {

		while (true) {

			try {
				Thread.sleep(UPDATE_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.updateQueryList();
			this.updateAdminList();
			this.updateSourcesList();

		}
	}

	public void updateQueryList() {
//		DefaultListModel model = new DefaultListModel();
//		for (String qID : thinPeerJxtaImpl.getQueryIds()) {
//			P2PQuery q = thinPeerJxtaImpl.getQuery(qID);
//			model.addElement(q.getId() + "(" + getAllBiddings(q) + ") - "
//					+ q.getStatus());
//		}
//		thinPeerJxtaImpl.getGui().getQuerys().setModel(model);

	}

	public void updateAdminList() {
//		synchronized (thinPeerJxtaImpl.getAdminPeers()) {
//			DefaultListModel model = new DefaultListModel();
//			for (String s : thinPeerJxtaImpl.getAdminPeers().keySet()) {
//				model.addElement(((ExtendedPeerAdvertisement) thinPeerJxtaImpl
//						.getAdminPeers().get(s)).getPeerName().substring(19));
//			}
//			thinPeerJxtaImpl.setAdminPeersModel(model);
//		}
	}

	public void updateSourcesList() {
//		synchronized (thinPeerJxtaImpl.getAdminPeers()) {
//			DefaultListModel model = new DefaultListModel();
//			for (SourceAdvertisement s : thinPeerJxtaImpl.getSources()) {
//				model.addElement(s.getSourceName()+" from "+ s.getPeerID()+"");
//			}
//			thinPeerJxtaImpl.setSourcesModel(model);
//		}
	}

	public int getAllBiddings(P2PQuery query) {
		int biddingCounter = 0;
		for (Subplan sub : query.getSubPlans().values()) {
			biddingCounter += sub.getBiddings().size();
		}
		return biddingCounter;
	}

}
