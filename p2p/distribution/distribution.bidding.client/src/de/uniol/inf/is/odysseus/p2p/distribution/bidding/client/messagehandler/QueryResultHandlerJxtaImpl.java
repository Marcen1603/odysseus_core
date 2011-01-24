package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class QueryResultHandlerJxtaImpl extends AbstractMessageHandler {

	private IQueryProvider queryProvider;

	public QueryResultHandlerJxtaImpl(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
		setInterestedNamespace("BiddingClient");
	}

	@Override
	public void handleMessage(Object _msg, String _namespace) {
		Message msg = (Message) _msg;

		String namespace = (String) _namespace;

		String result = MessageTool.getMessageElementAsString(namespace,
				"result", msg);
		String queryId = MessageTool.getMessageElementAsString(namespace,
				"queryId", msg);
		String subPlanId = MessageTool.getMessageElementAsString(namespace,
				"subplanId", msg);
		if (result.equals("granted")) {
			Log.logAction(queryId, "Erhalte Zusage fuer Teilplan " + subPlanId);

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
			Log.logAction(queryId,
					"Habe keine Zusage fuer die Anfrage bekommen");

			P2PQuery q = queryProvider.getQuery(queryId);
			if (q.getId() == queryId && q.getSubPlans().size() == 1) {
				queryProvider.removeQuery(q.getId());
			} else if (q.getId() == queryId) {
				q.getSubPlans().remove(subPlanId);
			}

		}

	}
}
