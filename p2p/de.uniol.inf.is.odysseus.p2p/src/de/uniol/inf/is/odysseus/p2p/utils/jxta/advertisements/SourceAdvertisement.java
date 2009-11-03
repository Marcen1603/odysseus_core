package de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

/**
 * Beschreibt eine Quelle im P2P-Netzwerk
 * @author christian
 *
 */
public class SourceAdvertisement extends Advertisement implements
Serializable, Cloneable, Comparable {

	private static final long serialVersionUID = 1L;

	private String sourceName;
	
	private String sourceScheme;

	public String getSourceScheme() {
		return sourceScheme;
	}

	public void setSourceScheme(String sourceScheme) {
		this.sourceScheme = sourceScheme;
	}

	private ID id = ID.nullID;
	
	private String peer;
	
	private String sourceId;
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceSocket() {
		return sourceSocket;
	}

	public void setSourceSocket(String sourceSocket) {
		this.sourceSocket = sourceSocket;
	}

	private String sourceSocket;


	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}

	private final static String sourceNameTag = "sourceName";
	private final static String idTag = "id";
	private final static String sourceSocketTag = "sourceSocket";
	private final static String peerTag = "peer";
	private final static String sourceIdTag = "sourceId";
	private final static String sourceSchemeTag = "sourceScheme";

	/**
	 * Indexable fields. Advertisements must define the indexables, in order to
	 * properly index and retrieve these advertisements locally and on the
	 * network
	 */
	private final static String[] fields = { idTag, sourceIdTag, sourceNameTag, sourceSchemeTag};

	public SourceAdvertisement(Element root) {
		TextElement doc = (TextElement) root;

		if (!getAdvertisementType().equals(doc.getName())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		initialize(doc);
	}

	public SourceAdvertisement(InputStream stream) throws IOException {
		StructuredTextDocument doc = (StructuredTextDocument) StructuredDocumentFactory
				.newStructuredDocument(MimeMediaType.XMLUTF8, stream);
		initialize(doc);
	}

	protected void initialize(Element root) {
		if (!TextElement.class.isInstance(root)) {
			throw new IllegalArgumentException(getClass().getName()
					+ " only supports TextElement");
		}
		TextElement doc = (TextElement) root;

		if (!doc.getName().equals(getAdvertisementType())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		Enumeration elements = doc.getChildren();

		while (elements.hasMoreElements()) {
			TextElement elem = (TextElement) elements.nextElement();

			if (!handleElement(elem)) {
				// LOG.warning("Unhandleded element \'" + elem.getName() +
				// "\' in " + doc.getName());
			}
		}
	}

	protected boolean handleElement(TextElement elem) {
		if (elem.getName().equals(idTag)) {
			try {
				URI id = new URI(elem.getTextValue());

				setID(IDFactory.fromURI(id));
			} catch (URISyntaxException badID) {
				throw new IllegalArgumentException(
						"unknown ID format in advertisement: "
								+ elem.getTextValue());
			} catch (ClassCastException badID) {
				throw new IllegalArgumentException(
						"Id is not a known id type: " + elem.getTextValue());
			}
			return true;
		}
		if (elem.getName().equals(sourceNameTag)) {
			setSourceName(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(sourceSocketTag)) {
			setSourceSocket(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(peerTag)) {
			setPeer(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(sourceIdTag)) {
			setSourceId(elem.getTextValue());
			return true;
		}
		if(elem.getName().equals(sourceSchemeTag)) {
			setSourceScheme(elem.getTextValue());
			return true;
		}
		

		return false;
	}

	public SourceAdvertisement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument adv = StructuredDocumentFactory
				.newStructuredDocument(asMimeType, getAdvertisementType());
		if (adv instanceof Attributable) {
			((Attributable) adv).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		Element e;
		e = adv.createElement(idTag, getID().toString());
		adv.appendChild(e);
		e = adv.createElement(sourceNameTag, getSourceName().trim());		
		adv.appendChild(e);
		e = adv.createElement(sourceSocketTag, getSourceSocket().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(peerTag, getPeer().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(sourceIdTag, getSourceId().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(sourceSchemeTag, getSourceScheme().toString().trim());
		adv.appendChild(e);
		return adv;
	}

	public static String getAdvertisementType() {
		return "jxta:SourceAdvertisement";
	}

	@Override
	public ID getID() {
		return (id == null ? null : id);
	}

	@Override
	public String[] getIndexFields() {
		return fields;
	}

	@Override
	public int compareTo(Object other) {
		return getID().toString().compareTo(other.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof SourceAdvertisement) {
			SourceAdvertisement adv = (SourceAdvertisement) obj;
			return getID().equals(adv.getID());
		}
		return false;
	}

	public void setID(ID id) {
		this.id = (id == null ? null : id);
	}

	

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}



	/**
	 * Instantiator
	 */
	public static class Instantiator implements
			AdvertisementFactory.Instantiator {
		/**
		 * Returns the identifying type of this Advertisement.
		 * 
		 * @return String the type of advertisement
		 */
		public String getAdvertisementType() {
			return SourceAdvertisement.getAdvertisementType();
		}

		/**
		 * Constructs an instance of <CODE>Advertisement</CODE> matching the
		 * type specified by the <CODE>advertisementType</CODE> parameter.
		 * 
		 * @return The instance of <CODE>Advertisement</CODE> or null if it
		 *         could not be created.
		 */
		public Advertisement newInstance() {
			return new SourceAdvertisement();
		}

		@Override
		public Advertisement newInstance(Element root) {
			return new SourceAdvertisement(root);
		}
	}




	
}
