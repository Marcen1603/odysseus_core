package com.ganesh.transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

public class DynamicXMLBuilder<T extends IMetaAttribute> {
    
	private final static String DEFAULT_VALUE = "NO_VALUE";
	private final String PROP_INTENDAMOUNT = "indentamount";
	private final String PROP_ENCODING = "encoding";
	
	public Document createXML(String rootElement, Tuple<T> object, String xsdSchema, SDFSchema sdfSchema,
			Map<String, Integer> attributeMapping, Map<String, String> xPathMapping, Properties properties) throws XPathExpressionException {

		Document xml = generateDocument(rootElement, xsdSchema, sdfSchema, properties);

		// Set elements that are implied by their name. However, if the same attribute is associated
		// with an x-path expression, it will be considered by the expression. Thus, this action
		// will ignore the attribute.
		NodeList nodeList = xml.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (attributeMapping.containsKey(node.getNodeName().toLowerCase())
						&& !xPathMapping.containsKey(node.getNodeName())) {
					node.setTextContent(object.getAttribute(attributeMapping.get(node.getNodeName())).toString());
				}
			}
		}

		// Set values of attributes or elements specified with x-path expressions.
		return setValuesWithXPath(xPathMapping, attributeMapping, object, xml);
	}

	private Document generateDocument(String rootElement, String xsdSchema, SDFSchema sdfSchema, Properties properties) {

		XSModel xsModel = new XSParser().parseString(xsdSchema, "");
		XSInstance xsInstance = new XSInstance();
		xsInstance.loadOptions(properties);
		xsInstance.sampleValueGenerator = new EmptySampleValueGenerator();

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()){

			XMLDocument sampleXml = new XMLDocument(
					new StreamResult(out), 
					false, 
					Integer.parseInt((String) properties.getProperty(PROP_INTENDAMOUNT)), 
					properties.getProperty(PROP_ENCODING)
			);
			
			xsInstance.generate(xsModel, new QName("", rootElement), sampleXml);

			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new ByteArrayInputStream(out.toByteArray()));

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public Document setValuesWithXPath(Map<String, String> xPathMapping, Map<String, Integer> attributeMapping, Tuple<T> object, Document xml) throws XPathExpressionException {
		for (Map.Entry<String, String> e : xPathMapping.entrySet()) {

			String expression = e.getValue();
			Object attributeValue = object.getAttribute(attributeMapping.get(e.getKey()));
			String value = DEFAULT_VALUE;
			if (attributeValue != null) {
				value = attributeValue.toString();
			}
			
			NodeList nodes = XMLStreamObject.getNodeList(expression, xml);
			if (nodes.item(0) instanceof Attr) {
				((Attr) nodes.item(0)).setNodeValue(value);
			} else if (nodes.item(0) instanceof Element) {
				((Element) nodes.item(0)).setTextContent(value);
			}

		}
		
		return xml;
	}
	
	public Document transformXML(Document content, String xslt) {
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(new ByteArrayInputStream(xslt.getBytes())));
			transformer.transform(new DOMSource(content), new StreamResult(out));

			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new ByteArrayInputStream(out.toByteArray()));

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return null;
	}
	
    public static class EmptySampleValueGenerator implements XSInstance.SampleValueGenerator{

		@Override
		public String generateSampleValue(XSElementDeclaration element, XSSimpleTypeDefinition simpleType) {
			return DEFAULT_VALUE;
		}

		@Override
		public String generateSampleValue(XSAttributeDeclaration attribute, XSSimpleTypeDefinition simpleType) {
			return DEFAULT_VALUE;
		}
		
    }
	
}
