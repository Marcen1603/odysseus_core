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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider.messagehandler;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class BiddingMessageResultHandler extends AbstractJxtaMessageHandler {

	private IQueryProvider queryProvider;

	public BiddingMessageResultHandler(IQueryProvider queryProvider, ILogListener log) {
		super(log, "BiddingProvider");
		this.queryProvider = queryProvider;
	}

	@Override
	public void handleMessage(Object oMsg, String namespace) {
		Message msg = (Message) oMsg;
		String queryId = meas(namespace, "queryId", msg);
		String bid = meas(namespace, "ExecutionBid", msg);
		Integer bidValue = Integer.valueOf(bid);
		
		PipeAdvertisement pipeAdv = MessageTool.createResponsePipeFromMessage(namespace, msg,
				0);

		String peerId = meas(namespace, "peerId", msg);
		String subplanId = meas(namespace, "subplanId", msg);
		log.logAction(queryId, "Bid (" + bid
				+ ") from "+peerId);
		P2PQuery actualQuery = queryProvider.getQuery(queryId);
		
		if (actualQuery != null && bidValue > 0 ) {
			((P2PQueryJxtaImpl) actualQuery).addBidding(pipeAdv, peerId,
					subplanId, bidValue);
			log.addBid(queryId, actualQuery.getSubPlans().get(subplanId)
					.getBiddings().size());
		}
	}

}
