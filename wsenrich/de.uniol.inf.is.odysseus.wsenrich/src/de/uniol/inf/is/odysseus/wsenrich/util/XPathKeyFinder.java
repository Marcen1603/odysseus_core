package de.uniol.inf.is.odysseus.wsenrich.util;

import org.w3c.dom.Document;
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
	
	private String message;
	private Document xmlDocument;
	private String search;
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
	public void setMessage(String Message) {
		this.message = Message;	
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(false);
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.xmlDocument = builder.parse(new ByteArrayInputStream(this.message.getBytes()));
		} catch (ParserConfigurationException e) {
			//TODO
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Object getValueOf(String search, boolean keyValue) {
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		StringBuffer temp = new StringBuffer();
		try {
			NodeList nodeList = (NodeList) xPath.compile(search).evaluate(xmlDocument, XPathConstants.NODESET);
			if(nodeList.getLength() == 0) {
				return null;  //No item found or the XPath expression is wrong
			}
			if(keyValue) {
				for(int i = 0; i < nodeList.getLength(); i++) {
					temp.append(nodeList.item(i).getNodeName() + " : " + nodeList.item(i).getFirstChild().getNodeValue() + ", ");
				} 
				temp.delete(temp.length()-2, temp.length());
			} else {
				for(int i = 0; i < nodeList.getLength(); i++) {
					temp.append(nodeList.item(i).getFirstChild().getNodeValue() + ", ");
				}
				temp.delete(temp.length()-2, temp.length());
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.value = temp;
		return this.value;
	}

	@Override
	public String getName() {
		return "XPath";

	}

	@Override
	public IKeyFinder createInstance() {
		return new XPathKeyFinder();
	}

}
