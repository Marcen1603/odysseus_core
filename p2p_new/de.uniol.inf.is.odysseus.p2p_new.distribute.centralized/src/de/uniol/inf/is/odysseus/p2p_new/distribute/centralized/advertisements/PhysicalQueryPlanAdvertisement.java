package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.HelperProvider;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.IPhysicalOperatorHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers.SubscriptionHelper;
import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

public class PhysicalQueryPlanAdvertisement extends Advertisement implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryPlanAdvertisement.class);
	
	private static final String ADVERTISEMENT_TYPE = "jxta:PhysicalQueryPlanAdvertisement";
	private static final String ID_TAG = "id";
	private static final String OPERATORS_TAG = "operators";
	private static final String PEER_ID_TAG = "peerid";
	private static final String MASTER_PEER_ID_TAG = "masterpeerid";
	private static final String SUBSCRIPTIONS_TAG = "subscriptions";
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };
	
	private ID id;
	private PeerID masterPeerID;
	private StructuredDocument<?> operators;
	private StructuredDocument<?> subscriptions;
	private PeerID peerID;
	private Map<Integer,IPhysicalOperator> opObjects =  new HashMap<Integer,IPhysicalOperator>();
	
	/**
	 * This constructor rebuilds the Advertisement from the Document sent by another peer
	 * @param root the document from which to re-create the Advertisement
	 */
	public PhysicalQueryPlanAdvertisement(Element<?> root) {
		if( root == null) {
			throw new IllegalArgumentException("Can't instantiate from null");
		}
		final Enumeration<?> elements = root.getChildren();
		TextElement<?> subscriptionElement = null;
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				this.id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(OPERATORS_TAG)) {
				handleOperatorStatement(elem);
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				this.peerID = (PeerID)convertToID(elem.getTextValue());
			} else if (elem.getName().equals(SUBSCRIPTIONS_TAG)) {
				// it's essential to process the subscriptions last
				subscriptionElement = elem;
			} else if (elem.getName().equals(MASTER_PEER_ID_TAG)) {
				this.setMasterPeerID((PeerID)convertToID(elem.getTextValue()));
			}
		}
		if(subscriptionElement != null) {
			handleSubscriptionsStatement(subscriptionElement);
		}
	}
	
	public PhysicalQueryPlanAdvertisement() {
		super();
	}
	/**
	 * Generates the connections between the operators based on the given list of subscriptions in textual form
	 * @param subscriptionElement the list of subscription-statements
	 */
	@SuppressWarnings("unchecked")
	private void handleSubscriptionsStatement(TextElement<?> subscriptionElement) {
		SubscriptionHelper.reconnectOperators(this.opObjects, subscriptionElement.getRoot());
	}

	/**
	 * re-creates the operators based on the string-representation in the given document
	 * @param the operators-statement which contains all operators
	 */
	private void handleOperatorStatement(TextElement<?> statement) {
		Enumeration<? extends TextElement<?>> elements = statement.getChildren();
		while(elements.hasMoreElements()) {
			TextElement<? extends TextElement<?>> elem = elements.nextElement();
			String operatorType = elem.getName();
			IPhysicalOperatorHelper<?> helper = HelperProvider.getInstance().getPhysicalOperatorHelper(operatorType);
			if(helper != null) {
				Entry<Integer,? extends IPhysicalOperator> e = helper.createOperatorFromStatement(elem, false);
				opObjects.put(e.getKey(),e.getValue());
			}
		}
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

	@Override @SuppressWarnings({"rawtypes","unchecked"})
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument doc =  StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		doc.appendChild(doc.createElement(ID_TAG, id.toString()));
		generateOperatorsDocument(asMimeType);
		doc.appendChild(doc.createElement(OPERATORS_TAG, getOperators().toString()));
		doc.appendChild(doc.createElement(PEER_ID_TAG, peerID.toString()));
		doc.appendChild(doc.createElement(MASTER_PEER_ID_TAG, masterPeerID.toString()));
		generateSubscriptionsDocument(asMimeType);
		doc.appendChild(doc.createElement(SUBSCRIPTIONS_TAG, getSubscriptions().toString()));
		return doc;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, Object value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public StructuredDocument<?> getOperators() {
		return operators;
	}

	public void setOperators(StructuredDocument<?> operators) {
		this.operators = operators;
	}

	public StructuredDocument<?> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(StructuredDocument<?> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	@SuppressWarnings("rawtypes")
	private void generateSubscriptionsDocument(MimeMediaType asMimeType) {
		StructuredDocument doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, SUBSCRIPTIONS_TAG);
		setSubscriptions(SubscriptionHelper.generateSubscriptionStatement(asMimeType, this.opObjects.values(), doc));
	}
	
	private void generateOperatorsDocument(MimeMediaType asMimeType) {
		StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType,getAdvertisementType());
		for(IPhysicalOperator o : this.opObjects.values()) {
			IPhysicalOperatorHelper<?> gen = HelperProvider.getInstance().getPhysicalOperatorHelper(o);
			if(gen != null) {
				// use the class of the operator as a tag, in order to get the right Helper on the other side to re-assemble it
				appendElement(doc,gen.getOperatorClass().getName().toString(),gen.generateStatement(o,asMimeType,false,doc,doc.getRoot()));
			}
		}
		setOperators(doc);
	}
	
	@Override
	public ID getID() {
		return id;
	}
	public void setID(ID id) {
		this.id = id;
	}
	public PeerID getPeerID() {
		return peerID;
	}
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	public Map<Integer,IPhysicalOperator> getOpObjects() {
		return opObjects;
	}
	public void setOpObjects(Map<Integer,IPhysicalOperator> opObjects) {
		this.opObjects = opObjects;
	}
	public PeerID getMasterPeerID() {
		return masterPeerID;
	}
	public void setMasterPeerID(PeerID masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

}
