package de.uniol.inf.is.odysseus.viewer.swt.resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

import de.uniol.inf.is.odysseus.viewer.Activator;

public class XMLResourceConfiguration implements IResourceConfiguration {

	private static final String XSD_FILE = "viewer_cfg/resourcesSchema.xsd";
	private static final Logger logger = LoggerFactory.getLogger( XMLResourceConfiguration.class );
	
	private Map<String, String> resources = new HashMap<String, String>();
	
	public XMLResourceConfiguration( String configFileName ) throws IOException{
		
		logger.info( "Paring resourceConfigurationfile " + configFileName  );
		
		// VALIDATION
		SchemaFactory factory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );	
		Schema schema;
		try {
			// Neu mit OSGi
			URL xsd = Activator.getContext().getBundle().getEntry(XSD_FILE);
			logger.debug(xsd.toString());
			schema = factory.newSchema(xsd);
		} catch( SAXException ex ) {
			logger.error( " canntot compile Schemafile " + XSD_FILE + "because " );
			logger.error( ex.getMessage() );
			return;
		}
		
		Validator validator = schema.newValidator();
		// Neu mit OSGi
		URL xmlFile = Activator.getContext().getBundle().getEntry(configFileName);
		logger.debug(xmlFile.toString());
		Source source = new StreamSource(xmlFile.openStream());
		
		try {
			validator.validate( source );
			
		} catch( SAXException ex ) {
			logger.error( "Resourcesfile is not valid with " + XSD_FILE + "because " );
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
			XPathExpression expr = xpath.compile("/Resources/Image");
			NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			
			for( int i = 0 ; i < nodes.getLength(); i++ ) {
				Node node = nodes.item( i );
				final String name = getAttributeValue(node, "name");
				final String src= getAttributeValue(node, "source");
				logger.debug( "Inserting resourceInfo " + src + " --> " + name );
				resources.put( name, src  );
			}
			
			logger.info( "Paring resourceConfigurationfile successful" );

		} catch( Exception ex ) {
			ex.printStackTrace();
			throw new IOException("Error during loading resource-configuration!", ex);
		}

	}
	
	@Override
	public Map< String, String > getResources() {
		return resources;
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
