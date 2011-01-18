package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.AbstractMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class QueryResultHandlerJxtaImpl extends AbstractMessageHandler {

	private HashMap<Query, IExecutionListener> queries;

	public HashMap<Query, IExecutionListener> getQueries() {
		return queries;
	}

	public QueryResultHandlerJxtaImpl(HashMap<Query, IExecutionListener> hashMap) {
		this.queries = hashMap;
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
			for (Query q : getQueries().keySet()) {
				if (q.getId().equals(queryId)) {
					IExecutionListener listener = getQueries().get(q);
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
			}

		} else {
			Log.logAction(queryId,
					"Habe keine Zusage fuer die Anfrage bekommen");
			for (Query q : getQueries().keySet()) {
				if (q.getId() == queryId && q.getSubPlans().size() == 1) {
					getQueries().remove(q);
				} else if (q.getId() == queryId) {
					q.getSubPlans().remove(subPlanId);
				}

			}

		}

	}

}
