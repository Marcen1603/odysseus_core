package de.uniol.inf.is.odysseus.p2p_new.dictionary.sources;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.document.Element;
import net.jxta.document.TextElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public final class AccessAOConverter {

	private static final Logger LOG = LoggerFactory.getLogger(AccessAOConverter.class);

	private static final String SOURCE_TAG = "source";
	private static final String INPUT_SCHEMA_TAG = "inputSchema";
	private static final String INPUT_SCHEMA_ITEM_TAG = "inputSchemaItem";
	private static final String OPTIONS_TAG = "options";
	private static final String WRAPPER_TAG = "wrapper";
	private static final String DATAHANDLER_TAG = "dataHandler";
	private static final String PROTOCOLHANDLER_TAG = "protocolHandler";
	private static final String TRANSPORTHANDLER_TAG = "transportHandler";
	private static final String OUTPUTSCHEMA_TAG = "outputSchema";
	private static final String OUTPUTSCHEMA_TYPE_TAG = "___type___";

	public static AbstractAccessAO toAccessAO(TextElement<?> root) {
		final Enumeration<?> elements = root.getChildren();
		final AccessAO accessOperator = new AccessAO();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(accessOperator, elem);
		}

		return accessOperator;
	}

	public static void toDocument(Element<?> root, AbstractAccessAO accessOperator) {
		final Element<?> inputSchemaElement = appendElement(root, INPUT_SCHEMA_TAG);
		if (accessOperator.getInputSchema() != null && !accessOperator.getInputSchema().isEmpty()) {
			for (final String entry : accessOperator.getInputSchema()) {
				appendElement(inputSchemaElement, INPUT_SCHEMA_ITEM_TAG, entry);
			}
		}
		
		appendElement(root, SOURCE_TAG, accessOperator.getName());

		final Element<?> optionsElement = appendElement(root, OPTIONS_TAG);
		final Map<String, String> options = accessOperator.getOptionsMap();
		if (options != null && !options.isEmpty()) {
			for (final String key : options.keySet()) {
				if( key.equalsIgnoreCase("host")) {
					appendElement(optionsElement, key, determineHost(options.get(key)));
				} else {
					appendElement(optionsElement, key, options.get(key));
				}
			}
		}

		appendElement(root, WRAPPER_TAG, accessOperator.getWrapper());
		appendElement(root, DATAHANDLER_TAG, accessOperator.getDataHandler());
		appendElement(root, PROTOCOLHANDLER_TAG, accessOperator.getProtocolHandler());
		appendElement(root, TRANSPORTHANDLER_TAG, accessOperator.getTransportHandler());

		final SDFSchema outputSchema = accessOperator.getOutputSchema();
		if (outputSchema != null && !outputSchema.isEmpty()) {
			final Element<?> outSchemaElement = appendElement(root, OUTPUTSCHEMA_TAG, outputSchema.getURI());
			
			appendElement(outSchemaElement, OUTPUTSCHEMA_TYPE_TAG, outputSchema.getType().getName());
			
			for (final SDFAttribute attr : outputSchema) {
				appendElement(outSchemaElement, attr.getAttributeName(), attr.getDatatype().getURI());
			}
		}
	}

	private static String determineHost(String host) {
		if (host == null) {
			return null;
		}

		if (host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) {
			Optional<String> realHostAddress = determineHostAddress();

			return realHostAddress.isPresent() ? realHostAddress.get() : host;
		}
		return host;
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

	private static void handleElement(AbstractAccessAO accessAO, TextElement<?> elem) {
		if (elem.getName().equals(INPUT_SCHEMA_TAG)) {
			handleInputSchemaElement(accessAO, elem);

		} else if (elem.getName().equals(SOURCE_TAG)) {
			accessAO.setAccessAOName(new Resource(SessionManagementService.getActiveSession().getUser().getName() + "." + elem.getTextValue()));

		} else if (elem.getName().equals(OPTIONS_TAG)) {
			handleOptionsTag(accessAO, elem);

		} else if (elem.getName().equals(WRAPPER_TAG)) {
			accessAO.setWrapper(elem.getTextValue());

		} else if (elem.getName().equals(DATAHANDLER_TAG)) {
			accessAO.setDataHandler(elem.getTextValue());

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

	private static void handleInputSchemaElement(AbstractAccessAO accessAO, TextElement<?> root) {
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

	private static void handleOptionsTag(AbstractAccessAO accessAO, TextElement<?> root) {
		final Enumeration<?> children = root.getChildren();
		final Map<String, String> options = Maps.newHashMap();

		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			options.put(elem.getKey(), elem.getTextValue());
		}

		if (!options.isEmpty()) {
			accessAO.setOptionMap(options);
		}
	}

	@SuppressWarnings("unchecked")
	private static void handleOutputSchemaTag(AbstractAccessAO accessAO, TextElement<?> root) {
		Enumeration<?> children = root.getChildren();
		List<SDFAttribute> attributes = Lists.newArrayList();
		
		Class<? extends IStreamObject<?>> schemaType = null;
		
		while (children.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) children.nextElement();
			
			if( elem.getKey().equals(OUTPUTSCHEMA_TYPE_TAG) ) {
				schemaType = tryGetSchemaType(elem);
			} else {
				// TODO: Unit and Constraints?
				final SDFAttribute attr = new SDFAttribute("", elem.getKey(), ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getDatatype(elem.getTextValue()), null, null, null);
				attributes.add(attr);
			}
		}
		if (!attributes.isEmpty()) {
			if( schemaType == null ) {
				LOG.error("Schematype not set! Use Tuple-class as default!");
				schemaType = (Class<? extends IStreamObject<?>>) Tuple.class;
			}
			
			final SDFSchema schema = new SDFSchema(root.getTextValue(), schemaType, attributes);
			accessAO.setOutputSchema(schema);
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends IStreamObject<?>> tryGetSchemaType(final TextElement<?> elem) {
		try {
			return (Class<? extends IStreamObject<?>>) Class.forName(elem.getValue());
		} catch (ClassNotFoundException ex) {
			LOG.error("Could not get schematype");
			return null;
		}
	}
	
	private static Optional<String> determineHostAddress() {
		try {
			Enumeration<NetworkInterface> e= NetworkInterface.getNetworkInterfaces();
	        while(e.hasMoreElements())
	        {
	            Enumeration<InetAddress> ee = e.nextElement().getInetAddresses();
	            while(ee.hasMoreElements())
	            {
	                InetAddress i = ee.nextElement();
	                if( !i.isLoopbackAddress() && i instanceof Inet4Address) {
	                	return Optional.of(i.getHostAddress());
	                }
	            }
	        }
		} catch( Throwable t ) {
			LOG.error("Could not determine host address", t);
		}
		return Optional.absent();
	}
}
