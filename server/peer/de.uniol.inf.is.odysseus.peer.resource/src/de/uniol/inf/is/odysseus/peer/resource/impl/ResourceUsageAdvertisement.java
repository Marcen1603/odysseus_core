package de.uniol.inf.is.odysseus.peer.resource.impl;

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
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public final class ResourceUsageAdvertisement extends Advertisement implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageAdvertisement.class);
	private static final String ADVERTISEMENT_TYPE = "jxta:ResourceUsageAdvertisement";
	private static final long serialVersionUID = 1L;

	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerid";
	private static final String MEM_FREE_TAG = "memfree";
	private static final String MEM_MAX_TAG = "memmax";
	private static final String CPU_FREE_TAG = "cpufree";
	private static final String CPU_MAX_TAG = "cpumax";
	private static final String TIMESTAMP_TAG = "timestamp";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG, TIMESTAMP_TAG };

	private ID id;
	private IResourceUsage usage; 
	
	public ResourceUsageAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		determineFields(doc);
	}

	public ResourceUsageAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public ResourceUsageAdvertisement(ResourceUsageAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		usage = adv.usage != null ? adv.usage.clone() : null;
		id = adv.id;
	}

	ResourceUsageAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public ResourceUsageAdvertisement clone() {
		return new ResourceUsageAdvertisement(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ResourceUsageAdvertisement)) {
			return false;
		}

		final ResourceUsageAdvertisement other = (ResourceUsageAdvertisement) obj;
		return usage.equals(other.usage);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		Preconditions.checkNotNull(usage, "Resource usage is null!");
		
		StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PEER_ID_TAG, usage.getPeerID().toString());
		appendElement(doc, MEM_FREE_TAG, String.valueOf(usage.getMemFreeBytes()));
		appendElement(doc, MEM_MAX_TAG, String.valueOf(usage.getMemMaxBytes()));
		appendElement(doc, CPU_FREE_TAG, String.valueOf(usage.getCpuFree()));
		appendElement(doc, CPU_MAX_TAG, String.valueOf(usage.getCpuMax()));
		appendElement(doc, TIMESTAMP_TAG, String.valueOf(usage.getTimestamp()));
		
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

	public IResourceUsage getResourceUsage() {
		return usage;
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
	
	public void setResourceUsage( IResourceUsage usage ) {
		Preconditions.checkNotNull(usage, "Resource usage for advertisement must not be null!");
		
		this.usage = usage;
	}

	private void determineFields(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();

		PeerID peerID = null;
		long memFreeBytes = 0;
		long memMaxBytes = 0;
		double cpuFree = 0;
		double cpuMax = 0;
		long timestamp = 0;
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				peerID = convertToPeerID(elem.getTextValue());
			} else if (elem.getName().equals(MEM_FREE_TAG)) {
				memFreeBytes = Long.valueOf(elem.getTextValue());
			} else if (elem.getName().equals(MEM_MAX_TAG)) {
				memMaxBytes = Long.valueOf(elem.getTextValue());
			} else if (elem.getName().equals(CPU_FREE_TAG)) {
				cpuFree = Double.valueOf(elem.getTextValue());
			} else if (elem.getName().equals(CPU_MAX_TAG)) {
				cpuMax = Double.valueOf(elem.getTextValue());
			} else if (elem.getName().equals(TIMESTAMP_TAG)) {
				timestamp = Long.valueOf(elem.getTextValue());
			} 
		}
		
		usage = new ResourceUsage(peerID, memFreeBytes, memMaxBytes, cpuFree, cpuMax, timestamp);
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
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

	private static ID convertToID(String text) {
		try {
			final URI id = new URI(text);
			return ID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}
}
