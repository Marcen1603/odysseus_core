package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class RemoveMultipleSourceAdvertisement extends Advertisement implements Serializable {
	
	private static final String ADVERTISEMENT_TYPE = "jxta:RemoveMultipleSourceAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RemoveMultipleSourceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String SOURCES_TAG = "sources";
	private static final String SOURCE_ADV_ID_TAG = "sourceAdvertisementID";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG };
	
	private ID id;
	private Collection<ID> sourceAdvertisementIDs = Lists.newArrayList();
	
	public RemoveMultipleSourceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public RemoveMultipleSourceAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public RemoveMultipleSourceAdvertisement(RemoveMultipleSourceAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Advertisement to copy must not be null!");

		id = viewAdvertisement.id;
		sourceAdvertisementIDs = viewAdvertisement.sourceAdvertisementIDs;
	}

	public RemoveMultipleSourceAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public RemoveMultipleSourceAdvertisement clone() throws CloneNotSupportedException {
		return new RemoveMultipleSourceAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		Element<?> sourcesElement = appendElement(doc, SOURCES_TAG);
		for( ID id : sourceAdvertisementIDs ) {
			appendElement(sourcesElement, SOURCE_ADV_ID_TAG, id.toString());
		}
		
		return doc;
	}

	@Override
	public ID getID() {
		return id;
	}

	public void setID(ID id) {
		this.id = id;
	}
	
	public Collection<ID> getSourceAdvertisementIDs() {
		return sourceAdvertisementIDs;
	}
	
	public void setSourceAdvertisementIDs(Collection<ID> sourceAdvertisementIDs) {
		this.sourceAdvertisementIDs = sourceAdvertisementIDs;
	}
	
	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RemoveMultipleSourceAdvertisement)) {
			return false;
		}
		RemoveMultipleSourceAdvertisement adv = (RemoveMultipleSourceAdvertisement) obj;
		return
				Objects.equals(adv.id, id) &&
				Objects.equals(adv.sourceAdvertisementIDs, sourceAdvertisementIDs); 
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, sourceAdvertisementIDs);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));
		} else if (elem.getName().equals(SOURCES_TAG)) {
			List<ID> ids = Lists.newArrayList();
			
			Enumeration<?> sourceEnumeration = elem.getChildren();
			while( sourceEnumeration.hasMoreElements() ) {
				TextElement<?> source = (TextElement<?>)sourceEnumeration.nextElement();
				ids.add(toID(source.getTextValue()));
			}
			
			setSourceAdvertisementIDs(ids);
		} 
	}
	
	private static ID toID(TextElement<?> elem) {
		return toID(elem.getTextValue());
	}
	
	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		final Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag) {
		final Element ele = appendTo.getRoot().createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Element appendElement(Element sourcesElement, String tag, String string) {
		final Element ele = sourcesElement.getRoot().createElement(tag, string);
		sourcesElement.appendChild(ele);
		return ele;		
	}
}
