package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Enumeration;
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
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.AdvertisementCache;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;

public class MultipleSourceAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:MultipleSourceAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MultipleSourceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String SOURCES_TAG = "sources";
	private static final String PEER_ID_TAG = "peerid";
	private static final String SOURCE_TAG = "source";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG };

	private ID id;
	private PeerID peerID;
	private Collection<SourceAdvertisement> sourceAdvertisements = Lists.newArrayList();
	
	public MultipleSourceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public MultipleSourceAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public MultipleSourceAdvertisement(MultipleSourceAdvertisement advertisement) {
		Preconditions.checkNotNull(advertisement, "Advertisement to copy must not be null!");

		id = advertisement.id;
	}

	public MultipleSourceAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public MultipleSourceAdvertisement clone() throws CloneNotSupportedException {
		return new MultipleSourceAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		
		int hashCode = hashCode();
		Optional<Document> optDoc = AdvertisementCache.getDocument(hashCode);
		if( optDoc.isPresent() ) {
			return optDoc.get();
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		
		Element<?> sourcesElement = appendElement(doc, SOURCES_TAG);
		for( SourceAdvertisement sourceAdvertisement : sourceAdvertisements ) {
			Element<?> sourceElement = appendElement(sourcesElement,SOURCE_TAG);
			sourceAdvertisement.appendTo(sourceElement);
		}
		
		AdvertisementCache.add(hashCode, doc);
		
		return doc;
	}

	@Override
	public ID getID() {
		return id;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public PeerID getPeerID() {
		return peerID;
	}
	
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public boolean isLocal() {
		return this.peerID.equals(P2PNetworkManagerService.getInstance().getLocalPeerID());
	}
	
	public Collection<SourceAdvertisement> getSourceAdvertisements() {
		return sourceAdvertisements;
	}
	
	public void setSourceAdvertisements(Collection<SourceAdvertisement> sourceAdvertisements) {
		this.sourceAdvertisements = sourceAdvertisements;
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
		if (!(obj instanceof MultipleSourceAdvertisement)) {
			return false;
		}
		MultipleSourceAdvertisement adv = (MultipleSourceAdvertisement) obj;
		return Objects.equals(adv.id, id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));

		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID)toID(elem));

		} else if (elem.getName().equals(SOURCES_TAG)) {
			Enumeration<?> sourceEnumeration = elem.getChildren();
			while( sourceEnumeration.hasMoreElements() ) {
				TextElement<?> source = (TextElement<?>)sourceEnumeration.nextElement();
				SourceAdvertisement sourceAdv = new SourceAdvertisement(source);
				sourceAdvertisements.add(sourceAdv);
			}
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
}
