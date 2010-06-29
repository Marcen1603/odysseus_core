package de.uniol.inf.is.odysseus.rcp.editor.model.cfg.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.IOperatorDescriptor;
import de.uniol.inf.is.odysseus.rcp.editor.model.cfg.IOperatorDescriptorProvider;

public class OperatorDescriptorXMLReader implements IOperatorDescriptorProvider{

	private final Collection<IOperatorDescriptor> ops = new ArrayList<IOperatorDescriptor>();
	private final Logger logger = LoggerFactory.getLogger(OperatorDescriptorXMLReader.class);
	
	public OperatorDescriptorXMLReader( URL xmlFile, URL xsdFile ) {
		
		// VALIDATION
		SchemaFactory factory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );	
		Schema schema;
		try {
			schema = factory.newSchema(xsdFile);
		} catch( SAXException ex ) {
			logger.error( " canntot compile Schemafile " + xsdFile + "because ", ex );
			return;
		}
		
		try {
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xmlFile.openStream());
			validator.validate( source );
		} catch( SAXException ex ) {
			logger.error( "Configurationfile is not valid with " + xsdFile + "because ", ex );
			return;
		} catch( IOException e ) {
			logger.error( "IOException during validating configurationFile ", e );
			return;
		}

		//XMLDatei einlesen
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		docFactory.setNamespaceAware(true); // never forget this!
		try {
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile.openStream());
			
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			
			XPathExpression expr = xpath.compile("/Operators/Operator");
			XPathExpression exprLabel = xpath.compile("Label[1]/text()");
			XPathExpression exprGroup = xpath.compile("Group[1]/text()");
			
			NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			
			for( int i = 0 ; i < nodes.getLength(); i++ ) {
				Node node = nodes.item( i );
				
				// Grunddaten
				final String name = getAttributeValue(node, "name");
				final String clazz = getAttributeValue(node, "class");
				
				EditableOperatorDescriptor desc = new EditableOperatorDescriptor(name, clazz);
				
				// Label
				Node child = (Node)exprLabel.evaluate(node, XPathConstants.NODE);
				desc.setLabel(child.getNodeValue());
				
				// Group
				child = (Node)exprGroup.evaluate(node, XPathConstants.NODE);
				desc.setGroup(child.getNodeValue());
				
				ops.add(desc);
			}
			
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	private static String getAttributeValue( Node node, String attributeName ) {
		if( node == null ) 
			return "";
		NamedNodeMap attributes = node.getAttributes();
		if( attributes != null ) {
			Node item = attributes.getNamedItem( attributeName );
			if( item != null )
				return item.getNodeValue();
		}
		return "";
	}

	@Override
	public Collection<IOperatorDescriptor> getOperatorDescriptors() {
		return ops;
	}

}
