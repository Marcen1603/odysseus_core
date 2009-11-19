package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.Log;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
 
public class QueryResultHandlerJxtaImpl implements IQueryResultHandler {
	
	private ITransformation trafo = null;
	
	@SuppressWarnings("unused")
	private int temporalID;
	
	public QueryResultHandlerJxtaImpl(ITransformation trafo) {
		this.setTrafo(trafo);
	}
	
	public void handleQueryResult(Object _msg, Object _namespace) {
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

		MessageTool.getResponsePipe(namespace,
				msg, 0);

		MessageTool.getEvents(events);
		//TODO: Prüfen, ob Plan bekannt ist, wenn nein, dann handelt es sich um eine direkte Zuweisung, wenn ja, dann schauen, ob man darauf geboten hat
		if (result.equals("granted") || namespace.equals("DoQuery")) {
			AbstractLogicalOperator ao = OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId).getSubPlans().get(subPlanId).getAo();
			OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.setStatus(Status.GRANTED);
			try {
				System.out.println("Füge hinzu: "+ao.toString());
				temporalID = OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(ao, new ParameterPriority(2));
			} catch (PlanManagementException e2) {
				e2.printStackTrace();
			}
//			ISource<?> source = null;
//			try {
//				source = (ISource<?>) getTrafo().transform(ao,null);
//			} catch (TransformationException e1) {
//				e1.printStackTrace();
//			}
			
//			RelationalPipesTransform trafo = new RelationalPipesTransform();
//			trafo.init();
//			ISource<?> source = null;
//			try {
//				if (OperatorPeerJxtaImpl.getTrafoMode().equals("PNID")) {
//					source = trafo.transformPNID(ao, PriorityMode.NO_PRIORITY);
//				} else if ((OperatorPeerJxtaImpl.getTrafoMode().equals("TI"))) {
//					source = trafo.transformTI(ao, PriorityMode.NO_PRIORITY);
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			OperatorPeerJxtaImpl.getInstance().getScheduler().addPlan(source,
//					Thread.NORM_PRIORITY);
			
			//TODO: Hier wieder einkommentieren
//			OperatorPeerJxtaImpl.getInstance().getScheduler().getSources().add((IIterableSource<?>) source);
//			OperatorPeerJxtaImpl.getInstance().getExecutor().add
//			((P2PPipePO) source).setQueryId(queryId);

			// **LOGGING**//

//			Log.setScheduler(queryId, OperatorPeerJxtaImpl.getInstance()
//					.getScheduler().getClass().getSimpleName());
//			Log.setSchedulerStrategy(queryId, OperatorPeerJxtaImpl
//					.getInstance().getSchedulerStrategy().getClass()
//					.getSimpleName());
			Log.logAction(queryId,
					"Habe die Zusage für die Ausführung des Teilplanes "
							+ subPlanId + " für Anfrage " + queryId);
			Log.logAction(queryId, "Führe den Teilplan aus");
			Log.setStatus(queryId, OperatorPeerJxtaImpl.getInstance()
					.getQueries().get(queryId).getStatus().toString());
			// **LOGGING**//

			int count = OperatorPeerJxtaImpl.getInstance().getQueries().get(
					queryId).getSubPlans().size();

				OperatorPeerJxtaImpl.getInstance().getGui().addSubplans(
						queryId, count);
//			for (POEventType event : eventList) {
//				source.subscribe(new P2PPOEventListenerJxtaImpl(queryId,
//						eventPipe), event);
//			}
//			try {
//				source.open();
//			} catch (OpenFailedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
//				if(!OperatorPeerJxtaImpl.getInstance().getExecutor().isRunning()) {
					System.out.println("starte Ausführung");
					if(!OperatorPeerJxtaImpl.getInstance().getExecutor().isRunning())
						OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//				}
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
//			if (!OperatorPeerJxtaImpl.getInstance().getScheduler().isRunning()) {
//				OperatorPeerJxtaImpl.getInstance().getScheduler()
//						.startScheduling();
//			}
			OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.setStatus(Status.RUN);

		} else {
			OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.setStatus(Status.DENIED);
			Log
					.logAction(queryId,
							"Habe keine Zusage für die Anfrage bekommen");
			Log.setStatus(queryId, OperatorPeerJxtaImpl.getInstance()
					.getQueries().get(queryId).getStatus().toString());

		}

	}

	public void setTrafo(ITransformation trafo) {
		this.trafo = trafo;
	}

	public ITransformation getTrafo() {
		return trafo;
	}

}
