package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.P2PPOEventListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.phyOp.jxta.P2PPipePO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration.Query.Status;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PipeAdvertisement;

public class QueryResultHandlerJxtaImpl implements IQueryResultHandler {
	
	private ITransformation trafo = null;
	
	public QueryResultHandlerJxtaImpl(ITransformation trafo) {
		this.setTrafo(trafo);
	}
	
	@SuppressWarnings("unchecked")
	public void handleQueryResult(Object _msg, Object _namespace) {

		Message msg = (Message) _msg;

		String namespace = (String) _namespace;

		String result = MessageTool.getMessageElementAsString(namespace,
				"result", msg);
		String queryId = MessageTool.getMessageElementAsString(namespace,
				"queryId", msg);
		String subPlanId = MessageTool.getMessageElementAsString(namespace,
				"subPlanId", msg);
		String events = MessageTool.getMessageElementAsString(namespace,
				"events", msg);

		PipeAdvertisement eventPipe = MessageTool.getResponsePipe(namespace,
				msg, 0);

		ArrayList<POEventType> eventList = MessageTool.getEvents(events);

		if (result.equals("granted") || namespace.equals("DoQuery")) {
			AbstractLogicalOperator ao = (AbstractLogicalOperator) MessageTool.getObjectFromMessage(msg, 0);
			OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.getSubPlans().put(subPlanId, ao);
			OperatorPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.setStatus(Status.GRANTED);
			//TODO: Transformationen -> Rewrite
			//Evtl. eine TansformationConfiguration angeben
			ISource<?> source = null;
			try {
				source = (ISource<?>) getTrafo().transform(ao, null);
			} catch (TransformationException e1) {
				e1.printStackTrace();
			}
			
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
			OperatorPeerJxtaImpl.getInstance().getScheduler().getSources().add((IIterableSource<?>) source);
			((P2PPipePO) source).setQueryId(queryId);

			// **LOGGING**//

			Log.setScheduler(queryId, OperatorPeerJxtaImpl.getInstance()
					.getScheduler().getClass().getSimpleName());
			Log.setSchedulerStrategy(queryId, OperatorPeerJxtaImpl
					.getInstance().getSchedulerStrategy().getClass()
					.getSimpleName());
			Log.logAction(queryId,
					"Habe die Zusage für die Ausführung des Teilplanes "
							+ subPlanId + " für Anfrage " + queryId);
			Log.logAction(queryId, "Führe den Teilplan aus");
			Log.setStatus(queryId, OperatorPeerJxtaImpl.getInstance()
					.getQueries().get(queryId).getStatus().toString());
			// **LOGGING**//

			int count = OperatorPeerJxtaImpl.getInstance().getQueries().get(
					queryId).getSubPlans().size();

			if (OperatorPeerJxtaImpl.getInstance().isGuiEnabled()) {
				OperatorPeerJxtaImpl.getInstance().getGui().addSubplans(
						queryId, count);
			}
			for (POEventType event : eventList) {
				source.subscribe(new P2PPOEventListenerJxtaImpl(queryId,
						eventPipe), event);
			}
			try {
				source.open();
			} catch (OpenFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!OperatorPeerJxtaImpl.getInstance().getScheduler().isRunning()) {
				OperatorPeerJxtaImpl.getInstance().getScheduler()
						.startScheduling();
			}
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
