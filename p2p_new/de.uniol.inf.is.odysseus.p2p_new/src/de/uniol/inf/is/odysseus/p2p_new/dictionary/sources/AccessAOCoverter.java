package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.document.Element;
import net.jxta.document.TextElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;

public final class AccessAOCoverter {

	private static final Logger LOG = LoggerFactory.getLogger(AccessAOCoverter.class);

//	private static final String ID_TAG = "id";
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

//	public static String[] getIndexableFieldTags() {
//		return new String[] { SOURCE_NAME_TAG };
//	}

	public static AccessAO toAccessAO(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();
		final AccessAO accessOperator = new AccessAO();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(accessOperator, elem);
		}

		return accessOperator;
	}

//	public static Document toDocument(MimeMediaType asMimeType, ID id, AccessAO accessOperator) {
	public static void toDocument(Element<?> root, AccessAO accessOperator) {
//		final StructuredDocument<?> doc = StructuredDocumentFactory.newStructuredDocument(asMimeType, StreamAdvertisement.getAdvertisementType());
//		if (doc instanceof Attributable) {
//			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
//		}

//		appendElement(doc, ID_TAG, id.toString());
		appendElement(root, SOURCE_NAME_TAG, withoutUsername(accessOperator.getSourcename()));
		final Element<?> inputSchemaElement = appendElement(root, INPUT_SCHEMA_TAG);
		if (accessOperator.getInputSchema() != null && !accessOperator.getInputSchema().isEmpty()) {
			for (final String entry : accessOperator.getInputSchema()) {
				appendElement(inputSchemaElement, INPUT_SCHEMA_ITEM_TAG, entry);
			}
		}
		appendElement(root, PORT_TAG, String.valueOf(accessOperator.getPort()));
		appendElement(root, HOST_TAG, accessOperator.getHost());
		appendElement(root, LOGIN_TAG, accessOperator.getLogin());
		appendElement(root, PASSWORD_TAG, accessOperator.getPassword());
		appendElement(root, AUTOCONNECT_TAG, String.valueOf(accessOperator.isAutoReconnectEnabled()));

		final Element<?> optionsElement = appendElement(root, OPTIONS_TAG);
		final Map<String, String> options = accessOperator.getOptionsMap();
		if (options != null && !options.isEmpty()) {
			for (final String key : options.keySet()) {
				appendElement(optionsElement, key, options.get(key));
			}
		}

		appendElement(root, WRAPPER_TAG, accessOperator.getWrapper());
		appendElement(root, INPUT_TAG, accessOperator.getInput());
		appendElement(root, DATAHANDLER_TAG, accessOperator.getDataHandler());
		appendElement(root, TRANSFORMER_TAG, accessOperator.getTransformer());
		appendElement(root, OBJECTHANDLER_TAG, accessOperator.getObjectHandler());
		appendElement(root, INPUTDATAHANDLER_TAG, accessOperator.getInputDataHandler());
		appendElement(root, ACCESSCONNECTIONHANDLER_TAG, accessOperator.getAccessConnectionHandler());
		appendElement(root, PROTOCOLHANDLER_TAG, accessOperator.getProtocolHandler());
		appendElement(root, TRANSPORTHANDLER_TAG, accessOperator.getTransportHandler());

		final SDFSchema outputSchema = accessOperator.getOutputSchema();
		if (outputSchema != null && !outputSchema.isEmpty()) {
			final Element<?> outSchemaElement = appendElement(root, OUTPUTSCHEMA_TAG, outputSchema.getURI());
			for (final SDFAttribute attr : outputSchema) {
				appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
			}
		}

//		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag, String value) {
		final Element ele = appendTo.getRoot().createElement(tag, value);
		appendTo.appendChild(ele);
		return ele;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element<?> appendElement(Element appendTo, String tag) {
		final Element<?> ele = appendTo.getRoot().createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private static Element appendElement(StructuredDocument appendTo, String tag, String value) {
//		final Element createElement = appendTo.createElement(tag, value);
//		appendTo.appendChild(createElement);
//		return createElement;
//	}

	private static String withoutUsername(String sourcename) {
		final int pos = sourcename.indexOf(".");
		return pos != -1 ? sourcename.substring(pos+1) : sourcename;
	}

	private static void handleElement(AccessAO accessAO, TextElement<?> elem) {
//		if (elem.getName().equals(ID_TAG)) {
//			handleIDTag(adv, elem);
//
//		} else 
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

//	private static void handleIDTag(StreamAdvertisement adv, TextElement<?> elem) {
//		try {
//			final URI id = new URI(elem.getTextValue());
//			adv.setID(IDFactory.fromURI(id));
//		} catch (URISyntaxException | ClassCastException ex) {
//			LOG.error("Could not set id", ex);
//		}
//	}

	private static void handleInputSchemaElement(AccessAO accessAO, TextElement<?> root) {
		final Enumeration<?> children = root.getChildren();
		final List<String> inputSchema = Lists.newArrayList();
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			inputSchema.add(elem.getTextValue());
		}
		if (!inputSchema.isEmpty()) {
			accessAO.setInputSchema(inputSchema);
		}
	}

	private static void handleOptionsTag(AccessAO accessAO, TextElement<?> root) {
		final Enumeration<?> children = root.getChildren();
		final Map<String, String> options = Maps.newHashMap();

		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			options.put(elem.getKey(), elem.getTextValue());
		}

		if (!options.isEmpty()) {
			accessAO.setOptions(options);
		}
	}

	private static void handleOutputSchemaTag(AccessAO accessAO, TextElement<?> root) {
		final Enumeration<?> children = root.getChildren();
		final List<SDFAttribute> attributes = Lists.newArrayList();
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			final SDFAttribute attr = new SDFAttribute(accessAO.getSourcename(), elem.getKey(), DataDictionaryService.get().getDatatype(elem.getTextValue()));
			attributes.add(attr);
		}

		if (!attributes.isEmpty()) {
			final SDFSchema schema = new SDFSchema(root.getTextValue(), attributes);
			accessAO.setOutputSchema(schema);
		}
	}
}
