package de.uniol.inf.is.odysseus.viewer.view.symbol;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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

public final class XMLSymbolConfiguration implements ISymbolConfiguration {

	private static final Logger logger = LoggerFactory.getLogger( XMLSymbolConfiguration.class );
	
	private Map<String, Collection<SymbolElementInfo>> mapTypeSymbolInfos;
	private Collection<SymbolElementInfo> defaultSymbolInfos;
	
	public XMLSymbolConfiguration( String configFilename ) throws IOException {
		if( configFilename == null ) 
			throw new IllegalArgumentException( "xmlFilename is null!" );
		
		if( configFilename.length() == 0 )  
			throw new IllegalArgumentException( "xmlFilename is empty!");
		
		
		logger.info("Parsing configurationfile " + configFilename );
		
		// DATEN EINLESEN
		mapTypeSymbolInfos = new HashMap<String, Collection<SymbolElementInfo>>();
		defaultSymbolInfos = new ArrayList<SymbolElementInfo>();
		
		// VALIDATION
		SchemaFactory factory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );	
		Schema schema;
		try {
			//schema = factory.newSchema( new File( XSD_FILE ) );
			//OSGi
			URL xsd = Activator.getContext().getBundle().getEntry(Activator.XSD_SYMBOL_SCHEMA_FILE);
			schema = factory.newSchema(xsd);
		} catch( SAXException ex ) {
			logger.error( " canntot compile Schemafile " + Activator.XSD_SYMBOL_SCHEMA_FILE + "because " );
			logger.error( ex.getMessage() );
			return;
		}
		
		Validator validator = schema.newValidator();
		// Neu mit OSGi
		URL xmlFile = Activator.getContext().getBundle().getEntry(configFilename);
		logger.debug(xmlFile.toString());
		Source source = new StreamSource(xmlFile.openStream());

		
		try {
			validator.validate( source );
			
		} catch( SAXException ex ) {
			logger.error( "Configurationfile is not valid with " + Activator.XSD_SYMBOL_SCHEMA_FILE + "because " );
			logger.error( ex.getMessage() );
			return;
		} catch( IOException e ) {
			logger.error( "IOException during validating configurationFile ", e );
			return;
		}

		logger.trace( "Empty SymbolConfiguration-instance created!" );
		
		try {
			// Parsing
			DocumentBuilderFactory bfactory = DocumentBuilderFactory.newInstance();
			bfactory.setNamespaceAware( true );
			DocumentBuilder builder = bfactory.newDocumentBuilder();
			Document document = builder.parse(xmlFile.openStream());
			
			// Über XPath Images rauslesen
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			
			XPathExpression expr = xpath.compile("/Symbols/Symbol");
			NodeList nodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			for( int i = 0 ; i < nodes.getLength(); i++ ) {
				parseSymbol(nodes.item( i ));
			}
			
			expr = xpath.compile( "/Symbols/Default" );
			nodes = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
			for( int i = 0 ; i < nodes.getLength(); i++ ) {
				parseDefault(nodes.item( i ));
			}
			
			if( defaultSymbolInfos.size() == 0 )
				logger.warn("No default symbol specified!");
					
			logger.info( "File parsed successfully" );
		} catch( Exception ex ) {
			ex.printStackTrace();
			throw new IOException("Error during loading symbolConfiguration", ex );
		} 
	}
				
	private SymbolElementInfo parseSymbolElement( Node node, int width, int height ) {
		logger.trace( "Entering <SymbolElement>" );
		
		final String symbolType = getAttributeValue(node, "type");
		if( symbolType.length() == 0 ) {
			logger.warn( "type for entity 'SymbolElement' must be specified" );
		}
		
		logger.debug( "Reading SymbolInfoParameters for '" + symbolType + "'" );
		
		// Parameter auslesen
		final NodeList childNodes = node.getChildNodes();
		final Map<String,String> params = new HashMap<String,String>();
		if( childNodes.getLength() > 0 ) {
			for( int i = 0; i < childNodes.getLength(); i++ ) {
				final Node childNode = childNodes.item( i );
				if( childNode.getNodeName().equals( "Parameter" )) {
					final String key = getAttributeValue(childNode, "key");
					final String value = getAttributeValue( childNode, "value");
					if( key.length() > 0 && value.length() > 0  ) {
						logger.debug( "Adding Parameter key=" + key + " value=" + value );
						params.put( key, value );
					} else {
						logger.warn("Wrong Parameter-entity: Key=" + key + " Value=" + value);
					}
				}
			}
		} else
			logger.debug( "No parameters for '" + symbolType + "' specified" );
		
		logger.trace( "Leaving <SymbolElement>" );
		
		return new SymbolElementInfo(symbolType, params, width, height);
	}
	
	private void parseSymbol( Node node ) {
		logger.trace( "Entering <Symbol>" );
		
		final String type = getAttributeValue(node, "nodeName");
		if( type.length() == 0 ) {
			logger.warn( "nodeName for entity 'Symbol' must be specified" );
		}
		
		int width = -1;
		int height = -1;
		final Collection<SymbolElementInfo> infos = new ArrayList<SymbolElementInfo>();
		final NodeList childNodes = node.getChildNodes();
		if( childNodes.getLength() > 0 ) {
			for( int i = 0; i < childNodes.getLength(); i++  ) {
				final Node n = childNodes.item( i );
				if( n.getNodeName().equals( "Size" )  ) {
					width = Integer.valueOf( getAttributeValue( n, "width" ) );
					height = Integer.valueOf( getAttributeValue( n, "height" ) );
				} else if( n.getNodeName().equals( "SymbolElement" )  ) {
					if( width <= 0 ) {
						logger.warn( "width is non-positive!" );
					} else if ( height <= 0 ) {
						logger.warn( "height is non-positive" );
					} else {
						infos.add(parseSymbolElement(n, width, height));
					}
				}
			}
		} 
		if( infos.size() > 0 ) {
			logger.debug( "Adding SymbolInfos for Type '" + type + "'" );
			mapTypeSymbolInfos.put( type, infos );
		} else {
			logger.warn( "No symbols for type '" + type + "' specified. Using default-symbol.");
			mapTypeSymbolInfos.put( type, defaultSymbolInfos);
		}
		logger.trace( "Leaving <Symbol>" );
	}
	
	private void parseDefault( Node node ) {
		logger.trace( "Entering <Default>" );
				
		int width = -1;
		int height = -1;
		final NodeList childNodes = node.getChildNodes();
		if( childNodes.getLength() > 0 ) {
			for( int i = 0; i < childNodes.getLength(); i++  ) {
				final Node n = childNodes.item( i );
				if( n.getNodeName().equals( "Size" )  ) {
					width = Integer.valueOf( getAttributeValue( n, "width" ) );
					height = Integer.valueOf( getAttributeValue( n, "height" ) );
				} else if( n.getNodeName().equals( "SymbolElement" )  ) {
					if( width <= 0 ) {
						logger.warn( "width is non-positive!" );
					} else if ( height <= 0 ) {
						logger.warn( "height is non-positive" );
					} else {
						defaultSymbolInfos.add(parseSymbolElement(n, width, height));
					}
				}
			}
		} 
		logger.trace( "Leaving <Default>" );
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
	public Collection< SymbolElementInfo > getDefaultSymbolInfos() {
		return defaultSymbolInfos;
	}

	@Override
	public Map< String, Collection< SymbolElementInfo >> getMap() {
		return mapTypeSymbolInfos;
	}
}
