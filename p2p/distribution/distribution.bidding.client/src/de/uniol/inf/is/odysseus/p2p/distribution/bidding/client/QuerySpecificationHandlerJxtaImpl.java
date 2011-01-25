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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * Gefundene Anfragen werden hier behandelt
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class QuerySpecificationHandlerJxtaImpl<S extends QueryExecutionSpezification>
		implements IQuerySpecificationHandler<S> {

	static private Logger logger = LoggerFactory
			.getLogger(QuerySpecificationHandlerJxtaImpl.class);

	private IOdysseusPeer peer;
	private IQuerySelectionStrategy querySelectionStrategy;

	private ILogListener log;

	public QuerySpecificationHandlerJxtaImpl(S querySpecification,
			IOdysseusPeer peer, IQuerySelectionStrategy querySelectionStrategy,
			ILogListener log) {
		this.peer = peer;
		this.querySelectionStrategy = querySelectionStrategy;
		handleQuerySpezification(querySpecification);
		this.log = log;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void handleQuerySpezification(S querySpecification) {
		logger.debug("handle adv zu Subplan "
				+ querySpecification.getSubplanId());

		PipeAdvertisement pipeAdv = null;
		StringReader sr = new StringReader(querySpecification.getBiddingPipe());
		XMLDocument xml = null;
		try {
			xml = (XMLDocument) StructuredDocumentFactory
					.newStructuredDocument(MimeMediaType.XMLUTF8, sr);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pipeAdv = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(xml);
		PipeAdvertisement pipe = (PipeAdvertisement) peer
				.getServerResponseAddress();

		P2PQueryJxtaImpl query = (P2PQueryJxtaImpl) peer
				.getQuery(querySpecification.getQueryId());

		if (query == null) {
			query = new P2PQueryJxtaImpl();
			query.setId(querySpecification.getQueryId());
			query.setLanguage(querySpecification.getLanguage());
			query.setAdminPeerPipe(MessageTool
					.createPipeAdvertisementFromXml(querySpecification
							.getBiddingPipe()));

			log.addQuery(querySpecification.getQueryId());
		}

		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					querySpecification.getSubplan().getBytes("utf-8"));
			Base64InputStream b64in = new Base64InputStream(bis);
			ObjectInputStream ois = new ObjectInputStream(b64in);
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		query.getSubPlans().put(querySpecification.getSubplanId(),
				(Subplan) obj);

		log.logAction(
				querySpecification.getQueryId(),
				"Ausgeschriebene(r) Anfrage/Teilplan gefunden: "
						+ AbstractTreeWalker.prefixWalk(
								query.getSubPlans()
										.get(querySpecification.getSubplanId())
										.getAo(),
								new AlgebraPlanToStringVisitor()));

		HashMap<String, Object> messageElements = new HashMap<String, Object>();
		if (getQuerySelectionStrategy().handleQuery(
				query.getSubPlans().get(querySpecification.getSubplanId()),
				peer)) {
			messageElements.put("ExecutionBid", "positive");
			query.setStatus(Lifecycle.GRANTED);
			peer.addQuery(query);
			log.logAction(querySpecification.getQueryId(),
					"Biete auf Anfrage/Teilplan");
		} else {
			messageElements.put("ExecutionBid", "negative");
			log.logAction(querySpecification.getQueryId(),
					"Biete nicht auf Anfrage/Teilplan");
			peer.removeQuery(query.getId());
		}
		messageElements.put("queryId", querySpecification.getQueryId());

		messageElements.put("peerId", PeerGroupTool.getPeerGroup().getPeerID()
				.toString());
		messageElements.put("subplanId", querySpecification.getSubplanId());
		messageElements.put("pipeAdvertisement", pipe);
		((JxtaMessageSender) (peer.getMessageSender())).sendMessage(
				PeerGroupTool.getPeerGroup(), MessageTool.createSimpleMessage(
						"BiddingProvider", messageElements), pipeAdv);
	}

	@Override
	public IQuerySelectionStrategy getQuerySelectionStrategy() {
		return querySelectionStrategy;
	}

}
