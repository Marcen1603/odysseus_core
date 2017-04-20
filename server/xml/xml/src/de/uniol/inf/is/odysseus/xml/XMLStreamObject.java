package de.uniol.inf.is.odysseus.xml;

import java.io.Serializable;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

// GetNext nicht vorgeschrieben im Protocolhandler - ACCESS benutzt es aber

public class XMLStreamObject<T extends IMetaAttribute> extends AbstractStreamObject<T> implements Serializable
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
		if(handleMetadata && getMetadata()!=null) ret.append(";").append(getMetadata().toString());
		return ret.toString();
	}

	private XMLStreamObject(Document _content)
	{
		xpath = XPathFactory.newInstance().newXPath();
		content = _content;
		System.out.println("XMLStreamObject Constructor");
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
		content = (Document)result.getNode();
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
		return xpathToNode("/*") == null ? false : true;
	}

	public Document xpathToDocument(String expression)
	{
		try
		{
			NodeList nodelist = xpathToNodeList(expression);
			Document newDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	        for (int i = 0; i < nodelist.getLength(); i++)
	        {
	            Node node = nodelist.item(i);
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
			LOG.error("XPathExpression Error", e);
			e.printStackTrace();
		}
		return null;
	}

	public Node xpathToNode(String expression)
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
	}

	public String xpathToString(String expression)
	{
		try
		{
			XPathExpression xExp = xpath.compile(expression);
			return  xExp.evaluate(content);
		} catch (XPathExpressionException e)
		{
			LOG.error("XPathExpression Error", e);
			e.printStackTrace();
		}
		return null;
	}

}