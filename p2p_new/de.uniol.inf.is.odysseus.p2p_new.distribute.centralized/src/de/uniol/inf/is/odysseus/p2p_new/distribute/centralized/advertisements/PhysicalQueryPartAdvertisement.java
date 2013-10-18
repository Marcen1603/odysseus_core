package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.PlanIntersection;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.PlanJunction;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.HelperProvider;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.IPhysicalOperatorHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.PlanIntersectionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.SubscriptionHelper;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredDocumentUtils;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

@SuppressWarnings("unused")
public class PhysicalQueryPartAdvertisement extends Advertisement {
	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryPartAdvertisement.class);
	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerid";
	private static final String MASTER_PEER_ID_TAG = "masterpeerid";
	private static final String NEW_OPERATORS_TAG = "new_operators";
	private static final String SUBSCRIPTIONS_TAG = "subscriptions";
	private static final String PLANINTERSECTIONS_TAG = "planIntersections";
	private static final String SHARED_QUERY_ID_TAG = "sharedQueryID";
	private static final String ADVERTISEMENT_TYPE = "jxta:PhysicalQueryPartAdvertisement";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };

	private ID id;
	private ID sharedQueryID;
	private PeerID masterPeerID;
	private PeerID peerID;
	// information about subscriptions between new operators and operators from the old plan
	private List<PlanIntersection> intersections;
	
	private StructuredDocument<? extends TextElement<?>> newOperatorsStatement;
	private StructuredDocument<? extends TextElement<?>> subscriptionsStatement;
	private StructuredDocument<? extends TextElement<?>> planIntersectionsStatement;
	
	private Map<Integer, IPhysicalOperator> queryPartOperatorObjects = new HashMap<Integer, IPhysicalOperator>();

	/**
	 * This constructor rebuilds the Advertisement from the Document sent by another peer
	 * @param root the document from which to re-create the Advertisement
	 */
	@SuppressWarnings("unchecked")
	public PhysicalQueryPartAdvertisement(Element<?> root) {
		if(root != null) {
			root = (TextElement<?>) root;
		} else {
			LOG.debug("can't instantiate from null");
		}
		final Enumeration<?> elements = root.getChildren();

		TextElement<?> subscriptionElement = null;
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				this.id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(NEW_OPERATORS_TAG)) {
				this.setNewOperatorsStatement(StructuredDocumentUtils.copyAsDocument(elem));
				//System.out.println(this.getNewOperatorsStatement().toString());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				this.peerID = (PeerID)convertToID(elem.getTextValue());
			} else if (elem.getName().equals(SUBSCRIPTIONS_TAG)) {
				// it's essential to process the subscriptions last
				this.setSubscriptionsStatement(StructuredDocumentUtils.copyAsDocument(elem));
				//System.out.println(this.getSubscriptionsStatement().toString());
				//StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8,SUBSCRIPTIONS_TAG);
			} else if (elem.getName().equals(MASTER_PEER_ID_TAG)) {
				this.setMasterPeerID((PeerID)convertToID(elem.getTextValue()));
			} else if(elem.getName().equals(PLANINTERSECTIONS_TAG)) {
				this.setPlanIntersectionsStatement(StructuredDocumentUtils.copyAsDocument(elem));
				//System.out.println(this.getPlanIntersectionsStatement().toString());
			} else if(elem.getName().equals(SHARED_QUERY_ID_TAG)) {
				this.setSharedQueryID(convertToID(elem.getTextValue()));
			}
		}
	}

	/**
	 * Generates the connections between the operators based on the given list of subscriptions in textual form
	 * Since we only provide the new operators at this point, the subscriptions to the old operators are handled separately and via the planIntersections-objects
	 * @param subscriptionElement the list of subscription-statements
	 */
	public void handleSubscriptionsStatement() {
		StructuredDocument<? extends TextElement<?>> subscriptionElement = this.getSubscriptionsStatement();
		SubscriptionHelper.reconnectOperators(this.queryPartOperatorObjects, subscriptionElement);
	}

	@SuppressWarnings("unchecked")
	public void handleOperatorStatement() {
		StructuredDocument<? extends TextElement<?>> statement = this.getNewOperatorsStatement().getRoot();
		Enumeration<? extends TextElement<?>> elements = statement.getChildren();
		while(elements.hasMoreElements()) {
			TextElement<? extends TextElement<?>> elem = elements.nextElement();
			String operatorType = elem.getName();
			IPhysicalOperatorHelper<?> helper = HelperProvider.getInstance().getPhysicalOperatorHelper(operatorType);
			if(helper != null) {
				Entry<Integer,? extends IPhysicalOperator> e = helper.createOperatorFromStatement(elem, true);
				queryPartOperatorObjects.put(e.getKey(),e.getValue());
			}
		}
	}
	
	public void handlePlanIntersectionStatement() {
		StructuredDocument<? extends TextElement<?>> statement = this.getPlanIntersectionsStatement();
		this.setIntersections(PlanIntersectionHelper.createPlanIntersectionsFromStatement(statement));
	}
	
	public PhysicalQueryPartAdvertisement() {
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument doc =  StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		doc.appendChild(doc.createElement(ID_TAG, id.toString()));

		//System.out.println(getNewOperatorsStatement().getKey());
		//System.out.println(getNewOperatorsStatement().getValue());
		//System.out.println(getNewOperatorsStatement().getChildren().nextElement());
		StructuredDocumentUtils.copyElements(doc,doc.getRoot(),getNewOperatorsStatement());

		doc.appendChild(doc.createElement(PEER_ID_TAG, peerID.toString()));
		doc.appendChild(doc.createElement(MASTER_PEER_ID_TAG, masterPeerID.toString()));
		
		StructuredDocumentUtils.copyElements(doc,doc.getRoot(),getSubscriptionsStatement());
	
		StructuredDocumentUtils.copyElements(doc,doc.getRoot(),getPlanIntersectionsStatement());
		
		doc.appendChild(doc.createElement(SHARED_QUERY_ID_TAG, sharedQueryID.toString()));
		return doc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generatePlanIntersectionsStatement(MimeMediaType asMimeType) {
		StructuredDocument doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, PLANINTERSECTIONS_TAG);
		setPlanIntersectionsStatement(PlanIntersectionHelper.generatePlanIntersectionStatement(asMimeType, this.getIntersections(),doc));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateSubscriptionsDocument(MimeMediaType asMimeType) {
		StructuredDocument doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, SUBSCRIPTIONS_TAG);
		setSubscriptionsStatement(SubscriptionHelper.generateSubscriptionStatement(asMimeType, this.queryPartOperatorObjects.values(), doc));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generateOperatorsDocument(MimeMediaType asMimeType) {
		StructuredDocument doc = StructuredDocumentFactory.newStructuredDocument(asMimeType,NEW_OPERATORS_TAG);
		for(IPhysicalOperator o : this.queryPartOperatorObjects.values()) {
			IPhysicalOperatorHelper<?> gen = HelperProvider.getInstance().getPhysicalOperatorHelper(o);
			if(gen != null) {
				// use the class of the operator as a tag, in order to get the right Helper on the other side to re-assemble it
				Element opElement = doc.createElement(gen.getOperatorClass().getName().toString());
				doc.appendChild(opElement);
				gen.generateStatement(o,asMimeType,true,doc,opElement);
			}
		}
		setNewOperatorsStatement(doc);
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}
	
	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public PeerID getMasterPeerID() {
		return masterPeerID;
	}

	public void setMasterPeerID(PeerID masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public Map<Integer, IPhysicalOperator> getQueryPartOperatorObjects() {
		return queryPartOperatorObjects;
	}

	public void setQueryPartOperatorObjects(
			Map<Integer, IPhysicalOperator> queryPartOperatorObjects) {
		this.queryPartOperatorObjects = queryPartOperatorObjects;
	}

	public StructuredDocument<?> getNewOperatorsStatement() {
		return newOperatorsStatement;
	}

	public void setNewOperatorsStatement(StructuredDocument<? extends TextElement<?>> newOperatorsStatement) {
		this.newOperatorsStatement = newOperatorsStatement;
	}

	public StructuredDocument<? extends TextElement<?>> getSubscriptionsStatement() {
		return subscriptionsStatement;
	}

	public void setSubscriptionsStatement(
			StructuredDocument<? extends TextElement<?>> subscriptionsStatement) {
		this.subscriptionsStatement = subscriptionsStatement;
	}
	
	private static ID convertToID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, Object value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	public List<PlanIntersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(List<PlanIntersection> intersections) {
		this.intersections = intersections;
	}

	public StructuredDocument<? extends TextElement<?>> getPlanIntersectionsStatement() {
		return planIntersectionsStatement;
	}

	public void setPlanIntersectionsStatement(
			StructuredDocument<? extends TextElement<?>> planIntersectionsStatement) {
		this.planIntersectionsStatement = planIntersectionsStatement;
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	public void setSharedQueryID(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}	
}
