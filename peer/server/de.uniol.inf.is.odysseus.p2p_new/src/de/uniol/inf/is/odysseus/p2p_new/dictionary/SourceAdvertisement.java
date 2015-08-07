package de.uniol.inf.is.odysseus.p2p_new.dictionary;

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
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.p2p_new.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class SourceAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:SourceAdvertisement";

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SourceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PEER_ID_TAG = "originalPeerID";
	
	// external streams
	private static final String PQL_TAG = "pql";
	
	// internal view
	private static final String PIPEID_TAG = "pipeid";
	private static final String OUTPUTSCHEMA_TAG = "outputSchema";
	private static final String BASETIMEUNIT_TAG = "baseTimeunit";
	private static final String METASCHEMA_TAG = "metaschemata";
	private static final String METAELEMENT_TAG = "metaelement";
	private static final String METASCHEMA_DATATYPE_TAG = "metadatatype";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, NAME_TAG, PEER_ID_TAG };


	private ID id;
	private String name;
	private PipeID pipeID;
	private PeerID peerID;
	private SDFSchema outputSchema;
	private List<SDFMetaSchema> metaschemata;
	private String baseTimeunit;
	private String pqlText;

	public SourceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public SourceAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public SourceAdvertisement(SourceAdvertisement srcAdvertisement) {
		Preconditions.checkNotNull(srcAdvertisement, "Advertisement to copy must not be null!");

		id = srcAdvertisement.id;
		name = srcAdvertisement.name;
		pipeID = srcAdvertisement.pipeID;
		peerID = srcAdvertisement.peerID;
		outputSchema = srcAdvertisement.outputSchema;
		metaschemata = srcAdvertisement.metaschemata;
		pqlText = srcAdvertisement.pqlText;
		baseTimeunit = srcAdvertisement.baseTimeunit;
	}

	public SourceAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public SourceAdvertisement clone() throws CloneNotSupportedException {
		return new SourceAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		
		appendTo(doc);

		return doc;
	}
	
	void appendTo( Element<?> doc ) {
		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, NAME_TAG, name);
		appendElement(doc, PEER_ID_TAG, peerID.toString());
		
		if( isStream() ) {
			appendElement(doc, PQL_TAG, pqlText);
			
		} else {
			appendElement(doc, PIPEID_TAG, pipeID.toString());
		}
		
		Element<?> outSchemaElement = appendElement(doc, OUTPUTSCHEMA_TAG, outputSchema.getURI());
		for (final SDFAttribute attr : outputSchema) {
			appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
		}	
		if( !Strings.isNullOrEmpty(baseTimeunit)) {
			appendElement(doc, BASETIMEUNIT_TAG, baseTimeunit);
		}
		
		for(SDFMetaSchema metaSchema : metaschemata) {
			Element<?> metaSchemataElement = appendElement(doc, METASCHEMA_TAG, metaSchema.getURI());
			appendElement(metaSchemataElement,METAELEMENT_TAG,metaSchema.getMetaAttribute().getName());
			appendElement(metaSchemataElement,METASCHEMA_DATATYPE_TAG,metaSchema.getType().getName());
			for (final SDFAttribute attr : metaSchema) {
				appendElement(metaSchemataElement, attr.getAttributeName(), attr.getDatatype().getURI());
			}	
		}
		
		
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
	
	public void setMetaSchemata(List<SDFMetaSchema> metaSchema)  {
		this.metaschemata = metaSchema;
	}
	
	public List<SDFMetaSchema> getMetaSchemata() {
		return this.metaschemata;
	}

	public void setPipeID(PipeID pipeID) {
		this.pipeID = pipeID;
	}

	public PipeID getPipeID() {
		return pipeID;
	}
	
	public boolean isLocal() {
		return this.peerID.equals(P2PNetworkManagerService.getInstance().getLocalPeerID());
	}
	
	public boolean isStream() {
		return !Strings.isNullOrEmpty(pqlText);
	}
	
	public boolean isView() {
		return !isStream();
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
	
	public String getPQLText() {
		return pqlText;
	}
	
	public void setPQLText(String text) {
		this.pqlText = text;
	}
	
	public String getBaseTimeunit() {
		return baseTimeunit;
	}
	
	public void setBaseTimeunit( String baseTimeunit ) {
		this.baseTimeunit = baseTimeunit;
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
		if (!(obj instanceof SourceAdvertisement)) {
			return false;
		}
		SourceAdvertisement adv = (SourceAdvertisement) obj;
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

		} else if( elem.getName().equals(PQL_TAG)) {
			pqlText = elem.getTextValue();
			
		} else if (elem.getName().equals(PIPEID_TAG)) {
			setPipeID((PipeID) toID(elem));

		} else if (elem.getName().equals(OUTPUTSCHEMA_TAG)) {
			setOutputSchema(handleOutputSchemaTag(elem, getName()));

		} else if (elem.getName().equals(BASETIMEUNIT_TAG)) {
			setBaseTimeunit(elem.getTextValue());

		} else if(elem.getName().equals(METASCHEMA_TAG)) {
			if(metaschemata==null) {
				metaschemata = Lists.newArrayList();
			}
			metaschemata.add(handleMetaSchemaTag(elem,elem.getValue()));
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
			final SDFAttribute attr = new SDFAttribute(viewName, elem.getKey(), ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getDatatype(elem.getTextValue()), null, null, null);
			attributes.add(attr);
		}

		return SDFSchemaFactory.createNewTupleSchema(root.getTextValue(), attributes);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static SDFMetaSchema handleMetaSchemaTag(TextElement<?> root, String viewName) {
		final Enumeration<?> children = root.getChildren();
		Class metaAttributeClass=null;
		Class dataTypeClass=null;
		final List<SDFAttribute> attributes = Lists.newArrayList();
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			if(elem.getName().equals(METAELEMENT_TAG)) {
				try {
					metaAttributeClass = Class.forName(elem.getValue());
				} catch (ClassNotFoundException e) {
					LOG.error("Class for MetaElement not found: {}",elem.getValue());
					e.printStackTrace();
				}
			} else {
				if(elem.getName().equals(METASCHEMA_DATATYPE_TAG)) {
					try {
						dataTypeClass = Class.forName(elem.getValue());
					} catch (ClassNotFoundException e) {
						LOG.error("Class for MetaSchemaDatatype not found: {}",elem.getValue());
						e.printStackTrace();
					}
				}
				else {
				
					final SDFAttribute attr = new SDFAttribute(viewName, elem.getKey(), ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getDatatype(elem.getTextValue()), null, null, null);
					attributes.add(attr);
				}
			}
		}
		
		return SDFSchemaFactory.createNewMetaSchema(viewName, dataTypeClass, attributes, metaAttributeClass);
		

	}


}
