package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement;

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
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public final class AuctionQueryAdvertisement extends Advertisement implements Serializable {
	private static final long serialVersionUID = -4302110920850371521L;
	private static final String ADVERTISEMENT_TYPE = "jxta:AuctionAdvertisement";
	private static final String ID_TAG = "id";
	private static final String PQL_TAG = "pql";
	private static final String PEER_ID_TAG = "peerId";
	private static final String SHARED_QUERY_ID_TAG = "sharedQueryId";
	private static final String TRANSCFG_NAME_TAG = "transcfg";
	private static final String AUCTION_ID_TAG = "auctionId";
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG };
	private static final Logger log = LoggerFactory.getLogger(AuctionQueryAdvertisement.class);
	
	private ID id;
	private ID sharedQueryID;
	private String auctionId;
	private String pqlStatement;
	private String transCfgName;
	private PeerID ownerPeerId;

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}	
	
	public AuctionQueryAdvertisement() {
		
	}
	
	public AuctionQueryAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		determineFields(doc);
	}

	public AuctionQueryAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public AuctionQueryAdvertisement(AuctionQueryAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		this.id = adv.id;
		this.sharedQueryID = adv.sharedQueryID;
		this.pqlStatement = adv.pqlStatement;
		this.transCfgName = adv.transCfgName;
		this.ownerPeerId = adv.ownerPeerId;
		this.auctionId = adv.pqlStatement;
	}
	
	@Override
	public AuctionQueryAdvertisement clone() {
		return new AuctionQueryAdvertisement(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuctionQueryAdvertisement)) {
			return false;
		}

		final AuctionQueryAdvertisement other = (AuctionQueryAdvertisement) obj;
		return id.equals(other.id);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PQL_TAG, pqlStatement);
		appendElement(doc, PEER_ID_TAG, ownerPeerId.toString());
		appendElement(doc, AUCTION_ID_TAG, auctionId.toString());
		appendElement(doc, SHARED_QUERY_ID_TAG, sharedQueryID.toString());
		appendElement(doc, TRANSCFG_NAME_TAG, transCfgName);

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
	
	public String getAuctionId() {
		return auctionId;
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
	
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
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
			} else if (elem.getName().equals(AUCTION_ID_TAG)) {
				setAuctionId(elem.getTextValue());
			} else if (elem.getName().equals(SHARED_QUERY_ID_TAG)) {
				setSharedQueryID(convertToID(elem.getTextValue()));				
			} else if (elem.getName().equals(TRANSCFG_NAME_TAG)) {
				setTransCfgName(elem.getTextValue());
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
}
