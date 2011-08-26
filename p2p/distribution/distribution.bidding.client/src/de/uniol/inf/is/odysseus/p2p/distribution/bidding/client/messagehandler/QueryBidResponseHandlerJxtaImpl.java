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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.BiddingClient;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * This class reacts on messages that state if a peer should process a
 * query/subplan
 * 
 * @author Marco Grawunder
 * 
 */

public class QueryBidResponseHandlerJxtaImpl extends AbstractJxtaMessageHandler {

	private IQueryProvider queryProvider;

	public QueryBidResponseHandlerJxtaImpl(IQueryProvider queryProvider, ILogListener log) {
		super(log, "BiddingClient");
		this.queryProvider = queryProvider;
	}

	@Override
	public void handleMessage(Object _msg, String namespace) {
		Message msg = (Message) _msg;

		String result = meas(namespace, "result", msg);
		String queryId = meas(namespace, "queryId", msg);
		String subPlanId = meas(namespace, "subplanId", msg);

		if (result.equals("granted")) {
			log.logAction(subPlanId, "Accepted for processing query " + subPlanId);

			P2PQuery q = queryProvider.getQuery(queryId);
			if (q != null) {
				IExecutionListener listener = queryProvider.getListenerForQuery(queryId);
				for (Subplan sp : q.getSubPlans().values()) {
					if (sp.getId().toString().equals(subPlanId)) {
						sp.setStatus(Lifecycle.GRANTED);
					}
				}
				boolean run = true;
				for (Subplan sp : q.getSubPlans().values()) {
					if (sp.getStatus() != Lifecycle.GRANTED) {
						run = false;
						break;
					}
				}
				if (run) {
					q.setStatus(Lifecycle.GRANTED);
					listener.startListener();
				}
			}

		} else {
			log.logAction(subPlanId, "Denied to process plan. Removing plan from store");
			log.removeQueryOrSubplan(subPlanId);
			
			Subplan plan = queryProvider.getQuery(queryId).getSubPlans().get(subPlanId);
			try {
				BiddingClient.getExecutor().removeQuery(plan.getQuery().getID(), GlobalState.getActiveUser(""));
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}			

			P2PQuery q = queryProvider.getQuery(queryId);
			if (q.getId() == queryId && q.getSubPlans().size() == 1) {
				queryProvider.removeQuery(q.getId());
			} else if (q.getId() == queryId) {
				q.getSubPlans().remove(subPlanId);
				
			}

		}

	}
}
