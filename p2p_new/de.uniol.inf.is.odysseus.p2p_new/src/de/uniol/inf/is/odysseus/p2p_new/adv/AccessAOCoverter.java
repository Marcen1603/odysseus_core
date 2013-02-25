package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.datasrc.DataSourceManager;

public final class AccessAOCoverter {

	private static final Logger LOG = LoggerFactory.getLogger(AccessAOCoverter.class);

	private static final String ID_TAG = "id";
	private static final String SOURCE_NAME_TAG = "sourceName";
	private static final String INPUT_SCHEMA_TAG = "inputSchema";
	private static final String INPUT_SCHEMA_ITEM_TAG = "inputSchemaItem";
	private static final String PORT_TAG = "port";
	private static final String HOST_TAG = "host";
	private static final String LOGIN_TAG = "login";
	private static final String PASSWORD_TAG = "password";
	private static final String AUTOCONNECT_TAG = "autoconnect";
	private static final String OPTIONS_TAG = "options";
	private static final String WRAPPER_TAG = "wrapper";
	private static final String INPUT_TAG = "input";
	private static final String DATAHANDLER_TAG = "dataHandler";
	private static final String TRANSFORMER_TAG = "transformer";
	private static final String OBJECTHANDLER_TAG = "objectHandler";
	private static final String INPUTDATAHANDLER_TAG = "inputDataHandler";
	private static final String ACCESSCONNECTIONHANDLER_TAG = "accessConnectionHandler";
	private static final String PROTOCOLHANDLER_TAG = "protocolHandler";
	private static final String TRANSPORTHANDLER_TAG = "transportHandler";
	private static final String OUTPUTSCHEMA_TAG = "outputSchema";

	public static Document toDocument(MimeMediaType asMimeType, ID id, AccessAO accessOperator) {
		StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, SourceAdvertisement.getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendElement(doc, ID_TAG, id.toString());
		appendElement(doc, SOURCE_NAME_TAG, accessOperator.getSourcename());
		Element<?> inputSchemaElement = appendElement(doc, INPUT_SCHEMA_TAG);
		if (accessOperator.getInputSchema() != null && !accessOperator.getInputSchema().isEmpty()) {
			for (String entry : accessOperator.getInputSchema()) {
				appendElement(inputSchemaElement, INPUT_SCHEMA_ITEM_TAG, entry);
			}
		}
		appendElement(doc, PORT_TAG, String.valueOf(accessOperator.getPort()));
		appendElement(doc, HOST_TAG, accessOperator.getHost());
		appendElement(doc, LOGIN_TAG, accessOperator.getLogin());
		appendElement(doc, PASSWORD_TAG, accessOperator.getPassword());
		appendElement(doc, AUTOCONNECT_TAG, String.valueOf(accessOperator.isAutoReconnectEnabled()));

		Element<?> optionsElement = appendElement(doc, OPTIONS_TAG);
		Map<String, String> options = accessOperator.getOptionsMap();
		if (options != null && !options.isEmpty()) {
			for (String key : options.keySet()) {
				appendElement(optionsElement, key, options.get(key));
			}
		}

		appendElement(doc, WRAPPER_TAG, accessOperator.getWrapper());
		appendElement(doc, INPUT_TAG, accessOperator.getInput());
		appendElement(doc, DATAHANDLER_TAG, accessOperator.getDataHandler());
		appendElement(doc, TRANSFORMER_TAG, accessOperator.getTransformer());
		appendElement(doc, OBJECTHANDLER_TAG, accessOperator.getObjectHandler());
		appendElement(doc, INPUTDATAHANDLER_TAG, accessOperator.getInputDataHandler());
		appendElement(doc, ACCESSCONNECTIONHANDLER_TAG, accessOperator.getAccessConnectionHandler());
		appendElement(doc, PROTOCOLHANDLER_TAG, accessOperator.getProtocolHandler());
		appendElement(doc, TRANSPORTHANDLER_TAG, accessOperator.getTransportHandler());

		SDFSchema outputSchema = accessOperator.getOutputSchema();
		if (outputSchema != null && !outputSchema.isEmpty()) {
			Element<?> outSchemaElement = appendElement(doc, OUTPUTSCHEMA_TAG, outputSchema.getURI());
			for (SDFAttribute attr : outputSchema) {
				appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
			}
		}

		return doc;
	}

	public static AccessAO toAccessAO(TextElement<?> root, SourceAdvertisement adv) {
		checkType(root);

		Enumeration<?> elements = root.getChildren();
		AccessAO accessOperator = new AccessAO();
		while (elements.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) elements.nextElement();

			try {
				if (elem.getName().equals(ID_TAG)) {
					try {
						URI id = new URI(elem.getTextValue());
						adv.setID(IDFactory.fromURI(id));
					} catch (URISyntaxException | ClassCastException ex) {
						LOG.error("Could not set id", ex);
					}
				} else {
					handleElement(accessOperator, elem);
				}
			} catch (ClassNotFoundException | IOException ex) {
				LOG.error("Could not handle xml-element {}", elem, ex);
			}
		}

		return accessOperator;
	}
	
	public static String[] getIndexableFieldTags() {
		return new String[] { ID_TAG, SOURCE_NAME_TAG };
	}
	
	private static void handleElement(AccessAO accessAO, TextElement<?> elem) throws ClassNotFoundException, IOException {
		if (elem.getName().equals(SOURCE_NAME_TAG)) {
			accessAO.setSource(elem.getTextValue());
			
		} else if (elem.getName().equals(INPUT_SCHEMA_TAG)) {
			handleInputSchemaElement(accessAO, elem);
			
		} else if (elem.getName().equals(PORT_TAG)) {
			accessAO.setPort(Integer.valueOf(elem.getTextValue()));
			
		} else if (elem.getName().equals(HOST_TAG)) {
			accessAO.setHost(elem.getTextValue());
			
		} else if (elem.getName().equals(LOGIN_TAG)) {
			accessAO.setLogin(elem.getTextValue());
			
		} else if (elem.getName().equals(PASSWORD_TAG)) {
			accessAO.setPassword(elem.getTextValue());
			
		} else if (elem.getName().equals(AUTOCONNECT_TAG)) {
			accessAO.setAutoReconnectEnabled(Boolean.valueOf(elem.getTextValue()));
			
		} else if (elem.getName().equals(OPTIONS_TAG)) {
			handleOptionsTag(accessAO, elem);
			
		} else if (elem.getName().equals(WRAPPER_TAG)) {
			accessAO.setWrapper(elem.getTextValue());
			
		} else if (elem.getName().equals(INPUT_TAG)) {
			accessAO.setInput(elem.getTextValue());
			
		} else if (elem.getName().equals(DATAHANDLER_TAG)) {
			accessAO.setDataHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(TRANSFORMER_TAG)) {
			accessAO.setTransformer(elem.getTextValue());
			
		} else if (elem.getName().equals(OBJECTHANDLER_TAG)) {
			accessAO.setObjectHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(INPUTDATAHANDLER_TAG)) {
			accessAO.setInputDataHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(ACCESSCONNECTIONHANDLER_TAG)) {
			accessAO.setAccessConnectionHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(PROTOCOLHANDLER_TAG)) {
			accessAO.setProtocolHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(TRANSPORTHANDLER_TAG)) {
			accessAO.setTransportHandler(elem.getTextValue());
			
		} else if (elem.getName().equals(OUTPUTSCHEMA_TAG)) {
			handleOutputSchemaTag(accessAO, elem);
			
		} else {
			LOG.warn("Unknown element name: {}", elem.getName());
		}
	}

	private static void handleOutputSchemaTag(AccessAO accessAO, TextElement<?> root) {
		Enumeration<?> children = root.getChildren();
		List<SDFAttribute> attributes = Lists.newArrayList();
		while (children.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) children.nextElement();
			SDFAttribute attr = new SDFAttribute(accessAO.getSourcename(), elem.getKey(), DataSourceManager.getDataDictionary().getDatatype(elem.getTextValue()));
			attributes.add(attr);
		}

		if( !attributes.isEmpty()) {
			SDFSchema schema = new SDFSchema(root.getTextValue(), attributes);
			accessAO.setOutputSchema(schema);
		}
	}

	private static void handleOptionsTag(AccessAO accessAO, TextElement<?> root) {
		Enumeration<?> children = root.getChildren();
		Map<String, String> options = Maps.newHashMap();

		while (children.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) children.nextElement();
			options.put(elem.getKey(), elem.getTextValue());
		}
		
		if( !options.isEmpty() ) {
			accessAO.setOptions(options);
		}
	}

	private static void handleInputSchemaElement(AccessAO accessAO, TextElement<?> root) {
		Enumeration<?> children = root.getChildren();
		List<String> inputSchema = Lists.newArrayList();
		while (children.hasMoreElements()) {
			TextElement<?> elem = (TextElement<?>) children.nextElement();
			inputSchema.add(elem.getTextValue());
		}
		if(!inputSchema.isEmpty()) {
			accessAO.setInputSchema(inputSchema);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
		Element createElement = appendTo.createElement(tag, value);
		appendTo.appendChild(createElement);
		return createElement;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element<?> appendElement(StructuredDocument appendTo, String tag) {
		Element<?> ele = appendTo.createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag, String value) {
		Element ele = appendTo.getRoot().createElement(tag, value);
		appendTo.appendChild(ele);
		return ele;
	}

	private static void checkType(TextElement<?> root) {
		if (!root.getName().equals(SourceAdvertisement.getAdvertisementType())) {
			throw new IllegalArgumentException("Could not construct " + SourceAdvertisement.getAdvertisementType() + " from doc containing a " + root.getName());
		}
	}
}
