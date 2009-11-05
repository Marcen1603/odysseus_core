package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.EventListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Subplan;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query.Status;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Subplan.SubplanStatus;
//import de.uniol.inf.is.odyssesus.p2p.partitioning.base.IPartitioner;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.ExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class QueryResultHandlerJxtaImpl implements IQueryResultHandler {

	
	// Partitionierungskomponente
//	private IPartitioner partitioner;


	private ICompiler compiler = null;
	
	
	public ICompiler getCompiler() {
		return compiler;
	}
	
	public QueryResultHandlerJxtaImpl(ICompiler compiler) {
		this.compiler = compiler;
	}
	
	//Quatsch
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
				getCompiler().getInfos();
				plan = getCompiler().translateQuery(AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(queryId).getQuery(), "CQL");
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
			
			AbstractLogicalOperator restructPlan = (AbstractLogicalOperator) getCompiler().restructPlan(plan.get(0));
			
			
//				plan = CQLParser.getInstance().parse(AdministrationPeerJxtaImpl.getInstance()
//						.getQueries().get(queryId).getQuery());
				
//				IRestructure restructure = new RelationalRestructure();
//				restructure.init();
//				restructPlan = restructure.restructPlan(plan.get(0));


				
			

			
			
			//------------------------------------------------------------Aufteilungsphase---------------------------------------------//
			
			//Prüfen, ob Partitionierungsdienst überhaupt anwesend ist
//			if(getPartitioner()!=null) {
//				getPartitioner().splitPlan(restructPlan);
//				System.out.println("Partionierung aktiv");
//			}
			
			
			
			
			// ThinPeer zieht nur die Daten direkt aus der Quelle
			// Passiert bei Abfragen wie Select * From quelle
			// also bei Abfragen die nur aus einem AccessAO bestehen.
			if (restructPlan instanceof AccessAO) {
				System.out.println("ja ist");
				SDFSource source = ((AccessAO) restructPlan).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup().getPeerAdvertisement();

				Message thinPeerResponse = MessageTool.createSimpleMessage(
						"ResultStreaming", "queryId", queryId, MessageTool
								.createPipeAdvertisementFromXml(sourceAdv),
						peerAdv);
				MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup(),
						AdministrationPeerJxtaImpl.getInstance().getQueries()
								.get(queryId).getResponseSocketThinPeer(),
						thinPeerResponse);
				AdministrationPeerJxtaImpl.getInstance().getQueries().get(
						queryId).setStatus(Status.RUN);

				Log.setSubplans(queryId, 0);
				Log.setSplittingStrategy(queryId, "Keine verwendet");
				Log.setStatus(queryId, AdministrationPeerJxtaImpl.getInstance()
						.getQueries().get(queryId).getStatus().toString());
				

//				return;
				
			} 
			else { 
				Log.logAction(queryId, "Plan splitten");
				ArrayList<AbstractLogicalOperator> splitPlan = AdministrationPeerJxtaImpl.getInstance().splitPlan(restructPlan);
				System.out.println("split");
				for(@SuppressWarnings("unused") AbstractLogicalOperator op : splitPlan) {
				}
				
				Log.setSubplans(queryId, splitPlan.size());
				Log.setSplittingStrategy(queryId, AdministrationPeerJxtaImpl.getInstance().getSplitter().getName());
				Log.setStatus(queryId, AdministrationPeerJxtaImpl.getInstance()
						.getQueries().get(queryId).getStatus().toString());

				AdministrationPeerJxtaImpl.getInstance().getQueries().get(
						queryId).setSubplans(splitPlan);

			}

			
			
			
			

			
			//--------------------------------------Verteilungsphase---------------------------------------------------//
			
			
			
			
			if (BIDDING) {
				Log.logAction(queryId, "Anfrage ausschreiben");
				// Anfragen ausschreiben
				AdministrationPeerJxtaImpl.getInstance().getQueries().get(
						queryId.toString()).setStatus(Status.BIDDING);

				// Hier wird dann die Ausführung der Anfrage ausgeschrieben

				QueryExecutionSpezification adv = (QueryExecutionSpezification) AdvertisementFactory
						.newAdvertisement(QueryExecutionSpezification
								.getAdvertisementType());

				adv
						.setBiddingPipe(((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getSocketServerListener())
								.getServerPipeAdvertisement().toString());
				adv.setQueryId(queryId.toString());
				adv.setLanguage(language);
				try {
					AdministrationPeerJxtaImpl.getInstance()
							.getDiscoveryService().publish(adv, 15000, 15000);
					AdministrationPeerJxtaImpl.getInstance()
							.getDiscoveryService().remotePublish(adv,15000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// Anfragen direkt verteilen
				HashMap<String, ExtendedPeerAdvertisement> operatorPeers = AdministrationPeerJxtaImpl.getInstance().getOperatorPeers();
				ArrayList<Subplan> subPlans = AdministrationPeerJxtaImpl.getInstance().getQueries().get(queryId.toString())
						.getSubPlans();
				ArrayList<String> operatorPeersRandomList = new ArrayList<String>();
				operatorPeersRandomList.addAll(operatorPeers.keySet());
				int subplannumber = 0;
				for (Subplan s : subPlans) {
					if (subplannumber == 0) {
						String pipeAdv = ((P2PSinkAO) s.getAo()).getAdv();
						PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl.getInstance().getNetPeerGroup()
								.getPeerAdvertisement();

						Message thinPeerResponse = MessageTool
								.createSimpleMessage(
										"ResultStreaming",
										"queryId",
										AdministrationPeerJxtaImpl.getInstance().getQueries()
												.get(queryId).getId(),
										MessageTool
												.createPipeAdvertisementFromXml(pipeAdv),
										peerAdv);
						MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().netPeerGroup,
								AdministrationPeerJxtaImpl.getInstance()
										.getQueries().get(queryId)
										.getResponseSocketThinPeer(),
								thinPeerResponse);
					}

					Collections.shuffle(operatorPeersRandomList);
					ExtendedPeerAdvertisement adv = operatorPeers
							.get(operatorPeersRandomList.get(0));
					Message response = MessageTool
							.createSimpleMessage(
									"DoQuery",
									"queryId",
									"language",
									"result",
									"adminPipeAdvertisement",
									"subPlanId",
									 "events",
									 AdministrationPeerJxtaImpl.getInstance()
											.getQueries().get(queryId).getId(),
											AdministrationPeerJxtaImpl.getInstance()
											.getQueries().get(queryId)
											.getLanguage(),
									"granted",
									((SocketServerListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
											.getSocketServerListener())
											.getServerPipeAdvertisement()
											.toString(),
									s.getId(),
									AdministrationPeerJxtaImpl.getInstance()
											.getEvents(),
									s.getAo(),
									((EventListenerJxtaImpl) AdministrationPeerJxtaImpl.getInstance().getEventListener())
											.getPipeAdv());
					// Erfolg
					MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance().netPeerGroup, MessageTool
							.createPipeAdvertisementFromXml(adv.getPipe()),
							response);

					// Den Peer setzen der gerade den Teilplan ausführt
					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(queryId).getSubPlans().get(
									subplannumber)).setPeerId(adv.getPeerId());
					// Socket von dem Peer setzen der gerade den Teilplan
					// ausführt
					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(queryId).getSubPlans().get(
									subplannumber)).setResponseSocket(adv
							.getPipe());
					AdministrationPeerJxtaImpl.getInstance().getQueries().get(
							queryId).getSubPlans().get(subplannumber)
							.setStatus(SubplanStatus.CLOSED);

					subplannumber++;
				}

			}

		} else if (queryResult.equals("denied")) {
			// Status erstmal auf Denied ist im Moment unnötig, da die Anfrage
			// zurzeit sowieso verworfen wird.
			AdministrationPeerJxtaImpl.getInstance().getQueries().get(queryId)
					.setStatus(Status.DENIED);
			// Keine Zusage bekommen also weg damit
			AdministrationPeerJxtaImpl.getInstance().getQueries().remove(
					queryId);
		}

	}

	private  void sendSourceFailure(String queryId) {

		MessageTool.sendMessage(AdministrationPeerJxtaImpl.getInstance()
				.getNetPeerGroup(), AdministrationPeerJxtaImpl.getInstance()
				.getQueries().get(queryId).getResponseSocketThinPeer(),
				MessageTool.createSimpleMessage("UnknownSource", "query",
						queryId));
		Log.logAction(queryId, "Absage für die Verwaltung der Anfrage bekommen.");
	}
	
//	public IPartitioner getPartitioner() {
//		return partitioner;
//	}
	
	
}
