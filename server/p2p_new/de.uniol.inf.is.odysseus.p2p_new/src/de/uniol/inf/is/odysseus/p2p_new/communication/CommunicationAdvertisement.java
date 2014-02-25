package de.uniol.inf.is.odysseus.p2p_new.communication;

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
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class CommunicationAdvertisement extends Advertisement implements Serializable {
	
	private static final String ADVERTISEMENT_TYPE = "jxta:CommunicationAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CommunicationAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerID";
	private static final String PIPE_ID_TAG = "pipeID";
	private static final String PEER_NAME_TAG = "peerName";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };
	
	private ID id;
	private PipeID pipeID;
	private PeerID peerID;
	private String peerName;
	
	public CommunicationAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public CommunicationAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public CommunicationAdvertisement(CommunicationAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Advertisement to copy must not be null!");

		id = viewAdvertisement.id;
		pipeID = viewAdvertisement.pipeID;
		peerID = viewAdvertisement.peerID;
	}

	public CommunicationAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public CommunicationAdvertisement clone() throws CloneNotSupportedException {
		return new CommunicationAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PIPE_ID_TAG, pipeID.toString());
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		appendElement(doc, PEER_NAME_TAG, peerName);
		
		return doc;
	}

	@Override
	public ID getID() {
		return id;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public void setPipeID(PipeID pipeID) {
		this.pipeID = pipeID;
	}

	public PipeID getPipeID() {
		return pipeID;
	}
	
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public PeerID getPeerID() {
		return peerID;
	}
	
	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}
	
	public String getPeerName() {
		return peerName;
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
		if (!(obj instanceof CommunicationAdvertisement)) {
			return false;
		}
		CommunicationAdvertisement adv = (CommunicationAdvertisement) obj;
		return
				Objects.equals(adv.id, id) &&
				Objects.equals(adv.peerID, peerID) && 
				Objects.equals(adv.pipeID, pipeID);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, peerID, pipeID);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));
		} else if (elem.getName().equals(PIPE_ID_TAG)) {
			setPipeID((PipeID) toID(elem));
		} else if (elem.getName().equals(PEER_NAME_TAG)) {
			setPeerName(elem.getTextValue());
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
}
