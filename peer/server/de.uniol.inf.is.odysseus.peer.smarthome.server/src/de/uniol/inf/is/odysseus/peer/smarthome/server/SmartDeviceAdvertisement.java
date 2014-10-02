package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.io.Serializable;
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
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

public class SmartDeviceAdvertisement extends Advertisement implements
		Serializable {

	private static final long serialVersionUID = 1253246425406629535L;
	private static final String ADVERTISEMENT_TYPE = "jxta:SmartDeviceAdvertisement";

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SmartDeviceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PEER_ID_TAG = "originalPeerID";
	private static final String CONTEXT_NAME = "contextName";
	

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, NAME_TAG, PEER_ID_TAG };

	private ID id;
	private String name;
	@SuppressWarnings("unused")
	private PipeID pipeID;
	private PeerID peerID;
	
	public SmartDeviceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			//TODO: 
			@SuppressWarnings("unused")
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			//handleElement(elem);
		}
	}

	public SmartDeviceAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		
		appendTo(doc);

		return doc;
	}
	
	void appendTo( Element<?> doc ) {
		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, NAME_TAG, name);
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		appendElement(doc, CONTEXT_NAME, "ctxName");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag, String value) {
		final Element ele = appendTo.getRoot().createElement(tag, value);
		appendTo.appendChild(ele);
		return ele;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static Element appendElement(Element appendTo, String tag) {
		final Element ele = appendTo.getRoot().createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

	@Override
	public ID getID() {
		return id;
	}
	
	public void setID(ID id) {
		this.id = id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
}
