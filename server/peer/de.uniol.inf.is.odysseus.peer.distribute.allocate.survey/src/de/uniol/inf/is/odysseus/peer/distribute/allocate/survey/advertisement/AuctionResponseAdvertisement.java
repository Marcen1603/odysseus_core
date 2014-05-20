package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement;

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

import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;

public final class AuctionResponseAdvertisement extends Advertisement implements Serializable {

	private static final long serialVersionUID = 8933981347574448287L;
	private static final Logger LOG = LoggerFactory.getLogger(AuctionResponseAdvertisement.class);

	private static final String ADVERTISEMENT_TYPE = "jxta:BidAdvertisement";
	private static final String ID_TAG = "id";
	private static final String AUCTION_ID_TAG = "auctionId";
	private static final String BID_TAG = "bid";
	private static final String PEER_ID_TAG = "peerid";
	private static final String POSITION_X_ID_TAG = "positionX";
	private static final String POSITION_Y_ID_TAG = "positionY";
	private static final String POSITION_Z_ID_TAG = "positionZ";
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, AUCTION_ID_TAG };

	private ID id;
	private Bid bid;
	private ID auctionId;
	private Vector3D pingMapPosition;

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public AuctionResponseAdvertisement() {

	}

	public AuctionResponseAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		determineFields(doc);
	}

	public AuctionResponseAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public AuctionResponseAdvertisement(AuctionResponseAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		this.id = adv.id;
		this.auctionId = adv.auctionId;
		this.bid = adv.bid;
		this.pingMapPosition = adv.pingMapPosition;
	}

	@Override
	public AuctionResponseAdvertisement clone() {
		return new AuctionResponseAdvertisement(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuctionResponseAdvertisement)) {
			return false;
		}

		final AuctionResponseAdvertisement other = (AuctionResponseAdvertisement) obj;
		return id.equals(other.id);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, AUCTION_ID_TAG, auctionId.toString());
		appendElement(doc, PEER_ID_TAG, bid.getBidderPeerID().toString());
		appendElement(doc, BID_TAG, String.valueOf(bid.getValue()));
		appendElement(doc, POSITION_X_ID_TAG, String.valueOf(pingMapPosition.getX()));
		appendElement(doc, POSITION_Y_ID_TAG, String.valueOf(pingMapPosition.getY()));
		appendElement(doc, POSITION_Z_ID_TAG, String.valueOf(pingMapPosition.getZ()));

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

	public ID getAuctionId() {
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

	public void setAuctionId(ID auctionId) {
		this.auctionId = auctionId;
	}
	
	public Vector3D getPingMapPosition() {
		return pingMapPosition;
	}
	
	public void setPingMapPosition(Vector3D pingMapPosition) {
		this.pingMapPosition = pingMapPosition;
	}

	private void determineFields(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();

		PeerID bidder = null;
		double bidValue = -1;
		Vector3D pos = new Vector3D(0, 0, 0);
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(AUCTION_ID_TAG)) {
				auctionId = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				bidder = convertToPeerID(elem.getTextValue());
			} else if (elem.getName().equals(BID_TAG)) {
				bidValue = Double.valueOf(elem.getTextValue());
			} else if( elem.getName().equals(POSITION_X_ID_TAG)) {
				Double valueOf = Double.valueOf(elem.getTextValue());
				pos = new Vector3D(valueOf, pos.getY(), pos.getZ());
			} else if( elem.getName().equals(POSITION_Y_ID_TAG)) {
				Double valueOf = Double.valueOf(elem.getTextValue());
				pos = new Vector3D(pos.getX(), valueOf, pos.getZ());
			} else if( elem.getName().equals(POSITION_Z_ID_TAG)) {
				Double valueOf = Double.valueOf(elem.getTextValue());
				pos = new Vector3D(pos.getX(), pos.getY(), valueOf);
			}
		}
		
		pingMapPosition = pos;
		bid = new Bid(bidder, bidValue);
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
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	public Bid getBid() {
		return bid;
	}
	
	public void setBid(Bid bid) {
		this.bid = bid;
	}
}
