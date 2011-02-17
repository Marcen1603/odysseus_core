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

import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.OdysseusQueryAction;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.ErrorPopup;
import de.uniol.inf.is.odysseus.p2p.thinpeer.jxta.ThinPeerJxtaImpl;

public class QueryNegotiationMessageHandler extends AbstractJxtaMessageHandler {

	private ThinPeerJxtaImpl thinPeerJxtaImpl;

	public QueryNegotiationMessageHandler(ThinPeerJxtaImpl thinPeerJxtaImpl) {
		super(thinPeerJxtaImpl.getLog(),OdysseusMessageType.QueryNegotiation.name());
		this.thinPeerJxtaImpl = thinPeerJxtaImpl;
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		String action = MessageTool.getMessageElementAsString(namespace,
				 "queryAction", (Message) msg);

		if (action.equals(OdysseusQueryAction.bidding)) {
			String queryId = meas(namespace, "queryId", (Message) msg);
			PipeAdvertisement pipeAdv = MessageTool.createResponsePipeFromMessage(namespace,
					(Message) msg, 0);
			PeerAdvertisement peerAdv = MessageTool.createPeerAdvertisementFromMessage(
					namespace, (Message) msg);
			P2PQuery q = thinPeerJxtaImpl.getQuery(queryId);
			if (q != null && (q instanceof P2PQueryJxtaImpl)) {
				((P2PQueryJxtaImpl) q).addAdminBidding(pipeAdv, peerAdv);
			}

		}
		if (action.equals(OdysseusQueryAction.resultStreaming)) {
			StreamHandlerJxtaImpl shandler = new StreamHandlerJxtaImpl(
					MessageTool.createResponsePipeFromMessage(namespace, (Message) msg, 0),
					MessageTool.getMessageElementAsString(namespace, "queryId",
							(Message) msg), thinPeerJxtaImpl);
			Thread t = new Thread(shandler);
			t.start();
		}
		if (action.equals(OdysseusQueryAction.unknownSource)) {
			String queryId = MessageTool.getMessageElementAsString(
					"UnknownSource", "query", (Message) msg);
			thinPeerJxtaImpl.removeQuery(queryId);
			thinPeerJxtaImpl.getGui().removeTab(queryId);
			new ErrorPopup("Error in query translation.");
		}

		if (action.equals(OdysseusQueryAction.queryFailed)) {
			String queryId = MessageTool.getMessageElementAsString(
					"QueryFailed", "queryId", (Message) msg);
			thinPeerJxtaImpl.removeQuery(queryId);
			thinPeerJxtaImpl.getGui().removeTab(queryId);
			new ErrorPopup(
					"<html>Error in query distribution<br />Not enough bids.</html>");
		}
	}
}
