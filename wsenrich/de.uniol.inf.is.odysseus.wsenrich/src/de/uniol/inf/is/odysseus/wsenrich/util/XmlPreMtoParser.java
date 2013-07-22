package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlPreMtoParser {
	
	private Document xmlDocument;
	private ArrayList<Document> splittetXmlDocuments;
	
	public XmlPreMtoParser() {
		this.splittetXmlDocuments = new ArrayList<Document>();
	}
	
	protected void setMessage(Document xmlDocument) {
		this.xmlDocument = xmlDocument;
		splitDocuments();
	}
	
	private void splitDocuments() {
		this.splittetXmlDocuments.clear();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();			
			//Get the children of the root element
			NodeList list = xmlDocument.getDocumentElement().getChildNodes();
			for(int i = 0; i < list.getLength(); i++) {
				//Get the subelements that are not single Textelements
				if(list.item(i).getChildNodes().getLength() > 1) {
					Document doc = docBuilder.newDocument();
					//Get the root element
					Node root = xmlDocument.getDocumentElement();
					Node copyOfRoot = doc.importNode(root, false);
					doc.appendChild(copyOfRoot);
					Node element = (Node) list.item(i);
					Node copyOfChildren = doc.importNode(element, true);
					doc.getDocumentElement().appendChild(copyOfChildren);
					this.splittetXmlDocuments.add(doc);
				}
				
			}
			for(int i = 0; i < splittetXmlDocuments.size(); i++) {
				for(int j = 0; j < list.getLength(); j++) {
					if(list.item(j).getChildNodes().getLength() == 1) {
							Element element = (Element) list.item(j);
							Node copyOfChildren = splittetXmlDocuments.get(i).importNode(element, true);
							splittetXmlDocuments.get(i).getDocumentElement().appendChild(copyOfChildren);
						}
				} 
			} 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public ArrayList<Document> getXmlFragments() {
		return this.splittetXmlDocuments;
	}

}
