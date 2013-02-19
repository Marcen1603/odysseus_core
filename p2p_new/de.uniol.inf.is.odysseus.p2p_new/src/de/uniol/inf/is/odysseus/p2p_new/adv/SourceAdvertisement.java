package de.uniol.inf.is.odysseus.p2p_new.adv;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public final class SourceAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:SourceAdvertisement";
	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisement.class);
	private static final long serialVersionUID = 1L;

	private static final String SOURCE_NAME_TAG = "sourceName";
	private static final String ID_TAG = "id";
	private static final String[] INDEXABLE_FIELD_TAGS = { ID_TAG, SOURCE_NAME_TAG };

	private String sourceName;
	private ID id;

	public SourceAdvertisement(Element<?> root) {
		TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");
		initialize(doc);
	}

	public SourceAdvertisement(InputStream stream) throws IOException {
		Preconditions.checkNotNull(stream, "Stream must not be null!");

		StructuredTextDocument<?> doc = (StructuredTextDocument<?>) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, stream);
		initialize(doc);
	}

	public SourceAdvertisement(SourceAdvertisement sourceAdvertisement) {
		Preconditions.checkNotNull(sourceAdvertisement, "Advertisement to copy must not be null!");

		sourceName = sourceAdvertisement.sourceName;
		id = sourceAdvertisement.id;
	}

	SourceAdvertisement() {
		// for JXTA-side instances
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setID(ID id) {
		this.id = id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEXABLE_FIELD_TAGS;
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument<?> adv = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (adv instanceof Attributable) {
			((Attributable) adv).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(adv, ID_TAG, getID().toString());
		appendElement(adv, SOURCE_NAME_TAG, getSourceName().trim());

		return adv;
	}

	/**
	 * Instantiator
	 */
	public static class Instantiator implements AdvertisementFactory.Instantiator {
		/**
		 * Returns the identifying type of this Advertisement.
		 * 
		 * @return String the type of advertisement
		 */
		@Override
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
		@Override
		public Advertisement newInstance() {
			return new SourceAdvertisement();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Advertisement newInstance(Element root) {
			return new SourceAdvertisement(root);
		}
	}

	@Override
	public SourceAdvertisement clone() {
		return new SourceAdvertisement(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SourceAdvertisement)) {
			return false;
		}

		SourceAdvertisement other = (SourceAdvertisement) obj;
		if (sourceName == null) {
			if (other.sourceName != null) {
				return false;
			}
		} else if (!sourceName.equals(other.sourceName)) {
			return false;
		}
		return true;
	}

	private void initialize(TextElement<?> root) {
		checkType(root);

		Enumeration<?> elements = root.getChildren();
		while (elements.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) elements.nextElement();

			handleElement(elem);
		}
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(SOURCE_NAME_TAG)) {
			setSourceName(elem.getTextValue());
		} else if (elem.getName().equals(ID_TAG)) {
			try {
				URI id = new URI(elem.getTextValue());
				setID(IDFactory.fromURI(id));
			} catch (URISyntaxException badID) {
				throw new IllegalArgumentException("unknown ID format in advertisement: " + elem.getTextValue());
			} catch (ClassCastException badID) {
				throw new IllegalArgumentException("Id is not a known id type: " + elem.getTextValue());
			}
		} else {
			LOG.warn("Unknown element name: {}", elem.getName());
		}
	}

	private static void checkType(TextElement<?> root) {
		if (!root.getName().equals(getAdvertisementType())) {
			throw new IllegalArgumentException("Could not construct " + getAdvertisementType() + " from doc containing a " + root.getName());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void appendElement(StructuredDocument appendTo, String tag, String value) {
		appendTo.appendChild(appendTo.createElement(tag, value));
	}

}
