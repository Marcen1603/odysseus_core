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

public class ResourceUsageUpdateAdvertisement extends Advertisement {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceUsageUpdateAdvertisement.class);
	private static final String ADVERTISEMENT_TYPE = "jxta:ResourceUsageUpdateAdvertisement";
	private static final String ID_TAG = "id";
	private static final String CPU_USAGE_TAG = "cpu_usage";
	private static final String NET_USAGE_TAG = "net_usage";
	private static final String MEM_FREE_TAG = "mem_free";
	private static final String MEM_USED_TAG = "mem_used";
	private static final String MEM_TOTAL_TAG = "mem_total";
	private static final String TIMESTAMP_TAG = "timestamp";
	private static final String MASTER_ID_TAG = "master_id";
	private static final String PEER_ID_TAG = "peer_id";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, PEER_ID_TAG, MASTER_ID_TAG };
	
	ID id;
	double cpu_usage;
	double mem_free;
	double mem_used;
	double mem_total;
	long timestamp;
	PeerID peerID;
	PeerID masterID;
	double networkUsage;
	
	public ResourceUsageUpdateAdvertisement() {
		super();
	}
	
	public ResourceUsageUpdateAdvertisement(Element<?> root) {
		if(root != null) {
			root = (TextElement<?>) root;
		} else {
			LOG.debug("can't instantiate from null");
		}
		final Enumeration<?> elements = root.getChildren();

		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			if (elem.getName().equals(ID_TAG)) {
				this.id = convertToID(elem.getTextValue());
			} else if (elem.getName().equals(PEER_ID_TAG)) {
				this.peerID = (PeerID)convertToID(elem.getTextValue());
			} else if (elem.getName().equals(MASTER_ID_TAG)) {
				this.setMasterID((PeerID)convertToID(elem.getTextValue()));
			} else if (elem.getName().equals(CPU_USAGE_TAG)) {
				this.setCpu_usage(Double.parseDouble(elem.getTextValue()));
			} else if (elem.getName().equals(NET_USAGE_TAG)) {
				this.setNetworkUsage(Double.parseDouble(elem.getTextValue()));
			} else if (elem.getName().equals(MEM_FREE_TAG)) {
				this.setMem_free(Double.parseDouble(elem.getTextValue()));
			} else if (elem.getName().equals(MEM_USED_TAG)) {
				this.setMem_used(Double.parseDouble(elem.getTextValue()));
			} else if (elem.getName().equals(MEM_TOTAL_TAG)) {
				this.setMem_total(Double.parseDouble(elem.getTextValue()));
			} else if (elem.getName().equals(TIMESTAMP_TAG)) {
				this.setTimestamp(Long.parseLong(elem.getTextValue()));
			}
		}
	}

	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument doc =  StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		doc.appendChild(doc.createElement(ID_TAG, id.toString()));
		doc.appendChild(doc.createElement(CPU_USAGE_TAG, cpu_usage));
		doc.appendChild(doc.createElement(NET_USAGE_TAG, networkUsage));
		doc.appendChild(doc.createElement(MEM_FREE_TAG, mem_free));
		doc.appendChild(doc.createElement(MEM_USED_TAG, mem_used));
		doc.appendChild(doc.createElement(MEM_TOTAL_TAG, mem_total));
		doc.appendChild(doc.createElement(TIMESTAMP_TAG, timestamp));
		doc.appendChild(doc.createElement(PEER_ID_TAG, peerID.toString()));
		doc.appendChild(doc.createElement(MASTER_ID_TAG, masterID.toString()));
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

	public double getCpu_usage() {
		return cpu_usage;
	}

	public void setCpu_usage(double cpu_usage) {
		this.cpu_usage = cpu_usage;
	}

	public double getMem_free() {
		return mem_free;
	}

	public void setMem_free(double mem_free) {
		this.mem_free = mem_free;
	}

	public double getMem_used() {
		return mem_used;
	}

	public void setMem_used(double mem_used) {
		this.mem_used = mem_used;
	}

	public double getMem_total() {
		return mem_total;
	}

	public void setMem_total(double mem_total) {
		this.mem_total = mem_total;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	public PeerID getMasterID() {
		return masterID;
	}

	public void setMasterID(PeerID masterID) {
		this.masterID = masterID;
	}
	
	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
	
	private ID convertToID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	public void setID(ID id) {
		this.id = id;
	}

	public void setNetworkUsage(double networkUsage) {
		this.networkUsage = networkUsage;		
	}
	
	public double getNetworkUsage() {
		return this.networkUsage;
	}
}
