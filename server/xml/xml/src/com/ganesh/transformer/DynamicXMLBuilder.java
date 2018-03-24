package com.ganesh.transformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

public class DynamicXMLBuilder<T extends IMetaAttribute> {
    
	private final static String DEFAULT_VALUE = "NO_VALUE";
	private final String PROP_INTENDAMOUNT = "indentamount";
	private final String PROP_ENCODING = "encoding";
	
	public Document createXML(String rootElement, Tuple<T> object, String xsdSchema, SDFSchema sdfSchema,
			Map<String, Integer> attributeMapping, Map<String, String> xpathMapping, Properties properties) throws XPathExpressionException {

		Document xml = generateDocument(rootElement, xsdSchema, sdfSchema, properties);

		// Set elements that are implied by their name. However, if the same attribute is associated
		// with an x-path expression, it will be considered by the expression. Thus, this action
		// will ignore the attribute.
		NodeList nodeList = xml.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (attributeMapping.containsKey(node.getNodeName().toLowerCase())
						&& !xpathMapping.containsKey(node.getNodeName())) {
					node.setTextContent(object.getAttribute(attributeMapping.get(node.getNodeName())));
				}
			}
		}

		// Set values of attributes or elements specified with x-path expressions.
		for (Map.Entry<String, String> e : xpathMapping.entrySet()) {

			String expression = e.getValue();
			Object attributeValue = object.getAttribute(attributeMapping.get(e.getKey()));
			String value = DEFAULT_VALUE;
			if (attributeValue != null) {
				value = attributeValue.toString();
			}
			
			NodeList nodes = getNodeList(expression, xml);
			if (nodes.item(0) instanceof Attr) {
				((Attr) nodes.item(0)).setNodeValue(value);
			} else if (nodes.item(0) instanceof Element) {
				((Element) nodes.item(0)).setTextContent(value);
			}

		}

		return xml;
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

	private NodeList getNodeList(String expression, Document document) throws XPathExpressionException {
		return (NodeList) XPathFactory.newInstance().newXPath().compile(expression).evaluate(document, XPathConstants.NODESET);
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
