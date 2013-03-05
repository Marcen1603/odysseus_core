package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import net.jxta.document.Advertisement;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public final class QueryPartAdvertisement extends Advertisement implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartAdvertisement.class);
	private static final String ADVERTISEMENT_TYPE = "jxta:QueryPartAdvertisement";
	private static final long serialVersionUID = 1L;

	private static final String ID_TAG = "id";
	private static final String PQL_TAG = "pql";
	private static final String PEER_ID_TAG = "peerid";
	private static final String SHARED_QUERY_ID_TAG = "sharedQueryID";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };

	private ID id;
	private String pqlStatement;
	private PeerID peerID;
	private ID sharedQueryID;

	public QueryPartAdvertisement(Element<?> root) {
		TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		determineFields(doc);
	}

	public QueryPartAdvertisement(InputStream stream) throws IOException {
		this((StructuredTextDocument<?>) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public QueryPartAdvertisement(QueryPartAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		id = adv.id;
		pqlStatement = adv.pqlStatement;
		peerID = adv.peerID;
	}

	QueryPartAdvertisement() {
		// for JXTA-side instances
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public void setID(ID id) {
		this.id = id;
	}

	@Override
	public ID getID() {
		return id;
	}

	public void setPqlStatement(String pqlStatement) {
		this.pqlStatement = pqlStatement;
	}

	public String getPqlStatement() {
		return pqlStatement;
	}

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	public PeerID getPeerID() {
		return peerID;
	}
	
	public void setSharedQueryID(ID sharedQueryID) {
		this.sharedQueryID = sharedQueryID;
	}
	
	public ID getSharedQueryID() {
		return sharedQueryID;
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PQL_TAG, pqlStatement);
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		appendElement(doc, SHARED_QUERY_ID_TAG, sharedQueryID.toString());
		
		return doc;
	}

	@Override
	public QueryPartAdvertisement clone() {
		return new QueryPartAdvertisement(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof QueryPartAdvertisement)) {
			return false;
		}

		QueryPartAdvertisement other = (QueryPartAdvertisement) obj;
		return id.equals(other.id);
	}

	private void determineFields(TextElement<?> root) {
		Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(convertToID(elem.getTextValue()));
		} else if (elem.getName().equals(PQL_TAG)) {
			setPqlStatement(elem.getTextValue());
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID(convertToPeerID(elem.getTextValue()));
		} else if (elem.getName().equals(SHARED_QUERY_ID_TAG)) {
			setSharedQueryID(convertToID(elem.getTextValue()));
		}
	}

	private static ID convertToID(String elem) {
		try {
			URI id = new URI(elem);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	private static PeerID convertToPeerID(String text) {
		try {
			URI id = new URI(text);
			return PeerID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}
}
