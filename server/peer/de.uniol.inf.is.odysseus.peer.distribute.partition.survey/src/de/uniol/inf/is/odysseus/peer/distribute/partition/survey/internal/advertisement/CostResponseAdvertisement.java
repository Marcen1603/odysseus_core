package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.internal.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;


public class CostResponseAdvertisement extends Advertisement implements Serializable {
	private static final long serialVersionUID = -1835515099213291456L;
	private static final String ADVERTISEMENT_TYPE = "jxta:CostResponseAdvertisement";
	private static final String ID_TAG = "id";
	private static final String PQL_TAG = "pql";
	private static final String PEER_ID_TAG = "peerId";
	private static final String SHARED_QUERY_ID_TAG = "sharedQueryId";
	private static final String TRANSCFG_NAME_TAG = "transcfg";
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };
	private static final String COST_SUMMARY_TAG = "costSummary";
	private static final String BID_TAG = "bid";
	private static final String CPU_COST_TAG = "cpuCost";
	private static final String MEM_COST_TAG = "memCost";	
	private static final Logger log = LoggerFactory.getLogger(CostResponseAdvertisement.class);
	private static final Gson gson = new Gson();
	private static final Type jsonType = new TypeToken<HashMap<String, CostSummary>>(){}.getType();
	
	private ID id;
	private ID sharedQueryID;
	private String pqlStatement;
	private String transCfgName;
	private PeerID ownerPeerId;
	private double bid;
	private double bearableCpuCostsInPercentage;
	private double bearableMemCostsInPercentage;	
	private Map<String, CostSummary> costSummary;

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}	
	
	public CostResponseAdvertisement() {
		
	}
	
	public CostResponseAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		determineFields(doc);
	}

	public CostResponseAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public CostResponseAdvertisement(CostResponseAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		this.id = adv.id;
		this.sharedQueryID = adv.sharedQueryID;
		this.pqlStatement = adv.pqlStatement;
		this.transCfgName = adv.transCfgName;
		this.ownerPeerId = adv.ownerPeerId;
		this.costSummary = adv.costSummary;
		this.bid = adv.bid;
		this.bearableCpuCostsInPercentage = adv.bearableCpuCostsInPercentage;
		this.bearableMemCostsInPercentage = adv.bearableMemCostsInPercentage;		
	}
	
	@Override
	public CostResponseAdvertisement clone() {
		return new CostResponseAdvertisement(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CostResponseAdvertisement)) {
			return false;
		}

		final CostResponseAdvertisement other = (CostResponseAdvertisement) obj;
		return id.equals(other.id);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		return getDocument(doc);
	}
	
	public Document getDocument(StructuredDocument<?> doc) {
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		String json = gson.toJson(costSummary, jsonType);
		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PQL_TAG, pqlStatement);
		appendElement(doc, PEER_ID_TAG, ownerPeerId.toString());			
		appendElement(doc, SHARED_QUERY_ID_TAG, sharedQueryID.toString());
		appendElement(doc, TRANSCFG_NAME_TAG, transCfgName);
		appendElement(doc, BID_TAG, this.bid+"");
		appendElement(doc, CPU_COST_TAG, this.bearableCpuCostsInPercentage+"");
		appendElement(doc, MEM_COST_TAG, this.bearableMemCostsInPercentage+"");		
		appendElement(doc, COST_SUMMARY_TAG, ""+json);
		
		return doc;
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public String getPqlStatement() {
		return pqlStatement;
	}
	
	public ID getSharedQueryID() {
		return sharedQueryID;
	}	
	
	public String getTransCfgName() {
		return transCfgName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public void setPqlStatement(String pqlStatement) {
		this.pqlStatement = pqlStatement;
	}

	public void setSharedQueryID(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}	
	
	public void setTransCfgName(String transCfgName) {
		this.transCfgName = transCfgName;
	}
	
	private void determineFields(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				setID(convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(PQL_TAG)) {
				setPqlStatement(elem.getTextValue());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				setOwnerPeerId(convertToPeerID(elem.getTextValue()));
			} else if (elem.getName().equals(SHARED_QUERY_ID_TAG)) {
				setSharedQueryID(convertToID(elem.getTextValue()));				
			} else if (elem.getName().equals(TRANSCFG_NAME_TAG)) {
				setTransCfgName(elem.getTextValue());
			}
			 else if (elem.getName().equals(BID_TAG)) {
				this.setBid(Double.valueOf(elem.getTextValue()));
			} else if (elem.getName().equals(CPU_COST_TAG)) {
				this.setPercentageOfBearableCpuCosts(Double.valueOf(elem.getTextValue()));
			} else if (elem.getName().equals(MEM_COST_TAG)) {
				this.setPercentageOfBearableMemCosts(Double.valueOf(elem.getTextValue()));				
			} else if (elem.getName().equals(COST_SUMMARY_TAG)) {
				String json = elem.getTextValue();
				Map<String, CostSummary> costSummary = gson.fromJson(json, jsonType);
				this.setCostSummary(costSummary);	
			}			
		}
	}

	private static PeerID convertToPeerID(String text) {
		try {
			final URI id = new URI(text);
			return PeerID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			log.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	private static ID convertToID(String elem) {
		try {
			final URI id = new URI(elem);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			log.error("Could not set id", ex);
			return null;
		}
	}

	public PeerID getOwnerPeerId() {
		return ownerPeerId;
	}

	public void setOwnerPeerId(PeerID ownerPeerId) {
		this.ownerPeerId = ownerPeerId;
	}

	public Map<String, CostSummary> getCostSummary() {
		return costSummary;
	}

	public void setCostSummary(Map<String, CostSummary> costSummary) {
		this.costSummary = costSummary;
	}

	
	public double getBid() {
		return bid;
	}
	public void setBid(double bid) {
		this.bid = bid;
	}
	public double getPercentageOfBearableCpuCosts() {
		return bearableCpuCostsInPercentage;
	}
	public void setPercentageOfBearableCpuCosts(double bearableCostsInPercentage) {
		this.bearableCpuCostsInPercentage = bearableCostsInPercentage;
	}
	
	public double getPercentageOfBearableMemCosts() {
		return bearableMemCostsInPercentage;
	}
	public void setPercentageOfBearableMemCosts(double bearableCostsInPercentage) {
		this.bearableMemCostsInPercentage = bearableCostsInPercentage;
	}
}
