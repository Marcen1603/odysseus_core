package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlKeyFinder {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(XmlKeyFinder.class);
	
	/**
	 * The parsing-object for the xml-document
	 */
	private XMLStreamReader parser;
	
	/**
	 * The value of the searched xml-document
	 */
	private Object value;
	
	/**
	 * Constructor for the Xml Parser
	 * @param input the xml-document as a input source
	 */
	public XmlKeyFinder(InputStream input) {
				
		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			this.parser = factory.createXMLStreamReader(input);
			
		} catch (XMLStreamException e) {
			
			logger.error("Exception by parsing XML-Document: {}", e.getMessage());
		}
		
	}
	
	/**
	 * search the xml document for the specified name
	 * @param elementName the name of the element to search for
	 * @return the value of the searched xml document
	 */
	public Object getElementByName(String elementName) {
		
		boolean found = false;

		try {
			while(parser.hasNext()) {
				
				switch (parser.getEventType()) {
				
				case XMLStreamConstants.START_ELEMENT:
					if(parser.getLocalName().equals(elementName)) {
						found = true;
						parser.next();
					}
					
				case XMLStreamConstants.CHARACTERS:
					if(found) {
						if(!parser.isWhiteSpace()) {
							this.value = parser.getText();
							found = false;
							return this.value;
						}
						
					}
				}
				parser.next();
			} 
		} catch (XMLStreamException e) {
			e.printStackTrace();
			
		}
		return this.value;
		
		
	}
	
	/**
	 * @return the value of the searched xml-document
	 */
	public Object getValue() {
		return this.value;
	}

}
