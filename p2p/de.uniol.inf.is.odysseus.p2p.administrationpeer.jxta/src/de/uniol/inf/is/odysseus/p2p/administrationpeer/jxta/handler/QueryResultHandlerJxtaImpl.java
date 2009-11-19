package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.handler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jxta.endpoint.Message;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.Log;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;


public class QueryResultHandlerJxtaImpl implements IQueryResultHandler {

	
	public QueryResultHandlerJxtaImpl() {
	}

	public void handleQueryResult(Object _msg, Object _namespace) {
		

		
		
		Message msg = (Message) _msg;
		
		String namespace = (String) _namespace;
		
		String queryId = MessageTool.getMessageElementAsString(namespace, "queryId",
				msg);
		String queryResult = MessageTool.getMessageElementAsString(namespace,
				"result", msg);
		// String language = MessageTool.getMessageElementAsString(type,
		// "language", msg);
		String language = "CQL";
		// AdminPeer hat Zuspruch für eine Anfrage bekommen.
		
		if (queryResult.equals("granted")) {
			Log.logAction(queryId, "Zusage für die Verwaltung der Anfrage bekommen.");
			
			
			
			//Nach der Zusage für die Verwaltung einer Anfrage wird folgend die Anfrage übersetzt und optimiert
			
			List<ILogicalOperator> plan = null;
			try {
				plan = AdministrationPeerJxtaImpl.getInstance().getCompiler().translateQuery(AdministrationPeerJxtaImpl.getInstance().
						getDistributionProvider().getManagedQueries().get(queryId).getQuery(), "CQL");
			} catch (QueryParseException e3) {
				e3.printStackTrace();
				Log.logAction(queryId, "Fehler bei der Übersetzung der Anfrage");
				sendSourceFailure(queryId);
				return;
			} catch (Throwable e2) {
				e2.printStackTrace();
				sendSourceFailure(queryId);
				return;
			}			
			
			//Restrukturierung des Operatorplans
			AbstractLogicalOperator restructPlan = (AbstractLogicalOperator) AdministrationPeerJxtaImpl.getInstance().getCompiler().restructPlan(plan.get(0));

			//Aufteilungsphase
			Log.logAction(queryId, "Operatorplan aufteilen mit Strategie: "+AdministrationPeerJxtaImpl.getInstance().getSplitting().getName());
			ArrayList<AbstractLogicalOperator> splitPlan = AdministrationPeerJxtaImpl.getInstance().splitPlan(restructPlan);
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(queryId).setSubplans(splitPlan);
			Log.setSubplans(queryId, splitPlan.size());
			Log.setSplittingStrategy(queryId, AdministrationPeerJxtaImpl.getInstance().getSplitting().getName());
			Log.setStatus(queryId, AdministrationPeerJxtaImpl.getInstance()
					.getDistributionProvider().getManagedQueries().get(queryId).getStatus().toString());

				
			
			
			

			
			
			
			

			
			//--------------------------------------Verteilungsphase---------------------------------------------------//
			
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().distributePlan(AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(queryId));
			
			
//			else {
//				// Anfragen direkt verteilen
//				HashMap<String, ExtendedPeerAdvertisement> operatorPeers = AdministrationPeerJxtaImpl.getInstance().getOperatorPeers();
//				ArrayList<Subplan> subPlans = AdministrationPeerJxtaImpl.getInstance().getQueries().get(queryId.toString())
//						.getSubPlans();
//				ArrayList<String> operatorPeersRandomList = new ArrayList<String>();
//				operatorPeersRandomList.addAll(operatorPeers.keySet());
//				int subplannumber = 0;
//				for (Subplan s : subPlans) {
//					if (subplannumber == 0) {
//						String pipeAdv = ((P2PSinkAO) s.getAo()).getAdv();
//						PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup()
//								.getPeerAdvertisement();
//
//						Message thinPeerResponse = MessageTool
//								.createSimpleMessage(
//										"ResultStreaming",
//										"queryId",
//										AdministrationPeerJxtaImpl.getInstance().getQueries()
//												.get(queryId).getId(),
//										MessageTool
//												.createPipeAdvertisementFromXml(pipeAdv),
//										peerAdv);
//						MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().netPeerGroup,
//								AdministrationPeerJxtaImpl.getInstance()
//										.getQueries().get(queryId)
//										.getResponseSocketThinPeer(),
//								thinPeerResponse);
//					}
//
//					Collections.shuffle(operatorPeersRandomList);
//					ExtendedPeerAdvertisement adv = operatorPeers
//							.get(operatorPeersRandomList.get(0));
//					Message response = MessageTool
//							.createSimpleMessage(
//									"DoQuery",
//									"queryId",
//									"language",
//									"result",
//									"adminPipeAdvertisement",
//									"subPlanId",
//									 "events",
//									 AdministrationPeerJxtaImpl.getInstance()
//											.getQueries().get(queryId).getId(),
//											AdministrationPeerJxtaImpl.getInstance()
//											.getQueries().get(queryId)
//											.getLanguage(),
//									"granted",
//									((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
//											.getSocketServerListener())
//											.getServerPipeAdvertisement()
//											.toString(),
//									s.getId(),
//									AdministrationPeerJxtaImpl.getInstance()
//											.getEvents(),
//									s.getAo(),
//									((EventListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getEventListener())
//											.getPipeAdv());
//					// Erfolg
//					MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().netPeerGroup, MessageTool
//							.createPipeAdvertisementFromXml(adv.getPipe()),
//							response);
//
//					// Den Peer setzen der gerade den Teilplan ausführt
//					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
//							.getQueries().get(queryId).getSubPlans().get(
//									subplannumber)).setPeerId(adv.getPeerId());
//					// Socket von dem Peer setzen der gerade den Teilplan
//					// ausführt
//					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
//							.getQueries().get(queryId).getSubPlans().get(
//									subplannumber)).setResponseSocket(adv
//							.getPipe());
//					AdministrationPeerJxtaImpl.getInstance().getQueries().get(
//							queryId).getSubPlans().get(subplannumber)
//							.setStatus(SubplanStatus.CLOSED);
//
//					subplannumber++;
//				}
//
//			}

		} else if (queryResult.equals("denied")) {
			// Status erstmal auf Denied ist im Moment unnötig, da die Anfrage
			// zurzeit sowieso verworfen wird.
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().get(queryId)
					.setStatus(Status.DENIED);
			// Keine Zusage bekommen also weg damit
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getManagedQueries().remove(
					queryId);
		}

	}

	private  void sendSourceFailure(String queryId) {
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), ((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance()
				.getDistributionProvider().getManagedQueries().get(queryId)).getResponseSocketThinPeer(),
				
				MessageTool.createSimpleMessage("UnknownSource", messageElements));
		Log.logAction(queryId, "Absage für die Verwaltung der Anfrage bekommen.");
	}
	
}
