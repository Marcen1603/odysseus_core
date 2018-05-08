package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.DOMImplementationLS;

import com.ganesh.transformer.DynamicXMLBuilder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObjectDataHandler;

public class XMLTransformPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>> {

	private org.slf4j.Logger log = LoggerFactory.getLogger(XMLTransformPO.class);

	private IStreamObjectDataHandler<XMLStreamObject<? extends IMetaAttribute>> xsoHandler = new XMLStreamObjectDataHandler();
	private DynamicXMLBuilder<T> dynamicXMLBuilder;
	private Transformer transformer;
	private String xslt;
	private int xsltSchemaHash;
	private boolean dynamicXSD;
	private OptionMap options;
	private Properties properties;

	public XMLTransformPO(Path xsdFile, String xsdString, boolean isDynamic, OptionMap optionsMap) throws IOException {

		this.options = optionsMap;
		this.dynamicXSD = isDynamic;

		try {
			this.transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}

		// get xslt
		if (xsdFile != null) {
			xslt = new String(Files.readAllBytes(xsdFile));
			xsltSchemaHash = xslt.hashCode();
			log.info("referencing xslt from file");
		} else if (xsdString != null) {
			xslt = xsdString;
			xsltSchemaHash = xslt.hashCode();
			log.info("referencing xslt from string");
		} else {
			if (!dynamicXSD) {
				log.error("no xslt were provided");
				throw new IllegalArgumentException("no xslt were provided");
			}
		}
		
		// create properties
		properties = new Properties();
		if (optionsMap != null) {
			optionsMap.getOptions().entrySet().stream().forEach(o -> {
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

		// format and log properties
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		Enumeration<?> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) properties.get(key);
			builder.append("\t" + key + "=" + value + ",\n");
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

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(XMLStreamObject<T> object, int port) {

		// Check if schema has changed
		if (dynamicXSD) {
						
			String docStr = ((DOMImplementationLS) object.getDocument()
					.getImplementation())
					.createLSSerializer()
					.writeToString(object.getDocument());
			
            String newXslt = docStr.substring(
            		docStr.indexOf("<xsl:stylesheet"), 
            		docStr.indexOf("</xsl:stylesheet") + 17
        		);
			
			int hash = newXslt.hashCode();
			if (xsltSchemaHash != hash) {
				xslt = newXslt;
				xsltSchemaHash = hash;
			}
		}

		try {

			// Generate new xml document
			DOMSource source = new DOMSource(dynamicXMLBuilder.transformXML(
					object.getDocument(),
					xslt
				)
			);

			//TODO result can also be a pure string!
			// Translate document to string and send it
			StringWriter sw = new StringWriter();
			transformer.setOutputProperty(OutputKeys.ENCODING, properties.getProperty("encoding"));
			transformer.setOutputProperty(OutputKeys.INDENT, properties.getProperty("intend"));
			transformer.transform(source, new StreamResult(sw));

			XMLStreamObject<T> output = (XMLStreamObject<T>) xsoHandler.readData(sw.toString());
			if (output != null && !output.isEmpty()) {
				if (object.getMetadata() != null) {
					output.setMetadata((T) object.getMetadata().clone());
				}
				transfer(output);
			}

		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
