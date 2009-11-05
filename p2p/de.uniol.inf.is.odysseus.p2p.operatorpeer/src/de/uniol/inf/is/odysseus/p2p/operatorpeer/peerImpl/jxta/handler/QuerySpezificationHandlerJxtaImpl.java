package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler;

import java.io.IOException;
import java.io.StringReader;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.logging.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;

public class QuerySpezificationHandlerJxtaImpl {

	@SuppressWarnings("unchecked")
	public static void handleQuerySpezification(QueryExecutionSpezification adv) {

		if (OperatorPeerJxtaImpl.getInstance().getQueries().keySet().contains(
				adv.getQueryId())) {
			return;
		}

		PipeAdvertisement pipeAdv = null;
		StringReader sr = new StringReader(adv.getBiddingPipe());
		XMLDocument xml = null;
		try {
			xml = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(MimeMediaType.XMLUTF8, sr);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(xml);
		PipeAdvertisement pipe = ((SocketServerListenerJxtaImpl) OperatorPeerJxtaImpl
				.getInstance().getSocketServerListener())
				.getServerPipeAdvertisement();

		synchronized (OperatorPeerJxtaImpl.getInstance().getQueries()) {
			QueryJxtaImpl q = new QueryJxtaImpl();
			q.setId(adv.getQueryId());
			q.setLanguage(adv.getLanguage());
			q.setAdminPeerPipeAdvertisement(MessageTool
					.createPipeAdvertisementFromXml(adv.getBiddingPipe()));
			OperatorPeerJxtaImpl.getInstance().getQueries().put(
					adv.getQueryId(), q);
		}
		
		Log.addQuery(adv.getQueryId());
		Log.logAction(adv.getQueryId(), "Ausschreibung für Anfrageausführung gefunden !");

		//TODO: Erstmal für alles bewerben abschalten
		
		// Strategy ob sich überhaupt beworben werden soll
//		if (OperatorPeerJxtaImpl.getInstance().getBiddingStrategy().doBidding(
//				null)) {
			MessageTool.sendMessage(OperatorPeerJxtaImpl.getInstance()
					.getNetPeerGroup(), pipeAdv, MessageTool
					.createSimpleMessage("ExecutionBid", "queryId", "peerId",
							adv.getQueryId(), OperatorPeerJxtaImpl
									.getInstance().getNetPeerGroup()
									.getPeerID().toString(), pipe));
			Log.logAction(adv.getQueryId(), "Für Anfrageausführung beworben !");

//		} else {
//			OperatorPeerJxtaImpl.getInstance().getQueries().get(
//					adv.getQueryId()).setStatus(Query.Status.DENIED);
//			Log.logAction(adv.getQueryId(), "Für Anfrageausführung nicht beworben !");
//		}

	}

}
