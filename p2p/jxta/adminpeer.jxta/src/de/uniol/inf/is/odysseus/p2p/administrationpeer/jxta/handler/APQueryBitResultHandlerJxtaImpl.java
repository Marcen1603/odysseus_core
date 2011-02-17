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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

/**
 * This class reacts on the answer for a bit to process a query
 * 
 * @author Marco Grawunder
 * 
 */

public class APQueryBitResultHandlerJxtaImpl extends AbstractJxtaMessageHandler {

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public APQueryBitResultHandlerJxtaImpl(
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		super(administrationPeerJxtaImpl.getLog(), "BiddingResult");
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}

	@Override
	public void handleMessage(Object _msg, String _namespace) {

		Message msg = (Message) _msg;
		String namespace = (String) _namespace;
		String queryId = meas(namespace, "queryId", msg);
		String queryResult = meas(namespace, "result", msg);

		// AdminPeer hat Zuspruch fuer eine Anfrage bekommen.
		if (queryResult.equals("granted")) {
			log.logAction(queryId,
					"Accepted query administration bid. Handle query");
			P2PQuery q = administrationPeerJxtaImpl.getQuery(queryId);
			if (q != null) {
				q.setStatus(Lifecycle.NEW);
				IExecutionListener l = administrationPeerJxtaImpl
						.getListenerForQuery(queryId);
				l.startListener();
			} else {
				log.logAction(queryId,
						"Got bid for unkown query");
			}

		} else if (queryResult.equals("denied")) {
			log.logAction(queryId,
					"Denied query administration bid. Remove query");
			administrationPeerJxtaImpl.removeQuery(queryId);
		}

	}

}
