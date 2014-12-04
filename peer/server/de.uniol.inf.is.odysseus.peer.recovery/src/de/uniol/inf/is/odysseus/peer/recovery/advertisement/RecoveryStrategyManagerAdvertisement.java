package de.uniol.inf.is.odysseus.peer.recovery.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Objects;

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

public class RecoveryStrategyManagerAdvertisement extends Advertisement implements Serializable {
	
	private static final String ADVERTISEMENT_TYPE = "jxta:RecoveryStrategyManagerAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryStrategyManagerAdvertisement.class);

	private static final String STRATEGY_MANAGER_NAME = "strategyManagerName";
	private static final String PEER_ID_TAG = "peerID";
	private static final String ID_TAG = "id";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG, STRATEGY_MANAGER_NAME };
	
	private String strategyManagerName;
	private PeerID peerID;
	private ID id;

	public RecoveryStrategyManagerAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public RecoveryStrategyManagerAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public RecoveryStrategyManagerAdvertisement(RecoveryStrategyManagerAdvertisement advertisement) {
		Preconditions.checkNotNull(advertisement, "Advertisement to copy must not be null!");
		strategyManagerName = advertisement.strategyManagerName;
		peerID = advertisement.peerID;
		id = advertisement.id;
	}

	public RecoveryStrategyManagerAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public RecoveryStrategyManagerAdvertisement clone() throws CloneNotSupportedException {
		return new RecoveryStrategyManagerAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, STRATEGY_MANAGER_NAME, strategyManagerName+"");
		appendElement(doc, PEER_ID_TAG, peerID.toString());

		return doc;
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

	public String getStrategyManagerName() {
		return this.strategyManagerName;
	}
	
	public void setStrategyManagerName(String strategyManagerName) {
		this.strategyManagerName = strategyManagerName;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RecoveryStrategyManagerAdvertisement)) {
			return false;
		}
		RecoveryStrategyManagerAdvertisement adv = (RecoveryStrategyManagerAdvertisement) obj;
		return
				Objects.equals(adv.peerID, peerID) &&
				Objects.equals(adv.id, id) &&
				Objects.equals(adv.strategyManagerName, strategyManagerName); 
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, strategyManagerName);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(STRATEGY_MANAGER_NAME)) {
			setStrategyManagerName(elem.getTextValue());
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));
		} else if( elem.getName().equals(ID_TAG)) {
			setID(toID(elem));
		}
	}
	

	
	private static ID toID(TextElement<?> elem) {
		return toID(elem.getTextValue());
	}
	
	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@Override
	public ID getID() {
		return this.id;
	}
}