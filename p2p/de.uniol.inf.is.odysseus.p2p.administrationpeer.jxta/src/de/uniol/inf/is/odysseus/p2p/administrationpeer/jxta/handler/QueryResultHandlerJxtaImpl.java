package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;


import java.util.HashMap;
import net.jxta.endpoint.Message;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageSender;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;



public class QueryResultHandlerJxtaImpl implements IMessageHandler {

	
	private IMessageSender<PeerGroup, Message, PipeAdvertisement> sender;

	public QueryResultHandlerJxtaImpl(IMessageSender<PeerGroup, Message, PipeAdvertisement> sender) {
		this.sender = sender;
	}

	public void handleMessage(Object _msg, String _namespace) {
		

		
		
		Message msg = (Message) _msg;
		
		String namespace = (String) _namespace;
		
		String queryId = MessageTool.getMessageElementAsString(namespace, "queryId",
				msg);
		String queryResult = MessageTool.getMessageElementAsString(namespace,
				"result", msg);
		// String language = MessageTool.getMessageElementAsString(type,
		// "language", msg);
		
		// AdminPeer hat Zuspruch für eine Anfrage bekommen.
		
		if (queryResult.equals("granted")) {
			Log.logAction(queryId, "Zusage für die Verwaltung der Anfrage bekommen.");
			for(Query q : AdministrationPeerJxtaImpl.getInstance().getQueries().keySet()) {
				if(q.getId().equals(queryId)) {
					//Einstieg in die Ausführungsumgebung
					q.setStatus(Lifecycle.OPEN);
					AdministrationPeerJxtaImpl.getInstance().getQueries().get(q).startListener();
				}
			}
			
			
			//Nach der Zusage für die Verwaltung einer Anfrage wird folgend die Anfrage übersetzt und optimiert
			
			
			//TODO Ersetzen durch StatusMessage für OPEN
			
//			List<ILogicalOperator> plan = null;
//			try {
//				//Anfrage Übersetzen und in Query kapseln
//				plan = AdministrationPeerJxtaImpl.getInstance().getCompiler().translateQuery(AdministrationPeerJxtaImpl.getInstance().
//						getDistributionProvider().getManagedQueries().get(queryId).getQuery(), language);
//			} catch (QueryParseException e3) {
//				e3.printStackTrace();
//				Log.logAction(queryId, "Fehler bei der Übersetzung der Anfrage");
//				sendSourceFailure(queryId);
//				return;
//			} catch (Throwable e2) {
//				e2.printStackTrace();
//				sendSourceFailure(queryId);
//				return;
//			}			

			//Restrukturierung des Operatorplans
//			AbstractLogicalOperator restructPlan = (AbstractLogicalOperator) AdministrationPeerJxtaImpl.getInstance().getCompiler().restructPlan(plan.get(0));

			
			
			//TODO Ersetzen durch StatusMessage Split 
			
			//Aufteilungsphase
//			Log.logAction(queryId, "Operatorplan aufteilen mit Strategie: "+AdministrationPeerJxtaImpl.getInstance().getSplitting().getName());
//			ArrayList<AbstractLogicalOperator> splitPlan = AdministrationPeerJxtaImpl.getInstance().getSplitting().splitPlan(restructPlan);
//			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(queryId).setSubplans(splitPlan);
//			Log.setSubplans(queryId, splitPlan.size());
//			Log.setSplittingStrategy(queryId, AdministrationPeerJxtaImpl.getInstance().getSplitting().getName());
//			Log.setStatus(queryId, AdministrationPeerJxtaImpl.getInstance()
//					.getDistributionProvider().getManagedQueries().get(queryId).getStatus().toString());

				
			
			
			

			
			
			
			

			
			//--------------------------------------Verteilungsphase---------------------------------------------------//
			
			//TODO Ersetzen durch StatusMessage Distribution
			
//			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().distributePlan(AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(queryId),AdministrationPeerJxtaImpl.getInstance().getServerPipeAdvertisement());
			
			


		} else if (queryResult.equals("denied")) {
			for(Query q : AdministrationPeerJxtaImpl.getInstance().getQueries().keySet()) {
				if(q.getId() == queryId) {
					AdministrationPeerJxtaImpl.getInstance().getQueries().remove(q);
				}
			}
		}

	}

	private  void sendSourceFailure(String queryId) {
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
//		MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance()
//				.getNetPeerGroup(), ((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance()
//				.getQueries().get(queryId)).getResponseSocketThinPeer(),
//				MessageTool.createSimpleMessage("UnknownSource", messageElements));
		this.sender.sendMessage(AdministrationPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), MessageTool.createSimpleMessage("UnknownSource", messageElements), ((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance()
				.getQueries().get(queryId)).getResponseSocketThinPeer());
		Log.logAction(queryId, "Absage für die Verwaltung der Anfrage bekommen.");
	}

	@Override
	public String getInterestedNamespace() {
		return "BiddingResult";
	}

	@Override
	public void setInterestedNamespace(String namespace) {
		
	}

	
}
