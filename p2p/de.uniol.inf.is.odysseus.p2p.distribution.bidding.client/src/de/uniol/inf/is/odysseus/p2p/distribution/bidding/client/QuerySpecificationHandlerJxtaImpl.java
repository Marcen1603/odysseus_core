package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;


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

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;

/**
 * Gefundene Anfragen werden hier behandelt
 * 
 * @author Mart Köhler
 *
 */
public class QuerySpecificationHandlerJxtaImpl implements IQuerySpecificationHandler{
	
	private AbstractPeer aPeer;

	public QuerySpecificationHandlerJxtaImpl(QueryExecutionSpezification temp2, AbstractPeer aPeer) {
		this.aPeer = aPeer;
		handleQuerySpezification(temp2);
	}
	
	@SuppressWarnings("unchecked")
	public void handleQuerySpezification(QueryExecutionSpezification adv) {
		System.out.println("handle adv zu Subplan "+adv.getSubplanId());
		QueryJxtaImpl query = null;
		for(Query q : aPeer.getQueries().keySet()) {
			if(q.getId() == adv.getQueryId()) {
				query = (QueryJxtaImpl) q;
				if(q.getSubPlans().containsKey(adv.getSubplanId())) {
					return;
				}
				break;
			}
		}

		PipeAdvertisement pipeAdv = null;
		StringReader sr = new StringReader(adv.getBiddingPipe());
		XMLDocument xml = null;
		try {
			xml = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(MimeMediaType.XMLUTF8, sr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(xml);
		PipeAdvertisement pipe =  (PipeAdvertisement) aPeer.getServerResponseAddress();
		synchronized (aPeer.getQueries()) {
			//Existiert die Query bereits
			if(query==null) {
				query = new QueryJxtaImpl();
				query.setId(adv.getQueryId());
				query.setLanguage(adv.getLanguage());
				query.setAdminPeerPipe(MessageTool
						.createPipeAdvertisementFromXml(adv.getBiddingPipe()));
				aPeer.getQueries().put(query, aPeer.getExecutionListenerFactory().getNewInstance(query, aPeer.getExecutionHandler()));
				Log.addQuery(adv.getQueryId());
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
			  query.getSubPlans().put(adv.getSubplanId(), (Subplan)obj);
		}


//		Log.logAction(adv.getQueryId(), "Ausschreibung für Anfrageausführung gefunden !");

		//TODO: Für bestimmten Subplan bewerben bzw. das auch senden BEWERBUNG HIER ENTSCHEIDEN
		
		// Strategy ob sich überhaupt beworben werden soll
//		if (OperatorPeerJxtaImpl.getInstance().getBiddingStrategy().doBidding(
//				null)) {
		
		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		messageElements.put("ExecutionBid", "positive");
		messageElements.put("queryId", adv.getQueryId());
		
//		messageElements.put("peerId", OperatorPeerJxtaImpl.getInstance().getNetPeerGroup().getPeerID().toString());
		messageElements.put("peerId", PeerGroupTool.getPeerGroup().getPeerID().toString());
		messageElements.put("subplanId", adv.getSubplanId());
		messageElements.put("pipeAdvertisement", pipe);
			MessageTool.sendMessage(PeerGroupTool.getPeerGroup(), pipeAdv, MessageTool
					.createSimpleMessage("BiddingProvider", messageElements));
			Log.logAction(adv.getQueryId(), "Für Anfrageausführung beworben !");

//		} else {
//			OperatorPeerJxtaImpl.getInstance().getQueries().get(
//					adv.getQueryId()).setStatus(Query.Status.DENIED);
//			Log.logAction(adv.getQueryId(), "Für Anfrageausführung nicht beworben !");
//		}

	}
}
