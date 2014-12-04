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

public class RecoveryAllocatorAdvertisement extends Advertisement implements Serializable {
	
	private static final String ADVERTISEMENT_TYPE = "jxta:RecoveryAllocatorAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryAllocatorAdvertisement.class);

	private static final String ALLOCATOR_NAME = "allocatorName";
	private static final String PEER_ID_TAG = "peerID";
	private static final String ID_TAG = "id";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG, ALLOCATOR_NAME };
	
	private String allocatorName;
	private PeerID peerID;
	private ID id;

	public RecoveryAllocatorAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public RecoveryAllocatorAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public RecoveryAllocatorAdvertisement(RecoveryAllocatorAdvertisement advertisement) {
		Preconditions.checkNotNull(advertisement, "Advertisement to copy must not be null!");
		allocatorName = advertisement.allocatorName;
		peerID = advertisement.peerID;
		id = advertisement.id;
	}

	public RecoveryAllocatorAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public RecoveryAllocatorAdvertisement clone() throws CloneNotSupportedException {
		return new RecoveryAllocatorAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, ALLOCATOR_NAME, allocatorName+"");
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

	public String getAllocatorName() {
		return this.allocatorName;
	}
	
	public void setAllocatorName(String allocatorName) {
		this.allocatorName = allocatorName;
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
		if (!(obj instanceof RecoveryAllocatorAdvertisement)) {
			return false;
		}
		RecoveryAllocatorAdvertisement adv = (RecoveryAllocatorAdvertisement) obj;
		return
				Objects.equals(adv.peerID, peerID) &&
				Objects.equals(adv.id, id) &&
				Objects.equals(adv.allocatorName, allocatorName); 
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, allocatorName);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ALLOCATOR_NAME)) {
			setAllocatorName(elem.getTextValue());
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