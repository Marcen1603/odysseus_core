package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler;

import java.util.HashMap;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;

public class QueryResultHandlerJxtaImpl implements IMessageHandler {
	
	
	private HashMap<Query, IExecutionListener> queries;
	
	
	public HashMap<Query, IExecutionListener> getQueries() {
		return queries;
	}

	public QueryResultHandlerJxtaImpl(HashMap<Query, IExecutionListener> hashMap) {
		this.queries = hashMap;
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
//		String events = MessageTool.getMessageElementAsString(namespace,
//				"events", msg);
		Log.logAction(queryId, "Erhalte Zusage für Teilplan "+subPlanId);
		if (result.equals("granted")) {
//			AbstractLogicalOperator ao = getQueries().get(queryId).getSubPlans().get(subPlanId).getAo();
			for(Query q : getQueries().keySet()) {
				if(q.getId().equals(queryId)) {
					IExecutionListener listener = getQueries().get(q);
					for(Subplan sp : q.getSubPlans().values()) {
						if(sp.getId().toString().equals(subPlanId)) {
							sp.setStatus(Lifecycle.GRANTED);
						}
					}
					boolean run = true;
					for(Subplan sp : q.getSubPlans().values()) {
						if(sp.getStatus()!=Lifecycle.GRANTED) {
							run = false;
							break;
						}
					}
					if(run) {
						q.setStatus(Lifecycle.GRANTED);
						listener.startListener();
					}
				}
			}
//			getQueries().get(queryId).setStatus(Status.GRANTED);
			
			
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
			
//			getQueries().get(queryId)
//					.setStatus(Status.RUN);

		} else {
//			getQueries().get(queryId)
//					.setStatus(Status.DENIED);
			Log
					.logAction(queryId,
							"Habe keine Zusage für die Anfrage bekommen");
//			Log.setStatus(queryId, getQueries().get(queryId).getStatus().toString());
			for(Query q : getQueries().keySet()) {
				if(q.getId() == queryId && q.getSubPlans().size() == 1) {
					getQueries().remove(q);
				}
				else if(q.getId() == queryId){
					q.getSubPlans().remove(subPlanId);
				}
				
			}

		}

	}

	@Override
	public String getInterestedNamespace() {
		return "BiddingClient";
	}

	@Override
	public void setInterestedNamespace(String namespace) {
		
	}

}
