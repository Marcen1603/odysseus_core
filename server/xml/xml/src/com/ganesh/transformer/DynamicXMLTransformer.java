/*
Taken from: 
https://github.com/TheGanesh/DynamicXMLTransformer/blob/master/src/main/java/com/ganesh/transformer/DynamicXMLTransformer.java
*/
package com.ganesh.transformer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class DynamicXMLTransformer {

	
	private static Document addElemtbypath(List<String> pathList1, List<String> elementList) {

		Document doc = null;
		try {
			DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docfactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Tree");
			doc.appendChild(rootElement);

			for (int i = 0; i < pathList1.size(); i++) {
				String pathList = pathList1.get(i);
				String element = elementList.get(i);

				List<String> xpath = new LinkedList<>();
				String[] parts = pathList.split("/");
				for (int j = 0; j < parts.length; j++) {
					xpath.add(parts[j]);

				}
				// System.out.println(xpath);

				Node node = doc.getDocumentElement();
				Document dom = node.getOwnerDocument();
				// System.out.println(node);

				for (int k = 0; k < xpath.size(); k++) {
					XPath xPath = XPathFactory.newInstance().newXPath();
					NodeList nodes = (NodeList) xPath.evaluate(xpath.get(k), doc.getDocumentElement(),
							XPathConstants.NODESET);

					if (nodes.getLength() != 0) {
						// node = (Node) nodes; // getting class cast exception
						continue;

					} else {
						try {
							if (xpath.get(k).contains("@")) {
//								node.appendChild(dom.createAttribute(xpath.get(k)));
							} 
							node = node.appendChild(dom.createElement(xpath.get(k)));
						} catch (org.w3c.dom.DOMException e) {
							System.err.println("help");
						}
					}
				}
				node.appendChild(dom.createTextNode(element));
				// System.out.println(nodes);
				// System.out.println(dom);
				// System.out.println(node);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        
        	// print result
        try {
	        	TransformerFactory tf = TransformerFactory.newInstance();
	        	Transformer transformer = tf.newTransformer();
	        	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        	transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(new DOMSource(doc), 
			     new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return doc;

    }
	
//	public static void main(String[] args) {
//		
//		
//		String[] paths = { 
//				"Product/Organization/RegisteredDetail/something", 
//				"Product/Organization/RegisteredDetail",
//				"Product/Organization/RegisteredDetail/anything/nothing", 
//				"Some/OtherPath/",
//				"cosem/ldevs/@smgw_id;/cosem/ldevs/ldev/objects/object/attributes/attribute[@id=\\'1\\']/value/logical_nam"
//				};
//		
//		String[] elements = {
//				"product1", 
//				"product2", 
//				"product3", 
//				"p4",
//				"pppeis"
//				};
//		
//		System.out.println(DynamicXMLTransformer.addElemtbypath(Arrays.asList(paths), Arrays.asList(elements)));
////			DynamicXMLTransformer.foo();
//		
//	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		DynamicXMLTransformer.getDoc();
		
    }
	
    public static Document transformXML(Document xml, Document xslt) throws TransformerException, ParserConfigurationException, FactoryConfigurationError {

        Source xmlSource = new DOMSource(xml);
        Source xsltSource = new DOMSource(xslt);
        DOMResult result = new DOMResult();

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        

        trans.transform(xmlSource, result);

        Document resultDoc = (Document) result.getNode();

        return resultDoc;
    }
	
	public static void foo() throws ParserConfigurationException {
		
		String[] paths = { "Product/Organization/RegisteredDetail/something", "Product/Organization/RegisteredDetail",
				"Product/Organization/RegisteredDetail/anything/nothing", "Some/OtherPath" };
		
		String[] elements = {"product1", "product2"};

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		
        Node item = null;
        Element rootElement = document.createElement("tree");
//        item = (Node) document.createElement("bookingID");
//        item.appendChild(document.createTextNode("115"));
//        rootElement.appendChild(item);
        
		
        Node root = document.appendChild(rootElement);

		for (int i = 0; i < paths.length; i++) {
			addElementByPath(root, paths[i], paths[i]);
		}
		
		System.out.println(root.toString());
	}
	
	private static void addElementByPath(Node parent, String path, String value) {
		
		  Node node = parent;
		  String[] parts = path.split("\\/");
		  Document dom = parent.getOwnerDocument();
		  Node existingNode = null;
		  
		  XPath xPath = XPathFactory.newInstance().newXPath();

		  for (int i = 0; i < parts.length; i++) {
			   
			  try {
				  existingNode = (Node) xPath.evaluate(parts[i], node, XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			  
			 if (existingNode != null) {
				 node = existingNode;
			 } else {
				 node = (Node) node.appendChild(dom.createElement(parts[i]));
			 }
			  
//		    existingNode = dom.evaluate(
//		      parts[i], node, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null
//		    ).singleNodeValue;
//		    
//		    if (existingNode) {
//		      node = existingNode;
//		    } else {
//		      node = node.appendChild(dom.createElement(parts[i]));
//		    }
//		    
		  }
		  
		  node.appendChild(dom.createTextNode(value));
		
	}

	private Document createTargetDocument(Map<String, String> mappings) throws Exception {

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		for (Entry<String, String> entry : mappings.entrySet()) {

			String targetXpath = entry.getKey();

			String[] tokens = targetXpath.split("/");
			List<String> tokensList = new ArrayList<String>();
			for (String token : tokens) {
				if (token != null & !token.trim().equals(""))
					tokensList.add(token.trim());
			}

			String previousXpath = "";
			Element parentElement = null;

			for (String token : tokensList) {

				String currentXpath = previousXpath + "/" + token;

				if (token.contains("[")) {

					String neededNode = token.split("\\[")[0];
					String indexStr = token.split("\\[")[1].split("\\]")[0];

					if (indexStr.contains("@")) {
						indexStr = "1";
					}
					
					Integer index = Integer.valueOf(indexStr.trim());
					NodeList nodes = getNodeList(previousXpath + "/" + neededNode, document);

					int resultCount = nodes.getLength();

					if (resultCount < index) {

						Element currentParent = null;
						for (int i = resultCount + 1; i <= index; i++) {
							Element el = document.createElement(neededNode);
							parentElement.appendChild(el);

							if (i == index) {
								currentParent = el;
							}
						}

						parentElement = currentParent;
					} else {

						NodeList nodeList = getNodeList(currentXpath, document);
						parentElement = (Element) nodeList.item(0);
					}

				} else {

					NodeList nodes = getNodeList(currentXpath, document);
					int resultCount = nodes.getLength();

					if (resultCount > 0) {
						if (nodes.item(0) instanceof Element) {
							parentElement = (Element) nodes.item(0);
						}
					} else {

						if (token.contains("@")) {
							Attr attr = document.createAttribute(token.replace("@", "").trim());
							parentElement.setAttributeNode(attr);
						} else {

							if (parentElement == null) {
								Element root = document.createElement(token);
								document.appendChild(root);
								parentElement = root;
							} else {
								Element el = document.createElement(token);
								parentElement.appendChild(el);
								parentElement = el;
							}
						}
					}

				}
				previousXpath = currentXpath;
			}
		}
		return document;
	}

	public Document transform(Map<String, String> finalMappings) throws Exception {

		//Document targetDoc = createTargetDocument(finalMappings);

		Document targetDoc = null;
	

		for (Entry<String, String> entry : finalMappings.entrySet()) {

			String path = entry.getKey();
			String value = entry.getValue();

			NodeList nodes = getNodeList(path, targetDoc);

			if (nodes.item(0) instanceof Attr) {

				((Attr) nodes.item(0)).setNodeValue(value);

			} else if (nodes.item(0) instanceof Element) {

				((Element) nodes.item(0)).setTextContent(value);

			}
		}
		return targetDoc;
	}
	
	public static Document getDoc() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		String xsl = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\r\n" + 
				"<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\r\n" + 
				"  <xs:element name=\"cosem\">\r\n" + 
				"    <xs:complexType>\r\n" + 
				"      <xs:sequence>\r\n" + 
				"        <xs:element name=\"ldevs\">\r\n" + 
				"          <xs:complexType>\r\n" + 
				"            <xs:sequence>\r\n" + 
				"              <xs:element name=\"ldev\" maxOccurs=\"unbounded\" minOccurs=\"0\">\r\n" + 
				"                <xs:complexType>\r\n" + 
				"                  <xs:sequence>\r\n" + 
				"                    <xs:element name=\"objects\">\r\n" + 
				"                      <xs:complexType>\r\n" + 
				"                        <xs:sequence>\r\n" + 
				"                          <xs:element name=\"object\">\r\n" + 
				"                            <xs:complexType>\r\n" + 
				"                              <xs:sequence>\r\n" + 
				"                                <xs:element name=\"attributes\">\r\n" + 
				"                                  <xs:complexType>\r\n" + 
				"                                    <xs:sequence>\r\n" + 
				"                                      <xs:element name=\"attribute\" maxOccurs=\"unbounded\" minOccurs=\"0\">\r\n" + 
				"                                        <xs:complexType>\r\n" + 
				"                                          <xs:sequence>\r\n" + 
				"                                            <xs:element name=\"value\">\r\n" + 
				"                                              <xs:complexType>\r\n" + 
				"                                                <xs:sequence>\r\n" + 
				"                                                  <xs:element type=\"xs:string\" name=\"logical_name\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:float\" name=\"value\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:byte\" name=\"scaler\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:byte\" name=\"unit\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:byte\" name=\"status\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:short\" name=\"capture_time\" minOccurs=\"0\"/>\r\n" + 
				"                                                  <xs:element type=\"xs:string\" name=\"type\" minOccurs=\"0\"/>\r\n" + 
				"                                                </xs:sequence>\r\n" + 
				"                                              </xs:complexType>\r\n" + 
				"                                            </xs:element>\r\n" + 
				"                                          </xs:sequence>\r\n" + 
				"                                          <xs:attribute type=\"xs:byte\" name=\"id\" use=\"optional\"/>\r\n" + 
				"                                        </xs:complexType>\r\n" + 
				"                                      </xs:element>\r\n" + 
				"                                    </xs:sequence>\r\n" + 
				"                                  </xs:complexType>\r\n" + 
				"                                </xs:element>\r\n" + 
				"                              </xs:sequence>\r\n" + 
				"                              <xs:attribute type=\"xs:byte\" name=\"class-id\" use=\"optional\"/>\r\n" + 
				"                              <xs:attribute type=\"xs:string\" name=\"id\" use=\"optional\"/>\r\n" + 
				"                              <xs:attribute type=\"xs:byte\" name=\"version\" use=\"optional\"/>\r\n" + 
				"                            </xs:complexType>\r\n" + 
				"                          </xs:element>\r\n" + 
				"                        </xs:sequence>\r\n" + 
				"                        <xs:attribute type=\"xs:byte\" name=\"count\" use=\"optional\"/>\r\n" + 
				"                      </xs:complexType>\r\n" + 
				"                    </xs:element>\r\n" + 
				"                  </xs:sequence>\r\n" + 
				"                  <xs:attribute type=\"xs:string\" name=\"id\" use=\"optional\"/>\r\n" + 
				"                </xs:complexType>\r\n" + 
				"              </xs:element>\r\n" + 
				"            </xs:sequence>\r\n" + 
				"            <xs:attribute type=\"xs:byte\" name=\"count\"/>\r\n" + 
				"            <xs:attribute type=\"xs:string\" name=\"smgw_id\"/>\r\n" + 
				"          </xs:complexType>\r\n" + 
				"        </xs:element>\r\n" + 
				"      </xs:sequence>\r\n" + 
				"    </xs:complexType>\r\n" + 
				"  </xs:element>\r\n" + 
				"</xs:schema>" +
				"</xsl:stylesheet>";
		
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document targetDoc = db.parse(new ByteArrayInputStream(xsl.getBytes()));
        
        Source xmlSource = new DOMSource();
        Source xsltSource = new DOMSource(targetDoc);
        DOMResult result = new DOMResult();

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        

        trans.transform(xmlSource, result);
        
        Document resultDoc = (Document) result.getNode();
        
        System.out.println(resultDoc.getChildNodes().toString());
        
        LSSerializer serializer = ((DOMImplementationLS) resultDoc.getImplementation()).createLSSerializer();
        System.out.println(serializer.writeToString(resultDoc));
        
        return resultDoc;
	}

	public Map<String, String> generateCompleteXpaths(Map<String, String> mappings, Document doc) throws Exception {

		Map<String, String> finalMappings = new LinkedHashMap<String, String>();

		for (Entry<String, String> entry : mappings.entrySet()) {

			String targetXpath = entry.getKey();
			String sourceXpath = entry.getValue();

			if (sourceXpath.contains("[")) {
				Map<String, String> subMap = getArrayXPaths(targetXpath, sourceXpath, doc);
				finalMappings.putAll(subMap);

			} else {
				String sourceValue = getElementValue(sourceXpath, doc);
				if (sourceValue != null && !sourceValue.trim().equals("")) {
					finalMappings.put(targetXpath, sourceValue);
				}
			}

		}
		return finalMappings;
	}

	private Map<String, String> getArrayXPaths(String targetXpath, String sourceXpath, Document doc) throws Exception {

		Map<String, String> subMap = new LinkedHashMap<String, String>();

		if (sourceXpath.contains("[a]")) {

			String xpath = sourceXpath.split("\\[a\\]")[0];
			NodeList nodeList = getNodeList(xpath, doc);
			if (nodeList != null) {

				String finalSourceXpath = null;
				String finalTargetXpath = null;

				for (int i = 1; i <= nodeList.getLength(); i++) {
					finalSourceXpath = sourceXpath.replace("[a]", "[" + i + "]");
					finalTargetXpath = targetXpath.replace("[a]", "[" + i + "]");
					if (!finalSourceXpath.contains("[b]")) {
						String sourceValue = getElementValue(finalSourceXpath, doc);
						if (sourceValue != null && !sourceValue.trim().equals("")) {
							subMap.put(finalTargetXpath, sourceValue);
						}
					} else {
						String iteratedXPathB = finalSourceXpath.split("\\[b\\]")[0];
						NodeList nodeListB = getNodeList(iteratedXPathB, doc);

						if (nodeListB != null) {
							for (int j = 1; j <= nodeListB.getLength(); j++) {
								String sourceXPath = finalSourceXpath.replace("[b]", "[" + j + "]");
								String targetXPath = finalTargetXpath.replace("[b]", "[" + j + "]");
								String sourceValue = getElementValue(sourceXPath, doc);
								if (sourceValue != null && !sourceValue.trim().equals("")) {
									subMap.put(targetXPath, sourceValue);
								}
							}
						}
					}
				}
			}
		}

		return subMap;
	}

	private NodeList getNodeList(String currentXpath, Document document) throws Exception {

		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expression = xpath.compile(currentXpath);
		Object result = expression.evaluate(document, XPathConstants.NODESET);

		NodeList nodeList = (NodeList) result;

		return nodeList;

	}

	private String getElementValue(String xPath, Document document) throws Exception {

		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expression = xpath.compile(xPath);
		String value = expression.evaluate(document);

		return value;
	}

}