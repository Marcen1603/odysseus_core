package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.UUID;

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

/**
 * The JXTA Advertisement for DistributedDataContainer to tell other peers, that
 * a DDC is available.
 * 
 * @author ChrisToenjesDeye, Michael Brand
 * 
 */
public class DistributedDataContainerAdvertisement extends Advertisement {
	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisement.class);

	// Tag constants for document
	private static final String ADVERTISEMENT_TYPE = "jxta:DDCAdvertisement";
	private static final String ID_TAG = "id";
	private static final String INITIATING_PEER_ID_TAG = "initiatingPeerId";
	private static final String ADVERTISEMENT_UID = "advertisementUid";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG,
			INITIATING_PEER_ID_TAG, ADVERTISEMENT_UID };

	// advertisement attributes
	private ID id;
	private PeerID initiatingPeerId;
	private UUID advertisementUid;

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	/**
	 * Default Constructor for @DDCAdvertisementInstantiator
	 */
	public DistributedDataContainerAdvertisement() {

	}

	/**
	 * JXTA Constructor, initialized with advertisement document
	 * 
	 * @param root
	 *            - the xml-Root-Element
	 */
	public DistributedDataContainerAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(
				root, "Root element must not be null!");
		determineFields(doc);
	}

	public DistributedDataContainerAdvertisement(InputStream stream)
			throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(
				MimeMediaType.XMLUTF8,
				Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	/**
	 * Copy Constructor
	 * 
	 * @param adv
	 *            - The @DDCAdvertisement to copy from
	 */
	public DistributedDataContainerAdvertisement(
			DistributedDataContainerAdvertisement adv) {
		Preconditions.checkNotNull(adv,
				"Advertisement to copy must not be null!");
		this.id = adv.id;
		this.initiatingPeerId = adv.initiatingPeerId;
		this.advertisementUid = adv.advertisementUid;
	}

	/**
	 * Creates the content document for the advertisement based on existing
	 * values
	 * 
	 * @param asMimeType
	 * @return Document with advertisement content
	 */
	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory
				.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, INITIATING_PEER_ID_TAG, initiatingPeerId.toString());
		appendElement(doc, ADVERTISEMENT_UID, advertisementUid.toString());
		return doc;
	}

	/**
	 * Appends an element to given @StructuredDocuement
	 * 
	 * @param appendTo
	 *            - @StructuredDocument to append
	 * @param tag
	 *            - Name of the tag
	 * @param value
	 *            - value of the tag
	 * @return created element
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo,
			String tag, String value) {
		Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	/**
	 * Getting values from @StructuredDocument and set them to @DDCAdvertisement
	 * 
	 * @param root
	 *            - the xml-Root-Element
	 */
	private void determineFields(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				setID(convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(INITIATING_PEER_ID_TAG)) {
				setInitiatingPeerId(convertToPeerID(elem.getTextValue()));
			} else if (elem.getName().equals(ADVERTISEMENT_UID)) {
				setDDCAdvertisementUid(UUID.fromString(elem.getTextValue()));
			}
		}
	}

	/**
	 * Converts a given @String to @ID
	 * 
	 * @param elem
	 *            - String containing a Id
	 * @return the ID if exists
	 */
	private static ID convertToID(String elem) {
		try {
			final URI id = new URI(elem);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	/**
	 * Converts a given @String to @PeerID
	 * 
	 * @param text
	 *            - String containing a peerId
	 * @return the peerID if exists
	 */
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

	public void setInitiatingPeerId(PeerID ownerPeerId) {
		this.initiatingPeerId = ownerPeerId;
	}

	public PeerID getInitiatingPeerId() {
		return initiatingPeerId;
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
		if (!(obj instanceof DistributedDataContainerAdvertisement)) {
			return false;
		}

		final DistributedDataContainerAdvertisement other = (DistributedDataContainerAdvertisement) obj;
		return id.equals(other.id);
	}

	public void setDDCAdvertisementUid(UUID advertisementUid) {
		this.advertisementUid = advertisementUid;
	}

	public UUID getAdvertisementUid() {
		return advertisementUid;
	}

}