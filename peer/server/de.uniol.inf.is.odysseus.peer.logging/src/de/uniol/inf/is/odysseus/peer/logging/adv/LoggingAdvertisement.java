package de.uniol.inf.is.odysseus.peer.logging.adv;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.google.common.base.Preconditions;

public class LoggingAdvertisement extends Advertisement {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingAdvertisement.class);
	private static final String ADVERTISEMENT_TYPE = "jxta:LoggingAdvertisement";

	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerID";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };

	private ID id;
	private PeerID peerID;

	public LoggingAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public LoggingAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public LoggingAdvertisement(LoggingAdvertisement loggingAdvertisement) {
		Preconditions.checkNotNull(loggingAdvertisement, "Advertisement to copy must not be null!");

		id = loggingAdvertisement.id;
		peerID = loggingAdvertisement.peerID;
	}

	public LoggingAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public LoggingAdvertisement clone() throws CloneNotSupportedException {
		return new LoggingAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PEER_ID_TAG, peerID.toString());

		return doc;
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));
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

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
}
