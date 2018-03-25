package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.LoggerFactory;

import com.ganesh.transformer.DynamicXMLBuilder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;

public class ToXMLPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, XMLStreamObject<T>> {
	
	private IStreamObjectDataHandler<XMLStreamObject<? extends IMetaAttribute>> xsoHandler = new XMLStreamObjectDataHandler();
	
	private org.slf4j.Logger log = LoggerFactory.getLogger(ToXMLPO.class);
	
	private DynamicXMLBuilder<T> dynamicXMLBuilder;
	private Transformer transformer;
	private Properties properties;
	private String rootElement;
	private String xsd;
	private String rootAttribute;
	private String xsdAttribute;
	private Collection<String> xPathAttributes = new ArrayList<>(); 
	private HashMap<String, String> xPathMapping = new HashMap<>();
	private HashMap<String, Integer> mapping = new HashMap();
	private boolean dynamicXSD;
	private boolean dynamicXPath;
	private boolean setXpath;
	private boolean setMapping;
	private int xsdSchemaHash;
	private OptionMap options;
	
	public ToXMLPO(String rootElement, String rootAttribute, Path xsdFile, String xsdString, String xsdAttribute, Collection<String> xPathAttributes, OptionMap optionMap) throws IOException {
		
		this.rootElement = rootElement; 
		this.options = optionMap;

		try {
			this.transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		
		// get xsd
		if (xsdFile != null) {
			xsd = new String(Files.readAllBytes(xsdFile));
			xsdSchemaHash = xsd.hashCode();
			log.info("referencing xsd from file");
		} else if(xsdString != null) {
			xsd = xsdString;
			xsdSchemaHash = xsd.hashCode();
			log.info("referencing xsd from string");
		} else if(xsdAttribute != null) {
			this.xsdAttribute = xsdAttribute;
			this.rootAttribute = rootAttribute;
			this.rootElement = null;
			this.dynamicXSD = true;
			log.info("referencing xsd from tuple attribute " + xsdAttribute);
		} else {
			log.error("no xsd were provided");
			throw new IllegalArgumentException("no xsd were provided");
		}

		// check if dynamic xpath expressions are used
		if (xPathAttributes != null && !xPathAttributes.isEmpty()) {
			dynamicXPath = true;
			this.xPathAttributes = new ArrayList<>(xPathAttributes);
		}
		
		// create properties
		properties = new Properties();
		if (optionMap != null) {
			optionMap.getOptions().entrySet().stream().forEach(o -> {
				properties.setProperty(o.getKey(), (String) o.getValue());
			});
		}
		
		// set default properties
		if (!properties.containsKey("encoding")) {
			properties.put("encoding", "UTF-8");
		}
		if (!properties.containsKey("intend")) {
			properties.put("intend", "yes");
		}
		if (!properties.containsKey("indentamount")) {
			properties.put("indentamount", "4");
		}
		if (!properties.containsKey("delimeter")) {
			properties.put("delimeter", ",");
		}
		if (!properties.containsKey("showContentModel") && !properties.containsKey("showcontentmodel")) {
			properties.put("showContentModel", "never");
		}
		if (!properties.containsKey("generateAllChoices") && !properties.containsKey("generateallchoices")) {
			properties.put("generateAllChoices", "always");
		}
		if (!properties.containsKey("minimumElementsGenerated") && !properties.containsKey("minimumelementsgenerated")) {
			properties.put("minimumElementsGenerated", "1");
		}
		if (!properties.containsKey("maximumElementsGenerated") && !properties.containsKey("maximumelementsgenerated")) {
			properties.put("maximumElementsGenerated", "1");
		}
		if (!properties.containsKey("generateOptionalElements") && !properties.containsKey("generateoptionalelements")) {
			properties.put("generateOptionalElements", "always");
		}
		
		// format and log properties
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		Enumeration<?> keys = properties.keys();
		while (keys.hasMoreElements()) {
		    String key = (String)keys.nextElement();
		    String value = (String) properties.get(key );
		    builder.append("\t"+ key + "=" + value + ",\n");
		}
		builder.append("}\n");

		log.info("the following options were provided:\n" + builder.toString());
		
		dynamicXMLBuilder = new DynamicXMLBuilder<>();
		
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	@SuppressWarnings({ "unchecked" })
	protected void process_next(Tuple<T> object, int port) {

		// create x-path mapping
		if (dynamicXPath || !setXpath) {
			Map<String, String> xpaths = new HashMap<>();
			Map<String, Integer> mapping = new HashMap<>();
			
			// Check for static x-path expressions
			if (!setXpath) {
				final SDFSchema schema = getInputSchema(port);
				for (int i = 0; i < schema.size(); i++) {
					final String attr = schema.get(i).getAttributeName();
					if (options.containsKey(attr)) {
						xpaths.put(attr, options.get(attr));
					}
				}
			}
			
			// create attribute mapping
			if (!setMapping) {
				int index = 0;
				for (SDFAttribute attribute : getInputSchema(port).getAttributes()) {
					mapping.put(attribute.getAttributeName().toLowerCase(), index++);
				}
				setMapping(mapping);
			}
			
			// Check for x-path expression in tuple attributes
			if (dynamicXPath) {

				this.mapping.entrySet().stream()
					.filter(p -> xPathAttributes.contains(p.getKey()))
					.forEach(m -> {
						
						int index = this.mapping.get(m.getKey());
						String component = object.getAttribute(index);
						String delimeter = properties.getProperty("delimeter");
						if (component != null && !component.isEmpty() && component.contains(delimeter)) {
								final String[] split = component.split(delimeter);
								if (split.length % 2 == 0) {
									
									for(int k = 0; k < split.length; k += 2) {
										
										// If given attribute name in the string is not in the schema, throw exception
										if (!this.mapping.containsKey(split[k])) {
											throw new IllegalArgumentException(split[k] + " not found in schema");
										}
										
										// Add new expression
										xpaths.put(split[k], split[k + 1]);
									}
									
								} else {
									throwIllegalArgumentExceptionCausedByMissingXPathAttribute(m.getKey(), delimeter, component);
								}
						} else {
							throwIllegalArgumentExceptionCausedByMissingXPathAttribute(m.getKey(), delimeter, component);
						}
				});
			}

			setXPaths(xpaths);
		}
		
		// Check if schema has changed
		if (dynamicXSD) {
			String newXsd = object.getAttribute(mapping.get(xsdAttribute));
			int hash = newXsd.hashCode();
			if (xsdSchemaHash != hash) {
				this.xsd = newXsd;
				this.xsdSchemaHash = hash;
				// Get possible new root element
				rootElement = object.getAttribute(mapping.get(rootAttribute));
			}			
		} 
		
		try {
			
			// Generate new xml document
			DOMSource source = new DOMSource(
					dynamicXMLBuilder.createXML(
							rootElement, 
							object, 
							xsd, 
							getInputSchema(port),
							mapping, 
							xPathMapping, 
							properties
				)
			);

			// Translate document to string and send it
			StringWriter sw = new StringWriter();
			transformer.setOutputProperty(OutputKeys.ENCODING, properties.getProperty("encoding"));
			transformer.setOutputProperty(OutputKeys.INDENT, properties.getProperty("intend"));
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, new StreamResult(sw));

			XMLStreamObject<T> output = (XMLStreamObject<T>) xsoHandler.readData(sw.toString());
			if (output != null && !output.isEmpty()) {
				output.setMetadata((T) object.getMetadata().clone());
				transfer(output);
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
			
	}

	private void throwIllegalArgumentExceptionCausedByMissingXPathAttribute(String key, String delimeter, String component) {
		throw new IllegalArgumentException(
				  "wrong string format in xPathAttribute "+ key 
				+ ": should be attributename0" + delimeter + "xpath0, attributename1" + delimeter + "xpath1, ...\n"
				+ " it was actually " + component
			);		
	}

	private void setMapping(Map<String, Integer> mapping) {
		this.mapping = new HashMap<>(mapping);
		setMapping = true;
	}

	private void setXPaths(Map<String, String> xpaths) {
		this.xPathMapping.putAll(xpaths);
		setXpath = true;
	}
}
