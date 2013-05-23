package de.uniol.inf.is.odysseus.p2p_new.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
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
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;

public class ViewAdvertisement extends Advertisement implements Serializable {

	public static class Same {
		private final PeerID peerID;
		private final String name;
		
		public Same( PeerID peerID, String viewName ) {
			this.peerID = peerID;
			this.name = viewName;
		}
		
		public PeerID getPeerID() {
			return peerID;
		}
		
		public String getViewName() {
			return name;
		}
	}
	
	private static final String ADVERTISEMENT_TYPE = "jxta:ViewAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ViewAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PEER_ID_TAG = "originalPeerID";
	
	// external sources
	private static final String ACCESS_AO_TAG = "accessAO";
	
	// internal sources
	private static final String PIPEID_TAG = "pipeid";
	private static final String OUTPUTSCHEMA_TAG = "outputSchema";
	private static final String SAME_AS_TAG = "sameAs";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, NAME_TAG, PEER_ID_TAG };

	private List<Same> sameAs;
	
	private ID id;
	private String name;
	private PipeID pipeID;
	private PeerID peerID;
	private SDFSchema outputSchema;
	
	private AccessAO accessAO;

	public ViewAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public ViewAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public ViewAdvertisement(ViewAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement, "Advertisement to copy must not be null!");

		id = viewAdvertisement.id;
		name = viewAdvertisement.name;
		pipeID = viewAdvertisement.pipeID;
		peerID = viewAdvertisement.peerID;
		outputSchema = viewAdvertisement.outputSchema;
		accessAO = viewAdvertisement.accessAO;
	}

	ViewAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public ViewAdvertisement clone() throws CloneNotSupportedException {
		return new ViewAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, NAME_TAG, name);
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		
		if( accessAO != null ) {
			// external source
			final Element<?> element = appendElement(doc, ACCESS_AO_TAG);
			AccessAOCoverter.toDocument(element, accessAO);
			
		} else {
		
			// internal source
			appendElement(doc, PIPEID_TAG, pipeID.toString());
	
			final Element<?> outSchemaElement = appendElement(doc, OUTPUTSCHEMA_TAG, outputSchema.getURI());
			for (final SDFAttribute attr : outputSchema) {
				appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
			}
			
			if( sameAs != null && !sameAs.isEmpty() ) {
				final Element<?> sameElement = appendElement(doc, SAME_AS_TAG);
				for( Same same : sameAs ) {
					appendElement(sameElement, same.getPeerID().toString(), same.getViewName());
				}
			}
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

	public void setOutputSchema(SDFSchema outputSchema) {
		this.outputSchema = outputSchema;
	}

	public SDFSchema getOutputSchema() {
		return outputSchema;
	}

	public void setPipeID(PipeID pipeID) {
		this.pipeID = pipeID;
	}

	public PipeID getPipeID() {
		return pipeID;
	}
	
	public boolean isLocal() {
		return this.peerID.equals(P2PNewPlugIn.getOwnPeerID());
	}
	
	public boolean isStream() {
		return accessAO != null;
	}
	
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public PeerID getPeerID() {
		return peerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSameAs(List<Same> sameAs) {
		this.sameAs = sameAs;
	}
	
	public List<Same> getSameAs() {
		return sameAs;
	}
	
	public AccessAO getAccessAO() {
		return accessAO;
	}
	
	public void setAccessAO(AccessAO accessAO) {
		this.accessAO = accessAO;
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
		if (!(obj instanceof ViewAdvertisement)) {
			return false;
		}
		ViewAdvertisement adv = (ViewAdvertisement) obj;
		return
				Objects.equals(adv.id, id) &&
				Objects.equals(adv.name, name) && 
				Objects.equals(adv.pipeID, pipeID);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, id, pipeID);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));

		} else if (elem.getName().equals(NAME_TAG)) {
			setName(elem.getTextValue());
			
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));

		} else if( elem.getName().equals(ACCESS_AO_TAG)) {
			accessAO = AccessAOCoverter.toAccessAO(elem);
			
		} else if (elem.getName().equals(PIPEID_TAG)) {
			setPipeID((PipeID) toID(elem));

		} else if (elem.getName().equals(OUTPUTSCHEMA_TAG)) {
			setOutputSchema(handleOutputSchemaTag(elem, getName()));

		} else if (elem.getName().equals(SAME_AS_TAG)) {
			setSameAs(handleSameAsTag(elem, getName()));

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
	private static Element appendElement(Element appendTo, String tag, String value) {
		final Element ele = appendTo.getRoot().createElement(tag, value);
		appendTo.appendChild(ele);
		return ele;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag) {
		final Element ele = appendTo.getRoot().createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

	private static SDFSchema handleOutputSchemaTag(TextElement<?> root, String viewName) {
		final Enumeration<?> children = root.getChildren();
		final List<SDFAttribute> attributes = Lists.newArrayList();
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			final SDFAttribute attr = new SDFAttribute(viewName, elem.getKey(), DataDictionaryService.get().getDatatype(elem.getTextValue()));
			attributes.add(attr);
		}

		return new SDFSchema(root.getTextValue(), attributes);
	}
	
	private static List<Same> handleSameAsTag(TextElement<?> root, String viewName) {
		final Enumeration<?> children = root.getChildren();
		final List<Same> sameAsList = Lists.newArrayList();
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			final Same s = new Same((PeerID)toID(elem.getKey()), elem.getTextValue());
			sameAsList.add(s);
		}

		return sameAsList;
	}
}
