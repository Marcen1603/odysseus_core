package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlInterpreter extends DefaultHandler {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(XmlInterpreter.class);
	/**
	 * The Document Locator for the Xml Document
	 */
	private Locator loc;
	
	/**
	 * Holds the Xml Elements with Element Name, Element Content, Element Datatype
	 */
	private ArrayList<XmlElement> data;
	
	/**
	 * The Element Name
	 */
	private String elementName;
	
	/**
	 * The Element Datatype
	 */
	private String elementDatatype;
	
	/**
	 * The Element Content
	 */
	private String elementContent;
	
	/**
	 * Setter for the Document Locator
	 */
	public void setDocumentLocator(Locator locator) {
		 
		loc = locator;  
	}
	 
	public void startDocument() throws SAXException {
		
		this.data = new ArrayList<XmlElement>();
	}
	  
	 public void endDocument() throws SAXException {
	 //TODO: überprüfen ob das funktioniert
		 getElements();
	 }
	 
	 public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		  	
		this.elementName = localName;
		  
		for(int i = 0; i < atts.getLength(); i++) {
			  
			this.elementContent = atts.getValue(i);
		 }
	  }
	 
	  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	  
		  //the xml element is only added, if content is not null, so
		  //only element with content will be added (no father elements with no data are ignored)
		  if(this.elementContent.length() > 0) {
			  
			 this.data.add(new XmlElement(elementName, elementContent, elementDatatype));
			 this.elementName = "";
			 this.elementContent = "";
			 this.elementDatatype = "";
		  }
	  }
	  
	  public void characters(char[] ch, int start, int length) throws SAXException
	  {		
		  String temp = (new String(ch, start, length)).trim();
		  if(temp.length() > 0) {
			  
			  this.elementContent = temp;
		  }
	
	  }
	 
	  public void warning(SAXParseException ex) {
	  
		  logger.debug("Warnings by parsing Xml Document, Row {}, Warning Message: {}", ex.getLineNumber(), ex.getMessage());
		  
	  }
	  public void error(SAXParseException ex) {
	  
		  logger.error("Exception by parsing Xml Document, Row {}, Error Message: {}", ex.getLineNumber(), ex.getMessage());
	    
	  }
	  
	  public void fatalError(SAXParseException ex) {
	  
		  logger.error("Fatal Error Exception by parsing Xml Document, Row {}, Error Message: {}", ex.getLineNumber(), ex.getMessage());
	  }
	  
	  /**
	   * @return the DocumentLocator of the Xml Document
	   */
	  public Locator getLocator() {
		  return this.loc;
	  }
	  
	  /**
	   * @return Cleaned Xml-Elemente, with the Element Name,
	   * Element Content and the Element Datatype.
	   * Element Datatype can be null, because this parameter
	   * is read throug the XML Parser and not every XML Document
	   * has declared Datatypes
	   */
	  public ArrayList<XmlElement> getElements() {
		  return this.data;
	  }
}



