package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
	private static final String ADDED_DDC_ENTRY = "addedDDCEntry";
	private static final String REMOVED_DDC_ENTRY = "removedDDCEntry";
	private static final String TS = "ts";
	private static final String VALUE = "value";
	private static final String MULTI_KEY = "multiKey";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG,
			OWNER_PEER_ID_TAG, ADVERTISEMENT_UID };

	private ID id;
	private PeerID ownerPeerId;
	private UUID advertisementUid;
	private DDCAdvertisementType type;
	private List<DDCEntry> addedDDCEntires;
	private List<String[]> removedDDCEntires;

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

		// add created ddc entires
		if (addedDDCEntires != null && !addedDDCEntires.isEmpty()) {
			for (DDCEntry ddcEntry : addedDDCEntires) {
				appendDDCEntry(doc, ADDED_DDC_ENTRY, ddcEntry);
			}
		}

		if (type.equals(DDCAdvertisementType.changeDistribution)) {
			if (removedDDCEntires != null && !removedDDCEntires.isEmpty()) {
				for (String[] ddcEntryKey : removedDDCEntires) {
					appendDeletedDDCEntry(doc, REMOVED_DDC_ENTRY, ddcEntryKey);
				}
			}
		}

		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo,
			String tag, String value) {
		Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendDDCEntry(StructuredDocument appendTo,
			String tag, DDCEntry ddcEntry) {
		Element baseDDCElement = appendTo.createElement(tag);
		appendTo.appendChild(baseDDCElement);

		Element keyDDCElement = appendTo.createElement(MULTI_KEY,
				StringUtils.join(ddcEntry.getKey(), ","));
		baseDDCElement.appendChild(keyDDCElement);
		Element valueDDCElement = appendTo.createElement(VALUE,
				ddcEntry.getValue());
		baseDDCElement.appendChild(valueDDCElement);
		Element tsDDCElement = appendTo.createElement(TS,
				String.valueOf(ddcEntry.getTimeStamp()));
		baseDDCElement.appendChild(tsDDCElement);

		return baseDDCElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendDeletedDDCEntry(StructuredDocument appendTo,
			String tag, String[] ddcEntryKey) {
		Element baseDDCElement = appendTo.createElement(tag);
		appendTo.appendChild(baseDDCElement);

		Element keyDDCElement = appendTo.createElement(MULTI_KEY,
				StringUtils.join(ddcEntryKey, ","));
		baseDDCElement.appendChild(keyDDCElement);

		return baseDDCElement;
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
			} else if (elem.getName().equals(ADDED_DDC_ENTRY)) {
				Enumeration<?> children = elem.getChildren();
				String[] key = null;
				String value = "";
				long ts = 0;

				while (children.hasMoreElements()) {
					final TextElement<?> child = (TextElement<?>) children
							.nextElement();

					if (child.getName().equals(MULTI_KEY)) {
						key = child.getValue().split(",");
					} else if (child.getName().equals(VALUE)) {
						value = child.getValue();
					} else if (child.getName().equals(TS)) {
						ts = Long.parseLong(child.getValue());
					}
				}
				DDCEntry entry = new DDCEntry(key, value, ts);
				addAddedEntry(entry);
			} else if (elem.getName().equals(REMOVED_DDC_ENTRY)) {
				Enumeration<?> children = elem.getChildren();
				while (children.hasMoreElements()) {
					final TextElement<?> child = (TextElement<?>) children
							.nextElement();
					if (child.getName().equals(MULTI_KEY)) {
						addRemovedEntry(child.getValue().split(","));
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

	public List<DDCEntry> getAddedDDCEntires() {
		return addedDDCEntires;
	}

	public void addAddedEntry(DDCEntry ddcEntry) {
		if (addedDDCEntires == null)
			addedDDCEntires = new ArrayList<DDCEntry>();
		addedDDCEntires.add(ddcEntry);
	}

	public List<String[]> getRemovedDDCEntires() {
		return removedDDCEntires;
	}

	public void addRemovedEntry(String[] ddcEntryKey) {
		if (removedDDCEntires == null)
			removedDDCEntires = new ArrayList<String[]>();
		removedDDCEntires.add(ddcEntryKey);
	}

}
