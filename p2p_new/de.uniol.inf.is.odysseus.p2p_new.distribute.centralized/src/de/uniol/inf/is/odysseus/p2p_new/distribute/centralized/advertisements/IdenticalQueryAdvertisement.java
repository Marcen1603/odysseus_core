package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class IdenticalQueryAdvertisement extends Advertisement {
	private static final Logger LOG = LoggerFactory.getLogger(IdenticalQueryAdvertisement.class);
	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerid";
	private static final String MASTER_PEER_ID_TAG = "masterpeerid";
	private static final String ADVERTISEMENT_TYPE = "jxta:IdenticalQueryAdvertisement";
	private static final String OLD_SHARED_QUERY_ID = "old_shared_query_id";
	private static final String NEW_SHARED_QUERY_ID = "new_shared_query_id";
	private ID id;
	private PeerID masterPeerID;
	private PeerID peerID;
	private ID oldSharedQueryID;
	private ID newSharedQueryID;
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG, MASTER_PEER_ID_TAG };
	
	public IdenticalQueryAdvertisement() {
		super();
	}
	
	public IdenticalQueryAdvertisement(Element<?> root) {
		if( root == null) {
			throw new IllegalArgumentException("Can't instantiate from null");
		}
		final Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				this.id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				this.peerID = (PeerID)convertToID(elem.getTextValue());
			} else if (elem.getName().equals(MASTER_PEER_ID_TAG)) {
				this.setMasterPeerID((PeerID)convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(OLD_SHARED_QUERY_ID)) {
				this.setOldSharedQueryID(convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(NEW_SHARED_QUERY_ID)) {
				this.setNewSharedQueryID(convertToID(elem.getTextValue()));
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument doc =  StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		doc.appendChild(doc.createElement(ID_TAG, id.toString()));
		doc.appendChild(doc.createElement(PEER_ID_TAG, peerID.toString()));
		doc.appendChild(doc.createElement(MASTER_PEER_ID_TAG, masterPeerID.toString()));
		doc.appendChild(doc.createElement(OLD_SHARED_QUERY_ID, oldSharedQueryID.toString()));
		doc.appendChild(doc.createElement(NEW_SHARED_QUERY_ID, newSharedQueryID.toString()));
		return doc;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}
	
	@Override
	public ID getID() {
		return id;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public PeerID getMasterPeerID() {
		return masterPeerID;
	}

	public void setMasterPeerID(PeerID masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public ID getOldSharedQueryID() {
		return oldSharedQueryID;
	}

	public void setOldSharedQueryID(ID oldSharedQueryID) {
		this.oldSharedQueryID = oldSharedQueryID;
	}

	public ID getNewSharedQueryID() {
		return newSharedQueryID;
	}

	public void setNewSharedQueryID(ID newSharedQueryID) {
		this.newSharedQueryID = newSharedQueryID;
	}

}
