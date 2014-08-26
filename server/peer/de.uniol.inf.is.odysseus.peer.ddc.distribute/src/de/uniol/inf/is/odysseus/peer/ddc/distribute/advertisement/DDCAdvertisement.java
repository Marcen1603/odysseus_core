package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
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
	private static final Logger LOG = LoggerFactory
			.getLogger(DDCAdvertisement.class);

	private static final String ADVERTISEMENT_TYPE = "jxta:DDCAdvertisement";

	private static final String ID_TAG = "id";
	private static final String OWNER_PEER_ID_TAG = "ownerPeerId";
	private static final String ADVERTISEMENT_UID = "advertisementUid";
	private static final String TYPE = "type";
	private static final String CREATED_DDC_ENTRIES = "createdDDCEntries";
	private static final String UPDATED_DDC_ENTRIES = "updatedDDCEntries";
	private static final String DELETED_DDC_ENTRIES = "deletedDDCEntries";
	private static final String CREATED_DDC_ENTRY = "createdDDCEntry";
	private static final String UPDATED_DDC_ENTRY = "updatedDDCEntry";
	private static final String DELETED_DDC_ENTRY = "deletedDDCEntry";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG,
			OWNER_PEER_ID_TAG, ADVERTISEMENT_UID };

	private ID id;
	private PeerID ownerPeerId;
	private UUID advertisementUid;
	private DDCAdvertisementType type;
	private List<DDCEntry> createdDDCEntires;
	private List<DDCEntry> updatedDDCEntires;
	private List<String> deletedDDCEntires;

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
		this.advertisementUid = adv.advertisementUid;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
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
		appendElement(doc, ADVERTISEMENT_UID, advertisementUid.toString());
		appendElement(doc, TYPE, type.toString());

		if (type.equals(DDCAdvertisementType.ddcCreated)
				|| type.equals(DDCAdvertisementType.ddcUpdated)) {
			// add created ddc entires
			if (createdDDCEntires != null && !createdDDCEntires.isEmpty()) {
				appendListElement(doc, CREATED_DDC_ENTRIES);
				for (DDCEntry ddcEntry : createdDDCEntires) {
					appendDDCEntry(doc, CREATED_DDC_ENTRY, ddcEntry);
				}
			}
		}
		if (type.equals(DDCAdvertisementType.ddcUpdated)) {
			// add updated and deleted enties
			if (updatedDDCEntires != null && !updatedDDCEntires.isEmpty()) {
				appendListElement(doc, UPDATED_DDC_ENTRIES);
				for (DDCEntry ddcEntry : updatedDDCEntires) {
					appendDDCEntry(doc, UPDATED_DDC_ENTRY, ddcEntry);
				}
			}
			if (deletedDDCEntires != null && !deletedDDCEntires.isEmpty()) {
				appendListElement(doc, DELETED_DDC_ENTRIES);
				for (String ddcEntryKey : deletedDDCEntires) {
					appendDeletedDDCEntry(doc, DELETED_DDC_ENTRY, ddcEntryKey);
				}
			}
		}

		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo,
			String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendDDCEntry(StructuredDocument appendTo,
			String tag, DDCEntry ddcEntry) {
		final Element baseDDCElement = appendTo.createElement(tag);

		final Element keyDDCElement = appendTo.createElement("multiKey",
				ddcEntry.getKey());
		baseDDCElement.appendChild(keyDDCElement);
		final Element valueDDCElement = appendTo.createElement("value",
				ddcEntry.getValue());
		baseDDCElement.appendChild(valueDDCElement);
		final Element tsDDCElement = appendTo.createElement("ts",
				ddcEntry.getTimeStamp());
		baseDDCElement.appendChild(tsDDCElement);

		appendTo.appendChild(baseDDCElement);
		return baseDDCElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendDeletedDDCEntry(StructuredDocument appendTo,
			String tag, String ddcEntryKey) {
		final Element baseDDCElement = appendTo.createElement(tag);

		final Element keyDDCElement = appendTo.createElement("multiKey",
				ddcEntryKey);
		baseDDCElement.appendChild(keyDDCElement);

		appendTo.appendChild(baseDDCElement);
		return baseDDCElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendListElement(StructuredDocument appendTo,
			String tag) {
		final Element createElement = appendTo.createElement(tag);
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
			} else if (elem.getName().equals(ADVERTISEMENT_UID)) {
				setDDCAdvertisementUid(UUID.fromString(elem.getTextValue()));
			} else if (elem.getName().equals(TYPE)) {
				setType(DDCAdvertisementType.parse(elem.getTextValue()));
			} else if (elem.getName().equals(CREATED_DDC_ENTRIES)) {
				Enumeration<?> children = elem.getChildren();
				while (children.hasMoreElements()) {
					final TextElement<?> child = (TextElement<?>) children
							.nextElement();
					if (child.getName().equals(CREATED_DDC_ENTRY)) {
						// TODO Create DDC
					}
				}
			} else if (elem.getName().equals(UPDATED_DDC_ENTRIES)) {
				Enumeration<?> children = elem.getChildren();
				while (children.hasMoreElements()) {
					final TextElement<?> child = (TextElement<?>) children
							.nextElement();
					if (child.getName().equals(UPDATED_DDC_ENTRY)) {
						// TODO Create DDC
					}
				}
			} else if (elem.getName().equals(DELETED_DDC_ENTRIES)) {
				Enumeration<?> children = elem.getChildren();
				while (children.hasMoreElements()) {
					final TextElement<?> child = (TextElement<?>) children
							.nextElement();
					if (child.getName().equals(DELETED_DDC_ENTRY)) {
						// TODO Create String
					}
				}
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

	public void setDDCAdvertisementUid(UUID advertisementUid) {
		this.advertisementUid = advertisementUid;
	}

	public UUID getAdvertisementUid() {
		return advertisementUid;
	}

	public DDCAdvertisementType getType() {
		return type;
	}

	public void setType(DDCAdvertisementType type) {
		this.type = type;
	}

	public List<DDCEntry> getCreatedDDCEntires() {
		return createdDDCEntires;
	}

	public void addCreatedEntry(DDCEntry ddcEntry) {
		if (createdDDCEntires == null)
			createdDDCEntires = new ArrayList<DDCEntry>();
		createdDDCEntires.add(ddcEntry);
	}

	public List<DDCEntry> getUpdatedDDCEntires() {
		return updatedDDCEntires;
	}

	public void addUpdatedEntry(DDCEntry ddcEntry) {
		if (updatedDDCEntires == null)
			updatedDDCEntires = new ArrayList<DDCEntry>();
		updatedDDCEntires.add(ddcEntry);
	}

	public List<String> getDeletedDDCEntires() {
		return deletedDDCEntires;
	}

	public void addUpdatedEntry(String ddcEntryKey) {
		if (deletedDDCEntires == null)
			deletedDDCEntires = new ArrayList<String>();
		deletedDDCEntires.add(ddcEntryKey);
	}

}
