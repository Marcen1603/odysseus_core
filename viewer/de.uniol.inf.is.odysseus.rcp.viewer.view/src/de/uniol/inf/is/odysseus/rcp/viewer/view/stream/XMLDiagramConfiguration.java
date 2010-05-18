package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLDiagramConfiguration implements IDiagramConfiguration {

	private static final Logger logger = LoggerFactory.getLogger( XMLDiagramConfiguration.class );
	private Collection<DiagramInfo> diagramInfos = new ArrayList<DiagramInfo>();
	
	public XMLDiagramConfiguration( URL xmlFile, URL xsd ) {
		logger.info( "Paring resourceConfigurationfile " + xmlFile  );
		
		// VALIDATION
		SchemaFactory factory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );	
		Schema schema;
		try {
			schema = factory.newSchema(xsd);

		} catch( SAXException ex ) {
			logger.error( " canntot compile Schemafile " + xsd + "because " );
			logger.error( ex.getMessage() );
			return;
		}
		
		Validator validator = schema.newValidator();
		Source source = null;
		try {
			source = new StreamSource(xmlFile.openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			validator.validate( source );
			
		} catch( SAXException ex ) {
			logger.error( "Resourcesfile is not valid with " + xsd + "because " );
			logger.error( ex.getMessage() );
			return;
		} catch( IOException e ) {
			logger.error( "IOException during validating resourcesfile " );
			logger.error( e.getMessage() );
			return;
		}
		
		//XMLDatei einlesen
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true); // never forget this!
		try {
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile.openStream());
			
			// Über XPath Images rauslesen
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			XPathExpression expr = xpath.compile("/Diagrams/Diagram");
			NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			
			for( int i = 0 ; i < nodes.getLength(); i++ ) {
				
				final String name = getAttributeValue(nodes.item( i ), "name");
				final String diagramType = getAttributeValue( nodes.item( i ), "diagramType");
				final String converterType = getAttributeValue( nodes.item( i ), "converterType");
				
				Parameters params = parseParameters( nodes.item(i));
				diagramInfos.add( new DiagramInfo(name, diagramType, converterType, params) );
			}
			
			logger.info( "Paring resourceConfigurationfile successful" );

		} catch( ParserConfigurationException e ) {
			logger.error("ParserConfigurationException occured!", e);
		} catch( FileNotFoundException e ) {
			logger.error("Could not find '" + xmlFile + "'!", e);
		} catch( SAXException e ) {
			logger.error("Error during parsing XML-File!", e);
		} catch( IOException e ) {
			logger.error("IOException occured!", e);
		} catch( XPathExpressionException e ) {
			logger.error("XPathException occured!", e);
		}
		
	}
	
	
	@Override
	public Collection< DiagramInfo > getDiagramInfo() {
		return diagramInfos;
	}

	private Parameters parseParameters( Node node ) {
		Parameters params = new Parameters();
		
		NodeList nodes = node.getChildNodes();
		for( int i = 0; i < nodes.getLength(); i++ ) {
			Node n = nodes.item( i );
			if( n.getNodeName().equals( "Parameter" ) ) {
				String key = getAttributeValue(n, "key");
				String val = getAttributeValue(n, "value");
				params.put( key, val );
			}
		}
		return params;
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
}
