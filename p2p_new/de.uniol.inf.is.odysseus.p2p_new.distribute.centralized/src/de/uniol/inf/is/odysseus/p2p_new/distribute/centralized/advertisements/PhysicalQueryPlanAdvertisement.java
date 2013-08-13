package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public class PhysicalQueryPlanAdvertisement extends Advertisement implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String ADVERTISEMENT_TYPE = "jxta:PhysicalQueryPlanAdvertisement";
	private static final String ID_TAG = "id";
	private static final String OPERATORS_TAG = "operators";
	private static final String OPERATOR_TAG = "operators";
	private static final String PEER_ID_TAG = "peerid";
	private static final String SUBSCRIPTIONS_TAG = "subscriptions";
	private final Map<Class<? extends IPhysicalOperator>, IPhysicalOperatorHelper<IPhysicalOperator>> helpers = Maps.newHashMap();
	
	private ID id;
	private StructuredDocument<?> operators;
	private StructuredDocument<?> subscriptions;
	private PeerID peerID;
	private List<IPhysicalOperator> opObjects;
	
	@Override @SuppressWarnings({"rawtypes","unchecked"})
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument doc =  StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		doc.appendChild(doc.createElement(ID_TAG, id.toString()));
		generateOperatorsDocument(asMimeType);
		doc.appendChild(doc.createElement(OPERATORS_TAG, getOperators()));
		doc.appendChild(doc.createElement(PEER_ID_TAG, peerID.toString()));
		generateSubscriptionsDocument(asMimeType);
		doc.appendChild(doc.createElement(SUBSCRIPTIONS_TAG, getSubscriptions()));
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, Object value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@Override
	public ID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getIndexFields() {
		// TODO Auto-generated method stub
		return null;
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
	
	private void generateSubscriptionsDocument(MimeMediaType asMimeType) {
		setSubscriptions(SubscriptionHelper.generateSubscriptionStatement(asMimeType, this.opObjects));
	}
	
	private void generateOperatorsDocument(MimeMediaType asMimeType) {
		StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType,getAdvertisementType());
		for(IPhysicalOperator o : this.opObjects) {
			IPhysicalOperatorHelper<?> gen = getPhysicalOperatorStatementGenerator(o);
			if(gen != null) {
				appendElement(doc,OPERATOR_TAG,gen.generateStatement(o,asMimeType));
			}
		}
		setOperators(doc);
	}
	
	@SuppressWarnings("unchecked")
	public void bindPhysicalOperatorStatementGenerator(IPhysicalOperatorHelper<?> helper) {
		helpers.put(helper.getOperatorClass(), (IPhysicalOperatorHelper<IPhysicalOperator>) helper);
	}
	
	public void unbindPhysicalOperatorStatementGenerator(IPhysicalOperatorHelper<?> helper) {
		helpers.remove(helper.getOperatorClass());
	}
	
	public IPhysicalOperatorHelper<IPhysicalOperator> getPhysicalOperatorStatementGenerator(IPhysicalOperator operator) {
		Preconditions.checkNotNull(operator, "Operator mustn't be null");
		
		IPhysicalOperatorHelper<IPhysicalOperator> h = helpers.get(operator.getClass());
		return h;
	}

}
