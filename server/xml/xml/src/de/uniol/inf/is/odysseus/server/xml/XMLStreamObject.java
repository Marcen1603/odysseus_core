package de.uniol.inf.is.odysseus.server.xml;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.INamedAttributeStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

// GetNext nicht vorgeschrieben im Protocolhandler - ACCESS benutzt es aber

public class XMLStreamObject<T extends IMetaAttribute> extends AbstractStreamObject<T> implements INamedAttributeStreamObject<T>, Serializable
{
	protected static final Logger LOG = LoggerFactory.getLogger(XMLStreamObject.class);
	private static final long serialVersionUID = 4868112466855659283L;
	private static XPath xpath;

	private Document content;

	public static XMLStreamObject<IMetaAttribute> createInstance(Document doc)
	{
		return new XMLStreamObject<>(doc);
	}

	@Override
	public String toString()
	{
		return toString(true);
	}

	@Override
	public String toString(boolean handleMetadata)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(xpathToString("/node()"));
		if (handleMetadata && getMetadata() != null)
			ret.append(";").append(getMetadata().toString());
		return ret.toString();
	}

	private XMLStreamObject(Document _content)
	{
		xpath = XPathFactory.newInstance().newXPath();
		content = _content;
		// System.out.println("XMLStreamObject Constructor");
	}

	public XMLStreamObject()
	{
	}

	private XMLStreamObject(XMLStreamObject<T> obj)
	{
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer tx;
		try
		{
			tx = tfactory.newTransformer();
			DOMSource source = new DOMSource(obj.getDocument());
			DOMResult result = new DOMResult();
			tx.transform(source, result);
			content = (Document) result.getNode();
		} catch (TransformerException e)
		{
			LOG.error("Could not copy DOM", e);
			e.printStackTrace();
		}
	}

	private void setDocument(Document doc)
	{
		content = doc;
	}

	public Document getDocument()
	{
		return content;
	}

	@Override
	public AbstractStreamObject<T> clone()
	{
		return new XMLStreamObject<>(this);
	}

	@Override
	public AbstractStreamObject<T> newInstance()
	{
		return new XMLStreamObject<>();
	}

	public boolean isEmpty()
	{
		try
		{
			Node check = content.getFirstChild();
			check.getNodeName();
			return false;
		} catch(NullPointerException e)
		{
			return true;
		}
	}

	public Document xpathToDocument(String expression)
	{
		try
		{
			NodeList nodelist = xpathToNodeList(expression);
			Document newDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			if(nodelist.getLength()>0)
			{
				Node node = nodelist.item(0);
				Node copyNode = newDoc.importNode(node, true);
				newDoc.appendChild(copyNode);
			}
			return newDoc;
		} catch (ParserConfigurationException e)
		{
			LOG.error("Returning Document from xpath failed", e);
			e.printStackTrace();
		}
		return null;
	}

	public NodeList xpathToNodeList(String expression)
	{
		try
		{
			XPathExpression xExp = xpath.compile(expression);			
			return (NodeList) xExp.evaluate(content, XPathConstants.NODESET);
		} catch (XPathExpressionException e)
		{
			XPathExpression xExp;
			try
			{
				xExp = xpath.compile(expression);
				String atomicValue = "<?xml version=\"1.0\"?><atomicResult>" + xExp.evaluate(content) + "</atomicResult>";
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();				
				Document doc = docBuilder.parse(new InputSource(new StringReader(atomicValue)));
				xpath.reset();
				xExp = xpath.compile("//node()");
				return (NodeList) xExp.evaluate(doc, XPathConstants.NODESET);				
			} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e1)
			{
				e1.printStackTrace();
			}			
			LOG.error("XPathExpression Error", e);
			e.printStackTrace();
		}
		return null;
	}
	
	/*public Node xpathToNode(String expression)
	{
		try
		{
			XPathExpression xExp = xpath.compile(expression);
			return (Node) xExp.evaluate(content, XPathConstants.NODE);
		} catch (XPathExpressionException e)
		{
			LOG.error("XPathExpression Error", e);
			e.printStackTrace();
		}
		return null;
	}*/

	public String xpathToString(String expression)
	{
		NodeList nl = xpathToNodeList(expression);
		StringWriter buf = new StringWriter();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node elem = nl.item(i);// Your Node
			try
			{
				Transformer xform = TransformerFactory.newInstance().newTransformer();
				xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
				xform.setOutputProperty(OutputKeys.INDENT, "yes");
				xform.transform(new DOMSource(elem), new StreamResult(buf));
			} catch (TransformerException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buf.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getAttribute(String name)
	{
		return (K) xpathToNodeList(name).item(0).getNodeName();
	}
}