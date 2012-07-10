/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.swt.resource;

import java.io.IOException;
import java.net.URL;

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

import org.eclipse.jface.resource.ImageRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;

public class XMLResourceLoader {

	private static final Logger logger = LoggerFactory.getLogger( XMLResourceLoader.class );
	
	public static void loadImages( URL xmlFile, URL xsd, ImageRegistry imageRegistry ) {

//		logger.info( "Paring resourceConfigurationfile " + xmlFile  );
		
		// VALIDATION
		SchemaFactory factory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );	
		Schema schema;
		try {
			schema = factory.newSchema(xsd);
		} catch( SAXException ex ) {
			logger.error( " canntot compile Schemafile " + xsd  + "because " );
			logger.error( ex.getMessage() );
			return;
		}
		
		
		try {
			Validator validator = schema.newValidator();
			Source source = new StreamSource(xmlFile.openStream());
			validator.validate( source );
			
		} catch( SAXException ex ) {
			logger.error( "Resourcesfile is not valid with " + xsd  + "because " );
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
//				logger.debug( "Inserting resourceInfo " + src + " --> " + name );
				
				try {
					imageRegistry.put(name, OdysseusRCPViewerPlugIn.getImageDescriptor(src));
				} catch( Exception ex ) {
					logger.error("Exception while loading image " + src + ":", ex);
				}
			}
			
//			logger.info( "Paring resourceConfigurationfile successful" );

		} catch( Exception ex ) {
			ex.printStackTrace();
			logger.error("Error during loading resource-configuration!", ex);
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

}
