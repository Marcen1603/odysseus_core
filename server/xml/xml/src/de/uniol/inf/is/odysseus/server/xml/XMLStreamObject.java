package de.uniol.inf.is.odysseus.server.xml;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;

public class XMLStreamObject<T extends IMetaAttribute> extends AbstractStreamObject<T> implements INamedAttributeStreamObject<T>, Serializable {

	private static final Logger log = LoggerFactory.getLogger(XMLStreamObject.class);
	private static final long serialVersionUID = 4868112466855659283L;

	public static final String SLASH_REPLACEMENT_STRING = "__SLASH_PLACEHOLDER__";
	public static final String AT_REPLACEMENT_STRING = "__AT_PLACEHOLDER__";
	public static final String LEFT_BRACE_REPLACEMENT_STRING = "__LEFT_BRACE_PLACEHOLDER__";
	public static final String RIGHT_BRACE_REPLACEMENT_STRING = "__RIGHT_BRACE_PLACEHOLDER__";

	private static XPathFactory factory = XPathFactory.newInstance();
	
	private Document content;

	
	/** Creates a new XMLStreamObject from a {@link Document}. **/
	public static XMLStreamObject<IMetaAttribute> createInstance(Document doc) throws XPathFactoryConfigurationException {
		return new XMLStreamObject<>(doc);
	}

	/** Creates a new XMLStreamObject from a {@link Node}. **/
	public static XMLStreamObject<IMetaAttribute> createInstance(Node node) throws XPathFactoryConfigurationException {
		try {
			
			Document newDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Node copyNode = newDoc.importNode(node, true);
			
			if (copyNode.getNodeType() == Node.ELEMENT_NODE) {
				newDoc.appendChild(copyNode);
			} else if (copyNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				Attr attr = (Attr) copyNode;
				attr.getName();
				Element root = newDoc.createElement(attr.getName());
				root.appendChild(newDoc.createTextNode(attr.getValue()));
				newDoc.appendChild(root);
			}
			
			return new XMLStreamObject<>(newDoc);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** Creates a new XMLStreamObject from a list of XPath expressions. **/
	public static XMLStreamObject<IMetaAttribute> createInstance(List<String> paths) {
		
		final String REGEX1 = "\\{|\\}";
		
		try {
			
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			for (String targetXpath : paths) {
				// Remove curly braces
				targetXpath = targetXpath.replaceAll(REGEX1, "");
				// Split path to retrieve single tokens
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
			
			return XMLStreamObject.createInstance(document);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (XPathFactoryConfigurationException e) {
			e.printStackTrace();
		}
			
		return null;
	}

	/** Merges two XMLStreamObjects by specified target path. **/
	public static XMLStreamObject<IMetaAttribute> merge(XMLStreamObject<?> left, XMLStreamObject<?> right, String path)
			throws XPathFactoryConfigurationException, XPathExpressionException {
		
		XMLStreamObject<IMetaAttribute> result = new XMLStreamObject<IMetaAttribute>(left.getDocument());
		Node node = result.getNode(path);
		if (node != null) {
			Node appendNode = left.getDocument().importNode(
					right.getDocument().getFirstChild().cloneNode(true), true
			);
			node.appendChild(appendNode);
		}
		
		return result;
	}

	@Override
	public String toString() {
		return toString(true);
	}

	@Override
	public String toString(boolean handleMetadata) {
		StringBuilder ret = new StringBuilder();
		ret.append(xPathToString("/node()"));
		if (handleMetadata && getMetadata() != null) {
			ret.append(";").append(getMetadata().toString());
		}
		return ret.toString();
	}

	public XMLStreamObject() {}
	
	private XMLStreamObject(Document xml) throws XPathFactoryConfigurationException {
		setDocument(xml);
	}

	@SuppressWarnings("unchecked")
	private XMLStreamObject(XMLStreamObject<T> obj) {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer tx;
		try {
			tx = tfactory.newTransformer();
			DOMSource source = new DOMSource(obj.getDocument());
			DOMResult result = new DOMResult();
			tx.transform(source, result);
			content = (Document) result.getNode();
			this.setMetadata((T) obj.getMetadata().clone());
		} catch (TransformerException e) {
			log.error("Could not copy DOM", e);
			e.printStackTrace();
		}
	}

	protected void setDocument(Document xml) {
		content = xml;
	}

	public Document getDocument() {
		return content;
	}

	@Override
	public AbstractStreamObject<T> clone() {
		return new XMLStreamObject<>(this);
	}

	@Override
	public AbstractStreamObject<T> newInstance() {
		return new XMLStreamObject<>();
	}

	public boolean isEmpty() {
		try {
			Node check = content.getFirstChild();
			check.getNodeName();
			return false;
		} catch (NullPointerException e) {
			return true;
		}
	}

	public static boolean hasParent(NodeList nl, Node node) {
		Node parent = node.getParentNode();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) == parent) {
				return true;
			}
		}
		return false;
	}

	public static NodeList getNodeList(String expression, Document document) throws XPathExpressionException {
		return (NodeList) factory.newXPath().compile(expression).evaluate(document, XPathConstants.NODESET);
	}
	
	public NodeList getNodeList(String expression) throws XPathExpressionException {
		return (NodeList) factory.newXPath().compile(expression).evaluate(content, XPathConstants.NODESET);
	}
	
	public Node getNode(String expression) throws XPathExpressionException {
		return (Node) factory.newXPath().compile(expression).evaluate(content, XPathConstants.NODE);
	}
	
	public NodeList getValueFromExpression(String expression) {
		try {
			String atomicValue = "<?xml version=\"1.0\"?><atomicResult>" + factory.newXPath().compile(expression).evaluate(content) + "</atomicResult>";
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(atomicValue)));
			return (NodeList) factory.newXPath().compile("//node()").evaluate(doc, XPathConstants.NODESET);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String xPathToString(String expression) {
		
		StringWriter buf = new StringWriter();
		try {
			
			NodeList nl = getNodeList(expression);
			Transformer xform = TransformerFactory.newInstance().newTransformer();
			xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			xform.setOutputProperty(OutputKeys.INDENT, "yes");
			xform.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			for (int i = 0; i < nl.getLength(); i++) {
				Node elem = nl.item(i);// Your Node
				xform.transform(new DOMSource(elem), new StreamResult(buf));
			}
			
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		} finally {
			try {
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buf.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(String name) {
		try {
			
			name = name.replaceAll(SLASH_REPLACEMENT_STRING, "/");
			name = name.replaceAll(AT_REPLACEMENT_STRING, "@");
			
			// Retrieve the node list
			NodeList nl = getNodeList(name);
			
			if (nl.getLength() > 1) {
				return null;
			}
			
			// Retrieve node value
			String result = nl.item(0).getTextContent();
			
			// If the result is empty, return null
			if ("".equals(result)) {
				return null;
			}
			
			return (K) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> path(String path) {
		try {
			return (List<Object>) getNode(path);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	@Override
	public void setAttribute(String name, Object value) {
		
		final String REGEX1 = "\\{|\\}";
		String path = name.replaceAll(REGEX1, "");
		String realValue = value.toString();
		
		try {
			NodeList nodes = getNodeList(path);
			if (nodes.item(0) instanceof Attr) {
				((Attr) nodes.item(0)).setNodeValue(realValue);
			} else if (nodes.item(0) instanceof Element) {
				((Element) nodes.item(0)).setTextContent(realValue);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}