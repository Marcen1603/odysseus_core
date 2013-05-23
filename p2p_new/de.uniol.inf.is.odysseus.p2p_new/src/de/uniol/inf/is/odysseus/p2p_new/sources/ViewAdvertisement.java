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
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;

public class ViewAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:ViewAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ViewAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String VIEW_NAME_TAG = "viewName";
	private static final String VIEW_ID_TAG = "viewID";
	private static final String PIPEID_TAG = "pipeid";
	private static final String OUTPUTSCHEMA_TAG = "outputSchema";
	private static final String PEER_ID_TAG = "originalPeerID";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, VIEW_NAME_TAG, PIPEID_TAG, PEER_ID_TAG };

	private ID id;
	private String viewName;
	private ID viewID;
	private PipeID pipeID;
	private PeerID peerID;
	private SDFSchema outputSchema;

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
		appendElement(doc, VIEW_NAME_TAG, viewName);
		appendElement(doc, VIEW_ID_TAG, viewID.toString());
		appendElement(doc, PIPEID_TAG, pipeID.toString());
		appendElement(doc, PEER_ID_TAG, peerID.toString());

		final Element<?> outSchemaElement = appendElement(doc, OUTPUTSCHEMA_TAG, outputSchema.getURI());
		for (final SDFAttribute attr : outputSchema) {
			appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
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
	
	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}
	
	public PeerID getPeerID() {
		return peerID;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	public void setViewID(ID viewID) {
		this.viewID = viewID;
	}
	
	public ID getViewID() {
		return viewID;
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
				Objects.equals(adv.viewID, viewID) &&
				Objects.equals(adv.viewName, viewName) && 
				Objects.equals(adv.pipeID, pipeID);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(viewName, id, pipeID);
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));

		} else if (elem.getName().equals(VIEW_NAME_TAG)) {
			setViewName(elem.getTextValue());

		} else if (elem.getName().equals(PIPEID_TAG)) {
			setPipeID((PipeID) toID(elem));

		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));

		} else if (elem.getName().equals(VIEW_ID_TAG)) {
			setViewID(toID(elem));

		} else if (elem.getName().equals(OUTPUTSCHEMA_TAG)) {
			setOutputSchema(handleOutputSchemaTag(elem, getViewName()));

		}
	}

	private static ID toID(TextElement<?> elem) {
		try {
			final URI id = new URI(elem.getTextValue());
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
}
