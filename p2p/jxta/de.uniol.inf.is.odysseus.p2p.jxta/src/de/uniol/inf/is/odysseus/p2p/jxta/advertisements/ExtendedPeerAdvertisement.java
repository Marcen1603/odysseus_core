package de.uniol.inf.is.odysseus.p2p.jxta.advertisements;

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
@SuppressWarnings("unchecked")
public class ExtendedPeerAdvertisement extends Advertisement implements
		Serializable, Cloneable, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ID id = ID.nullID;

	private String peerName;

	private String peerId;

	private String pipe;

	private String type;

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getPipe() {
		return pipe;
	}

	public void setPipe(String pipe) {
		this.pipe = pipe;
	}

	private final static String idTag = "id";
	private final static String pipeTag = "pipe";
	private final static String peerNameTag = "peerName";
	private final static String typeTag = "type";
	private final static String peerIdTag = "peerId";

	/**
	 * Indexable fields. Advertisements must define the indexables, in order to
	 * properly index and retrieve these advertisements locally and on the
	 * network
	 */
	private final static String[] fields = { idTag, typeTag, peerNameTag,
			peerIdTag };

	public ExtendedPeerAdvertisement(Element root) {
		TextElement doc = (TextElement) root;

		if (!getAdvertisementType().equals(doc.getName())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		initialize(doc);
	}

	public ExtendedPeerAdvertisement(InputStream stream) throws IOException {
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

		if (elem.getName().equals(pipeTag)) {
			setPipe(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(peerNameTag)) {
			setPeerName(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(typeTag)) {
			setType(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(peerIdTag)) {
			setPeerId(elem.getTextValue());
			return true;
		}

		return false;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ExtendedPeerAdvertisement() {
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
		e = adv.createElement(pipeTag, getPipe().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(peerNameTag, getPeerName().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(typeTag, getType().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(peerIdTag, getPeerId().toString().trim());
		adv.appendChild(e);
		return adv;
	}

	public static String getAdvertisementType() {
		return "jxta:AdministrationPeerAdvertisement";
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
		if (obj instanceof ExtendedPeerAdvertisement) {
			ExtendedPeerAdvertisement adv = (ExtendedPeerAdvertisement) obj;
			return getID().equals(adv.getID());
		}
		return false;
	}

	public void setID(ID id) {
		this.id = (id == null ? null : id);
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
		@Override
		public String getAdvertisementType() {
			return ExtendedPeerAdvertisement.getAdvertisementType();
		}

		/**
		 * Constructs an instance of <CODE>Advertisement</CODE> matching the
		 * type specified by the <CODE>advertisementType</CODE> parameter.
		 * 
		 * @return The instance of <CODE>Advertisement</CODE> or null if it
		 *         could not be created.
		 */
		@Override
		public Advertisement newInstance() {
			return new ExtendedPeerAdvertisement();
		}

		@Override
		public Advertisement newInstance(Element root) {
			return new ExtendedPeerAdvertisement(root);
		}
	}

}
