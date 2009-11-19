package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.HashMap;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.protocol.PipeAdvertisement;

import org.apache.commons.codec.binary.Base64InputStream;

import de.uniol.inf.is.odysseus.p2p.Subplan;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IQuerySpezificationHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.logging.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.listener.SocketServerListenerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.QueryExecutionSpezification;

/**
 * Gefundene Anfragen werden hier behandelt
 * 
 * @author Mart Köhler
 *
 */
public class QuerySpezificationHandlerJxtaImpl implements IQuerySpezificationHandler{
	

	@SuppressWarnings("unchecked")
	public static void handleQuerySpezification(QueryExecutionSpezification adv) {
		System.out.println("handle adv zu Subplan "+adv.getSubplanId());
		if (OperatorPeerJxtaImpl.getInstance().getQueries().keySet().contains(
				adv.getQueryId())) {
			if(OperatorPeerJxtaImpl.getInstance().getQueries().get(adv.getQueryId()).getSubPlans().containsKey(adv.getSubplanId())) {
				return;
			}
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
			//Existiert die Query bereits
			if(OperatorPeerJxtaImpl.getInstance().getQueries().get(adv.getQueryId())==null) {
				QueryJxtaImpl q = new QueryJxtaImpl();
				q.setId(adv.getQueryId());
				q.setLanguage(adv.getLanguage());
				q.setAdminPeerPipe(MessageTool
						.createPipeAdvertisementFromXml(adv.getBiddingPipe()));
				OperatorPeerJxtaImpl.getInstance().getQueries().put(
						adv.getQueryId(), q);
			}
			
			 Object obj = null;
			  try {
			    ByteArrayInputStream bis = new ByteArrayInputStream (adv.getSubplan().getBytes("utf-8"));
			    Base64InputStream b64in = new Base64InputStream(bis);
			    ObjectInputStream ois	 = new ObjectInputStream (b64in);
			    try {
					obj = ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			  }
			  catch (IOException ex) {
			    ex.printStackTrace();
			  }
			  OperatorPeerJxtaImpl.getInstance().getQueries().get(adv.getQueryId()).getSubPlans().put(adv.getSubplanId(), (Subplan) obj);
			

		}
		
		Log.addQuery(adv.getQueryId());
		Log.logAction(adv.getQueryId(), "Ausschreibung für Anfrageausführung gefunden !");

		//TODO: Für bestimmten Subplan bewerben bzw. das auch senden BEWERBUNG HIER ENTSCHEIDEN
		
		// Strategy ob sich überhaupt beworben werden soll
//		if (OperatorPeerJxtaImpl.getInstance().getBiddingStrategy().doBidding(
//				null)) {
		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("queryId", adv.getQueryId());
		messageElements.put("peerId", OperatorPeerJxtaImpl.getInstance().getNetPeerGroup().getPeerID().toString());
		messageElements.put("subplanId", adv.getSubplanId());
		messageElements.put("pipeAdvertisement", pipe);
			MessageTool.sendMessage(OperatorPeerJxtaImpl.getInstance()
					.getNetPeerGroup(), pipeAdv, MessageTool
					.createSimpleMessage("ExecutionBid", messageElements));
			Log.logAction(adv.getQueryId(), "Für Anfrageausführung beworben !");

//		} else {
//			OperatorPeerJxtaImpl.getInstance().getQueries().get(
//					adv.getQueryId()).setStatus(Query.Status.DENIED);
//			Log.logAction(adv.getQueryId(), "Für Anfrageausführung nicht beworben !");
//		}

	}


}
