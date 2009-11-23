package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query.Status;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
 
public class QueryResultHandlerJxtaImpl implements IMessageHandler {
	
	
	private HashMap<String, Query> queries;
	
	
	public HashMap<String, Query> getQueries() {
		return queries;
	}

	public QueryResultHandlerJxtaImpl(HashMap<String, Query> queries) {
		this.queries = queries;
	}
	
	public void handleMessage(Object _msg, String _namespace) {
		Message msg = (Message) _msg;

		String namespace = (String) _namespace;

		String result = MessageTool.getMessageElementAsString(namespace,
				"result", msg);
		String queryId = MessageTool.getMessageElementAsString(namespace,
				"queryId", msg);
		String subPlanId = MessageTool.getMessageElementAsString(namespace,
				"subplanId", msg);
		String events = MessageTool.getMessageElementAsString(namespace,
				"events", msg);

		if (result.equals("granted")) {
			AbstractLogicalOperator ao = getQueries().get(queryId).getSubPlans().get(subPlanId).getAo();
			getQueries().get(queryId).setStatus(Status.GRANTED);
			
			
//			try {
//				System.out.println("Füge hinzu: "+ao.toString());
//				getExecutor().addQuery(ao, new ParameterPriority(2));
//			} catch (PlanManagementException e2) {
//				e2.printStackTrace();
//			}
//
//			Log.logAction(queryId,
//					"Habe die Zusage für die Ausführung des Teilplanes "
//							+ subPlanId + " für Anfrage " + queryId);
//			Log.logAction(queryId, "Führe den Teilplan aus");
//			Log.setStatus(queryId, OperatorPeerJxtaImpl.getInstance()
//					.getQueries().get(queryId).getStatus().toString());
//			// **LOGGING**//
//
//			int count = OperatorPeerJxtaImpl.getInstance().getQueries().get(
//					queryId).getSubPlans().size();
//
//				OperatorPeerJxtaImpl.getInstance().getGui().addSubplans(
//						queryId, count);
//			try {
//					System.out.println("starte Ausführung");
//					if(!OperatorPeerJxtaImpl.getInstance().getExecutor().isRunning())
//						OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//			} catch (PlanManagementException e) {
//				e.printStackTrace();
//			}
			
			Log.addEvent(queryId, "Muss nur noch ausgeführt werden können");
			getQueries().get(queryId)
					.setStatus(Status.RUN);

		} else {
			getQueries().get(queryId)
					.setStatus(Status.DENIED);
			Log
					.logAction(queryId,
							"Habe keine Zusage für die Anfrage bekommen");
			Log.setStatus(queryId, getQueries().get(queryId).getStatus().toString());
			if(getQueries().get(queryId).getSubPlans().size() == 1) {
				getQueries().remove(queryId);
			}
			else {
				getQueries().get(queryId).getSubPlans().remove(subPlanId);
			}

		}

	}

	@Override
	public String getInterestedNamespace() {
		return "BiddingClient";
	}

}
