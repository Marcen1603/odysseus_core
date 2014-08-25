package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

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

public class DDCAdvertisement extends Advertisement {

	private static final String ADVERTISEMENT_TYPE = "jxta:DDCAdvertisement";
	private ID id;
	private PeerID ownerPeerId;
	private static final String ID_TAG = "id";
	private static final String OWNER_PEER_ID_TAG = "ownerPeerId";
	private static final Logger LOG = LoggerFactory
			.getLogger(DDCAdvertisement.class);
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, OWNER_PEER_ID_TAG };;

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public DDCAdvertisement() {

	}

	public DDCAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(
				root, "Root element must not be null!");

		determineFields(doc);
	}

	public DDCAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(
				MimeMediaType.XMLUTF8,
				Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public DDCAdvertisement(DDCAdvertisement adv) {
		Preconditions.checkNotNull(adv,
				"Advertisement to copy must not be null!");

		this.id = adv.id;
		this.ownerPeerId = adv.ownerPeerId;
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory
				.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, OWNER_PEER_ID_TAG, ownerPeerId.toString());

		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo,
			String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	private void determineFields(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				setID(convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(OWNER_PEER_ID_TAG)) {
				setOwnerPeerId(convertToPeerID(elem.getTextValue()));
			}
		}
	}

	private static ID convertToID(String elem) {
		try {
			final URI id = new URI(elem);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	private static PeerID convertToPeerID(String text) {
		try {
			final URI id = new URI(text);
			return PeerID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public void setID(ID newPipeID) {
		this.id = newPipeID;

	}

	public void setOwnerPeerId(PeerID ownerPeerId) {
		this.ownerPeerId = ownerPeerId;
	}

	public PeerID getOwnerPeerId() {
		return ownerPeerId;
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
		if (!(obj instanceof DDCAdvertisement)) {
			return false;
		}

		final DDCAdvertisement other = (DDCAdvertisement) obj;
		return id.equals(other.id);
	}

}
