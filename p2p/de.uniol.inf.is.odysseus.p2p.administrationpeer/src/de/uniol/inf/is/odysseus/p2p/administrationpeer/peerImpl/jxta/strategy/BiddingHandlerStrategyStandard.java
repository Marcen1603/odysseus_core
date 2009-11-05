package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy;

import java.util.Collections;

import net.jxta.endpoint.Message;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener.EventListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration.BidJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Subplan;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Subplan.SubplanStatus;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IBiddingHandlerStrategy;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;


public class BiddingHandlerStrategyStandard implements IBiddingHandlerStrategy {

	@Override
	public void handleBidding() {
		for (String s : AdministrationPeerJxtaImpl.getInstance().getQueries()
				.keySet()) {
			// Sind mehr oder exakt Angebote als Teilpläne vorhanden
			if (AdministrationPeerJxtaImpl.getInstance().getQueries().get(s)
					.getBiddings().size() >= AdministrationPeerJxtaImpl
					.getInstance().getQueries().get(s).getSubPlans().size() && AdministrationPeerJxtaImpl
					.getInstance().getQueries().get(s).getSubPlans().size()>0
					&& AdministrationPeerJxtaImpl.getInstance().getQueries()
							.get(s).getStatus() != Query.Status.RUN) {
				System.out.println("Vergleich "+AdministrationPeerJxtaImpl.getInstance().getQueries().get(s)
					.getBiddings().size()+ " mit "+AdministrationPeerJxtaImpl
					.getInstance().getQueries().get(s).getSubPlans().size());
				System.out.println("erstes if");
				// Wenn ja dann wird einfach den ersten Bewerbern
				// jeweils ein Teilplan
				// zugesprochen. Könnte man noch in Strategie auslagern.
				int subplannumber = 0;

				// Biddings nochmal durchtauschen um bessere zufällige
				// Verteilung zu haben
				Collections.shuffle(AdministrationPeerJxtaImpl.getInstance()
						.getQueries().get(s).getBiddings());

				for (Subplan subplan : AdministrationPeerJxtaImpl.getInstance()
						.getQueries().get(s).getSubPlans()) {
					if (subplan.getStatus() == Subplan.SubplanStatus.CLOSED) {
						continue;
					}
//					if (subplannumber == 0) {
//						String pipeAdv = ((P2PSinkAO) subplan.getAo()).getAdv();
//						PeerAdvertisement peerAdv = AdministrationPeerJxtaImpl
//								.getInstance().getNetPeerGroup()
//								.getPeerAdvertisement();
//						Log
//								.logAction(AdministrationPeerJxtaImpl
//										.getInstance().getQueries().get(s)
//										.getId(),
//										"Sende Bezugsmöglichkeit für Ergebnis an Thin-Peer");
//						Message thinPeerResponse = MessageTool
//								.createSimpleMessage(
//										"ResultStreaming",
//										"queryId",
//										AdministrationPeerJxtaImpl
//												.getInstance().getQueries()
//												.get(s).getId(),
//										MessageTool
//												.createPipeAdvertisementFromXml(pipeAdv),
//										peerAdv);
//						MessageTool.sendMessage(AdministrationPeerJxtaImpl
//								.getInstance().netPeerGroup,
//								AdministrationPeerJxtaImpl.getInstance()
//										.getQueries().get(s)
//										.getResponseSocketThinPeer(),
//								thinPeerResponse);
//					}

					PipeAdvertisement opPeer = ((BidJxtaImpl) AdministrationPeerJxtaImpl
							.getInstance().getQueries().get(s).getBiddings()
							.get(subplannumber)).getResponseSocket();

					Log.logAction(AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getId(),
							"Sende Zusage an Operator-Peer für Teilplan "
									+ subplan.getId());
					Message response = MessageTool.createSimpleMessage(
							"QueryResult", "queryId", "subPlanId", "result",
							"events", AdministrationPeerJxtaImpl.getInstance()
									.getQueries().get(s).getId(), subplan
									.getId(), "granted",
							AdministrationPeerJxtaImpl.getInstance()
									.getEvents(), subplan.getAo(),
							((EventListenerJxtaImpl) AdministrationPeerJxtaImpl
									.getInstance().getEventListener())
									.getPipeAdv());
					// Erfolg
					MessageTool.sendMessage(AdministrationPeerJxtaImpl
							.getInstance().netPeerGroup, opPeer, response);
					// Den Peer setzen der gerade den Teilplan ausführt
					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getSubPlans().get(
									subplannumber))
							.setPeerId(((BidJxtaImpl) AdministrationPeerJxtaImpl
									.getInstance().getQueries().get(s)
									.getBiddings().get(subplannumber))
									.getPeerId());
					// Socket von dem Peer setzen der gerade den
					// Teilplan ausführt
					((SubplanJxtaImpl) AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getSubPlans().get(
									subplannumber))
							.setResponseSocket(((BidJxtaImpl) AdministrationPeerJxtaImpl
									.getInstance().getQueries().get(s)
									.getBiddings().get(subplannumber))
									.getResponseSocket().toString());
					AdministrationPeerJxtaImpl.getInstance().getQueries()
							.get(s).getSubPlans().get(subplannumber).setStatus(
									SubplanStatus.CLOSED);
					subplannumber++;
					subplan.setStatus(Subplan.SubplanStatus.CLOSED);
					Log.setStatus(AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getId(), "Running");
				}
			} else {
				if (AdministrationPeerJxtaImpl.getInstance().getQueries()
						.get(s).getRetries() >= 3
						&& AdministrationPeerJxtaImpl.getInstance()
								.getQueries().get(s).getSubPlans().size() != 0) {
					// Es kommen nicht genug Angebote also wird die
					// Anfrage abgebrochen
					String queryId = AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getId();
					Log.logAction(AdministrationPeerJxtaImpl.getInstance()
							.getQueries().get(s).getId(),
							"Nicht genug Gebote von Operator-Peers");
					Message msg = MessageTool.createSimpleMessage(
							"QueryFailed", "queryId", queryId);
					Log.setStatus(queryId, "Failed");
					MessageTool.sendMessage(AdministrationPeerJxtaImpl
							.getInstance().getNetPeerGroup(),
							AdministrationPeerJxtaImpl.getInstance()
									.getQueries().get(s)
									.getResponseSocketThinPeer(), msg);
					AdministrationPeerJxtaImpl.getInstance().getQueries()
							.remove(queryId);
					Log.removeQuery(queryId);
				} else {
					System.out.println("RETRY WARUM???");
					AdministrationPeerJxtaImpl.getInstance().getQueries()
							.get(s).addRetry();
				}

			}

		}

	}

}
