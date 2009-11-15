package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64OutputStream;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.Subplan;
import de.uniol.inf.is.odysseus.p2p.Query.Status;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;


public class QueryResultHandlerJxtaImpl implements IQueryResultHandler {

	private ICompiler compiler = null;
	
	public QueryResultHandlerJxtaImpl() {
	}
	
	//TODO: Quatsch
	public static boolean BIDDING = true;

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
						getDistributionProvider().getQueries().get(queryId).getQuery(), "CQL");
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
			Log.logAction(queryId, "Operatorplan aufteilen");
			ArrayList<AbstractLogicalOperator> splitPlan = AdministrationPeerJxtaImpl.getInstance().splitPlan(restructPlan);
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(queryId).setSubplans(splitPlan);
			Log.logAction(queryId, "Operatorplan in "+splitPlan.size()+" Teile aufgeteilt");
			Log.setSubplans(queryId, splitPlan.size());
			Log.setSplittingStrategy(queryId, AdministrationPeerJxtaImpl.getInstance().getSplitting().getName());
			Log.setStatus(queryId, AdministrationPeerJxtaImpl.getInstance()
					.getDistributionProvider().getQueries().get(queryId).getStatus().toString());
			// get(0), weil die subpläne von den senken zu den quellen gehen und wir die p2psink für den thin-peer wollen
			Subplan topSink = AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(queryId.toString()).getSubPlans().get("1");
			String pipeAdv = ((P2PSinkAO) topSink.getAo()).getAdv();
			PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup()
					.getPeerAdvertisement();
				
			//Thin-Peer erhält Verbindungsinformationen zur letzten P2PSink
			Message thinPeerResponse = MessageTool
					.createMessage(
							"ResultStreaming",
							"queryId",
							AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries()
									.get(queryId).getId(),
							MessageTool
									.createPipeAdvertisementFromXml(pipeAdv),
							peerAdv);
			MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().netPeerGroup,
					((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance()
							.getDistributionProvider().getQueries().get(queryId))
							.getResponseSocketThinPeer(),
					thinPeerResponse);

			
			
			
			

			
			//--------------------------------------Verteilungsphase---------------------------------------------------//
			
			
			
			
			if (BIDDING) {
				Log.logAction(queryId, "Anfrage ausschreiben");
				// Anfragen ausschreiben
				AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(
						queryId.toString()).setStatus(Status.BIDDING);

				// Hier wird dann die Ausführung der Subpläne ausgeschrieben
				for(Subplan subplan : AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(
						queryId.toString()).getSubPlans().values()) {
					

					QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
							.newAdvertisement(QueryExecutionSpezification
									.getAdvertisementType());
	
					adv
							.setBiddingPipe(((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getSocketServerListener())
									.getServerPipeAdvertisement().toString());
					adv.setQueryId(queryId.toString());
					adv.setSubplanId(subplan.getId());
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
				    ObjectOutputStream oos = null;
				    Base64OutputStream b64os = null;
					try {
						
						b64os = new Base64OutputStream(bos);
						oos = new ObjectOutputStream(b64os);
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						oos.writeObject(subplan);
						b64os.flush();
					    oos.flush();
					    b64os.close();
					    oos.close();
					    bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
								 				

					try {
						adv.setSubplan(new String(bos.toByteArray(),"utf-8"));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					adv.setLanguage(language);
					try {
						AdministrationPeerJxtaImpl.getInstance()
								.getDiscoveryService().publish(adv, 15000, 15000);
						AdministrationPeerJxtaImpl.getInstance()
								.getDiscoveryService().remotePublish(adv,15000);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
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
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().get(queryId)
					.setStatus(Status.DENIED);
			// Keine Zusage bekommen also weg damit
			AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getQueries().remove(
					queryId);
		}

	}

	private  void sendSourceFailure(String queryId) {
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", queryId);
		MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), ((QueryJxtaImpl)AdministrationPeerJxtaImpl.getInstance()
				.getDistributionProvider().getQueries().get(queryId)).getResponseSocketThinPeer(),
				
				MessageTool.createSimpleMessage("UnknownSource", messageElements));
		Log.logAction(queryId, "Absage für die Verwaltung der Anfrage bekommen.");
	}
	
}
