package de.uniol.inf.is.odysseus.wsenrich.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.SAXException;

public class XPathKeyFinder implements IKeyFinder {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(XPathKeyFinder.class);
	
	/**
	 * The xml-message as a string-representation
	 */
	private String message;
	
	/**
	 * The xml-document
	 */
	private Document xmlDocument;
	
	/**
	 * The searched path
	 */
	private String search;
	
	/**
	 * Element data of the searched path
	 */
	private Object value;
	
	public XPathKeyFinder() {
		//Needed for IKeyFinderRegistry
	}
	
	@Override
	public String getSearch() {
		return this.search;
	}

	@Override
	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public void setMessage(String Message, String charset) {
		this.message = Message;	
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(false);
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.xmlDocument = builder.parse(new ByteArrayInputStream(this.message.getBytes(charset)));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			logger.error("Generaly Error while parsing XML-Document. Cause: {}", e.getMessage());
		}
	}

	@Override
	public Object getValueOf(String search, boolean keyValue) {
		if(search.endsWith("]")) {
			this.value = getValueOfSingleObject(search, keyValue);
		} else {
			this.value = getValueOfMultipleObject(search, keyValue); 
		}
		return this.value;	
	}
	
	/**
	 * Searches the XML Document for the complete Content of ONE ArrayElement an writes it
	 * in the StringBuffer
	 * @param search the Element to search for
	 * @param keyValue if true, the output looks like key : value, if false only the Values
	 * of the Elementes will be returned as value, value, ...
	 * @return The Values of the given search
	 */
	private StringBuffer getValueOfSingleObject(String search, boolean keyValue) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		StringBuffer temp = new StringBuffer();
		try {
			NodeList nodeList;
			Node node = (Node) xPath.compile(search).evaluate(xmlDocument, XPathConstants.NODE);
			if(node != null) {
				nodeList = node.getChildNodes();
				for(int i = 0; null!=nodeList && i < nodeList.getLength(); i++) {
					Node nod = nodeList.item(i);
					if(nod.getNodeType() == Node.ELEMENT_NODE && keyValue) {
						temp.append(nodeList.item(i).getNodeName() + " : " + nod.getFirstChild().getNodeValue() + " ");	
					} else if(nod.getNodeType() == Node.ELEMENT_NODE && !keyValue) {
						temp.append(nod.getFirstChild().getNodeValue() + ", ");	
					}
				}
				if(temp.equals("") || temp.length() == 0 || temp == null){
					return null;
				}
				if(!keyValue && temp.length() > 2) {
					temp.delete(temp.length()-2, temp.length());
				}
				return temp;
			} else {
				return null;
			}
		} catch (XPathExpressionException e) {
			logger.error("The specified XPath-Expression is invalid! Cause: {}", e.getMessage());
			return null;
		}
	}
	
	/**
	 * Searches the Xml Document for More then ONE Array Elements or single Elements that
	 * are not present in an Array
	 * @param search the Element to search for
	 * @param keyValue if true, the output looks like key : value, if false only the Values
	 * of the Elementes will be returned as value, value, ...
	 * @return The Values of the given search
	 */
	private StringBuffer getValueOfMultipleObject(String search, boolean keyValue) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		StringBuffer temp = new StringBuffer();
		try {
			 NodeList nodeList = (NodeList) xPath.compile(search).evaluate(xmlDocument, XPathConstants.NODESET);
			 for(int i = 0; i < nodeList.getLength(); i++) {
				 	if(keyValue) {
					temp.append(nodeList.item(i).getNodeName() + " : " + nodeList.item(i).getFirstChild().getNodeValue() + " ");
				 	} else {
				 		temp.append(nodeList.item(i).getFirstChild().getNodeValue() + ", ");
				 	}
				} 
			 if(temp.equals("") || temp.length() == 0 || temp == null) {
				 return null;
			 }
			 if(!keyValue && temp.length() > 2) {
				 temp.delete(temp.length()-2, temp.length());
			 }
			 return temp;
		 } catch (XPathExpressionException e) {
			 logger.error("The specified XPath-Expression is invalid! Cause: {}", e.getMessage());
			 return null;
		}
	 }

	@Override
	public String getName() {
		return "XPATH";
	}

	@Override
	public IKeyFinder createInstance() {
		return new XPathKeyFinder();
	}
}
