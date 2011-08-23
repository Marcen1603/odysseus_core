/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.p2p.OdysseusMessageType;
import de.uniol.inf.is.odysseus.p2p.ac.bid.IP2PBidGenerator;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.P2PQueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.JxtaMessageSender;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * Handle queries
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class QuerySpecificationHandlerJxtaImpl<S extends QueryExecutionSpezification>
		implements IQuerySpecificationHandler<S> {

	static private Logger logger = LoggerFactory
			.getLogger(QuerySpecificationHandlerJxtaImpl.class);

	private IOdysseusPeer peer;

	private int MAXQUERIES = 10;

	// private IQuerySelectionStrategy querySelectionStrategy;

	public QuerySpecificationHandlerJxtaImpl(S querySpecification,
			IOdysseusPeer peer, ILogListener log) {
		this.peer = peer;
		// this.querySelectionStrategy = querySelectionStrategy;
		handleQuerySpezification(querySpecification);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void handleQuerySpezification(S querySpecification) {
		logger.debug("Got query specification for subplan "
				+ querySpecification.getSubplanId());
		ILogListener log = peer.getLog();

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
			// Add Subplan not Query
			// log.addQuery(querySpecification.getQueryId());
		}

		String plan = querySpecification.getSubplan();
		Object obj = AdvertisementTools.fromBase64String(plan);
		query.getSubPlans().put(querySpecification.getSubplanId(),
				(Subplan) obj);

		log.addQuery(querySpecification.getSubplanId());

		log.logAction(
				querySpecification.getSubplanId(),
				"Found plan "
						+ AbstractTreeWalker.prefixWalk(
								query.getSubPlans()
										.get(querySpecification.getSubplanId())
										.getAo(),
								new AlgebraPlanToStringVisitor()));

		HashMap<String, Object> messageElements = new HashMap<String, Object>();

		Subplan subplan = query.getSubPlans().get(
				querySpecification.getSubplanId());
		
		String bid = getBid(subplan);
		Double d = Double.valueOf(bid);
		
		messageElements.put("ExecutionBid", bid);
		log.logAction(querySpecification.getSubplanId(), "Bid for execution (" + bid + ")");
		if( d >= 0.0 ) {
			query.setStatus(Lifecycle.GRANTED);
			peer.addQuery(query);
		} else {
			peer.removeQuery(query.getId());
		}
		messageElements.put("queryId", querySpecification.getQueryId());

		messageElements.put("peerId", PeerGroupTool.getPeerGroup().getPeerID()
				.toString());
		messageElements.put("subplanId", querySpecification.getSubplanId());
		messageElements.put("pipeAdvertisement", pipe);
		((JxtaMessageSender) (peer.getMessageSender())).sendMessage(
				PeerGroupTool.getPeerGroup(), MessageTool
						.createOdysseusMessage(
								OdysseusMessageType.BiddingProvider,
								messageElements), pipeAdv, 10);
	}

	private String getBid(Subplan subplan) {
		// Pruefen, ob adressierte Quellen ueberhaupt verwaltet werden
		List<AccessAO> sources = new ArrayList<AccessAO>();
		collectSourcesFromPlan(subplan.getAo(), sources);
		if (!sources.isEmpty()) {
			for (AccessAO ao : sources) {
				if (!GlobalState.getActiveDatadictionary()
						.containsViewOrStream(ao.getSource().getURI(),
								GlobalState.getActiveUser(""))) {
					return "-1";
				}
			}
		}

		// // keine Quellen werden verwaltet
		// if (!GlobalState.getActiveDatadictionary().emptySourceTypeMap()) {
		// return false;
		// }

		IExecutor executor = BiddingClient.getExecutor();
		IAdmissionControl admissionControl = BiddingClient
				.getAdmissionControl();

		try {
			// Plan in Ausführungsplan hinzufügen
			IQuery query = null;
			User user = GlobalState.getActiveUser("");
			IDataDictionary dd = GlobalState.getActiveDatadictionary();

			boolean result = false;
			if (executor == null || admissionControl == null) {
				if (peer.getQueryCount() < MAXQUERIES) {
					result = true;
				}
			} else {
				query = executor.addQuery(subplan.getAo(), user, dd, "Standard");
				subplan.setQuery(query);

				result = admissionControl.canStartQuery(query);
			}

			// Ausführungsplan wieder entfernen, falls
			// nicht darauf geboten wird
			if (result == false && query != null && executor != null) {
				executor.removeQuery(query.getID(), user);
			}
			
			// Wenn AC, dann genaues Gebot bestimmen lassen
			if( admissionControl != null ) {
				IP2PBidGenerator generator = BiddingClient.getBidGenerator();
				double bidValue = 1;
				if( generator != null ) {
					ICost act = admissionControl.getActualCost();
					ICost maxCost = admissionControl.getMaximumCost();
					ICost queryCost = admissionControl.evaluateQuery(query);
					bidValue = generator.generateBid(admissionControl, act, queryCost, maxCost);
				}
				
				return String.valueOf(bidValue);
			}

			return "1";
		} catch (PlanManagementException e) {
			e.printStackTrace();
			return "-1";
		}
	}

	private void collectSourcesFromPlan(ILogicalOperator op,
			List<AccessAO> sources) {
		if ((op instanceof AccessAO) && !(op instanceof P2PAO)) {
			sources.add((AccessAO) op);
		} else {
			for (LogicalSubscription subscription : op.getSubscribedToSource()) {
				collectSourcesFromPlan(subscription.getTarget(), sources);
			}
		}

	}
}
