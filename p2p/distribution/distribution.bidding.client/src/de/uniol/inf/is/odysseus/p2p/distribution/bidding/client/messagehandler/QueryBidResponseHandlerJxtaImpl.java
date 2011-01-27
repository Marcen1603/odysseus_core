package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler;

import sun.security.action.GetLongAction;
import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.AbstractJxtaMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

/**
 * This class reacts on messages that state if a peer 
 * should process a query/subplan
 * @author Marco Grawunder
 *
 */

public class QueryBidResponseHandlerJxtaImpl extends AbstractJxtaMessageHandler {

	private IQueryProvider queryProvider;

	public QueryBidResponseHandlerJxtaImpl(IQueryProvider queryProvider, ILogListener log) {
		super(log,"BiddingClient");
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
				IExecutionListener listener = queryProvider
						.getListenerForQuery(queryId);
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
			log.logAction(subPlanId,
					"Denied to process plan. Removing plan from store");
			log.removeQueryOrSubplan(subPlanId);

			P2PQuery q = queryProvider.getQuery(queryId);
			if (q.getId() == queryId && q.getSubPlans().size() == 1) {
				queryProvider.removeQuery(q.getId());
			} else if (q.getId() == queryId) {
				q.getSubPlans().remove(subPlanId);
			}

		}

	}
}
