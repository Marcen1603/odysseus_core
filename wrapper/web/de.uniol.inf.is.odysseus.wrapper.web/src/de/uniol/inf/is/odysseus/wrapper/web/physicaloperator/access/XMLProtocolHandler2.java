/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;

//import org.apache.commons.collections15.MultiMap;

/**
 * XML Protocol Handler which transforms a complete xml document into a nested key-value object and vice versa
 * 
 * @author Henrik Surm
 * 
 */
public class XMLProtocolHandler2<T extends KeyValueObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> 
{
    public static final String NAME = "XML2";

    private static final Logger LOG = LoggerFactory.getLogger(XMLProtocolHandler2.class);
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder db;
	private static TransformerFactory tf = TransformerFactory.newInstance();
    
    static
    {
    	try {
			db = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOG.error("Error while executing static block", e);
			e.printStackTrace();
		}
    }
    
    private InputStream input;
    private BufferedWriter output;
    private Transformer transformer;
    private List<T> result = new LinkedList<>();

    @Override public String getName() { return NAME; }
    
    public XMLProtocolHandler2() 
    {
        super();
    }

    public XMLProtocolHandler2(final ITransportDirection direction, final IAccessPattern access, IDataHandler<T> dataHandler, OptionMap options) 
    {
        super(direction, access, dataHandler, options);
        
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");        
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    }

    @Override public void open() throws UnknownHostException, IOException 
    {
        getTransportHandler().open();
        if (getDirection().equals(ITransportDirection.IN)) 
        {
            if ((getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
                input = getTransportHandler().getInputStream();
        }
        else 
        {
        	if ((getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
        		output = new BufferedWriter(new OutputStreamWriter(getTransportHandler().getOutputStream()));
        }
    }

    @Override public void close() throws IOException 
    {
        if (getDirection().equals(ITransportDirection.IN) && input != null)
        {
        	if (input != null)
        		input.close();
        }
        else 
        {
        	if (output != null)
        		output.close();
        }
        
        getTransportHandler().close();
    }

    @Override public boolean hasNext() throws IOException 
    {
        try {
            return result.size() > 0 || this.input.available() > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override public void process(InputStream message) 
    {
        try 
        {
            getTransfer().transfer(parseXml(message));
        }
        catch (IOException e) {
            XMLProtocolHandler2.LOG.error(e.getMessage(), e);
        }
    }

    @Override public void process(long callerId, ByteBuffer message) 
    {
        String msg = new String(message.array(), Charset.forName("UTF-8"));

        try {
            getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
        }
        catch (IOException e) {
            XMLProtocolHandler2.LOG.error(e.getMessage(), e);
        }
    }

	@SuppressWarnings("unchecked")
	public static void traverseNode(Node node, MultiMap<String, Object> targetMap)
	{
		if (node instanceof Text && node.getNodeValue().trim().equals("")) 
			return;

		String key = node.getNodeName();
		Object value = null;

		NodeList nodes = node.getChildNodes();
		if (nodes.getLength() == 0)
			value = node.getNodeValue();
		else
		if (nodes.getLength() == 1 && nodes.item(0) instanceof Text)
			value = nodes.item(0).getNodeValue();
		else
		{
			MultiMap<String, Object> map = new MultiHashMap<String, Object>(); 
			for (int i=0; i<nodes.getLength(); i++)
				traverseNode(nodes.item(i), map);
			
			value = map;
		}
		
		NamedNodeMap attributes = node.getAttributes();
		if (attributes.getLength() > 0)
		{
			MultiMap<String, Object> map;
			
			if (value instanceof String)
			{
				map = new MultiHashMap<String, Object>();
				map.put((String)null, value);
				value = map;
			}
			else
			if (value instanceof MultiMap)
			{				
				map = (MultiMap<String, Object>)value;
			}
			else
				throw new RuntimeException("asd");
			
			for (int i=0; i<attributes.getLength(); i++)
				map.put("Attribute@" + attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
		}				
		targetMap.put(key, value);
	}

	private void mapDepthFirstSearch(NestedKeyValueObject<? extends IMetaAttribute> kvObject, String root, MultiHashMap<String, Object> map)
	{
		for (Entry<String, Collection<Object>> e : map.entrySet())
		{
			String key = e.getKey();
			Collection<Object> values = e.getValue();
			
			String newRoot;
			if (root != null) 
				newRoot = root + "." + key;
			else
				newRoot = key;
									
			for (Object obj : values)
			{
				if (obj instanceof MultiHashMap)
					mapDepthFirstSearch(kvObject, newRoot,(MultiHashMap) obj);
				else
					kvObject.setAttribute(newRoot, obj);
			}
		}
	}
    
	
    @SuppressWarnings("unchecked")
	private T parseXml(InputStream input) throws IOException 
    {
        // Deliver Input from former runs
        if (result.size() > 0)
            return result.remove(0);

        if (input.available() == 0)
            return null;

        try 
        {
            Document doc = db.parse(input);
            try {
                // DOM parser closes input stream
                input.skip(input.available());
            }
            catch (Throwable t) {
                // Nothing
            }
            
/*        	DOMSource domSource = new DOMSource(doc);        	
        	StringWriter strWriter = new StringWriter();
   			transformer.transform(domSource, new StreamResult(strWriter));
   			String str = strWriter.toString();*/
            
   			MultiHashMap<String, Object> targetMap = new MultiHashMap<>();
   			traverseNode(doc.getDocumentElement().getChildNodes().item(1), targetMap);
   			
            NestedKeyValueObject<? extends IMetaAttribute> resultObj = new NestedKeyValueObject<>();
            mapDepthFirstSearch(resultObj, null, targetMap);            
            result.add((T) resultObj);
           	return result.size() > 0 ? result.remove(0) : null;
        }
        catch (Exception e)
        {        	
        }
        return null;
    }

    @Override
    public T getNext() throws IOException 
    {
        return parseXml(input);
    }

    @SuppressWarnings("unchecked")
	private void addListToNode(Document doc, Node root, String itemName, List<Object> list)
    {
    	for (Object obj : list)
    	{    	
    		Element node = doc.createElement(itemName);
    		
    		if (obj instanceof Map)
    			addMapToNode(doc, node, (Map<String, Object>) obj);
    		if (obj instanceof List)
    			throw new UnsupportedOperationException("A list may not contain a list. Use a map inbetween!");
    		else
    			node.appendChild(doc.createTextNode(obj.toString()));    		
    	}
    }
    
    @SuppressWarnings("unchecked")
	private void addMapToNode(Document doc, Node root, Map<String, Object> map)
    {
    	for (Entry<String, Object> e : map.entrySet())
    	{    	
    		Object value = e.getValue();
    		
    		if (value instanceof List)
    		{
    			addListToNode(doc, root, e.getKey(), (List<Object>) value);
    		}
    		else
    		{
    			Element node = doc.createElement(e.getKey());
    			if (value instanceof Map)
    				addMapToNode(doc, node, (Map<String, Object>) value);
    			else
    				node.appendChild(doc.createTextNode(value.toString()));
    			
    			root.appendChild(node);
    		}
    	}
    }
    
    @Override
    public void write(final T object) throws IOException 
    {
    	Map<String, Object> map = object.getAttributes();
    	
    	Document doc = db.newDocument();
    	Element root = doc.createElement("root");
    	addMapToNode(doc, root, map);
    	doc.appendChild(root);
    	DOMSource domSource = new DOMSource(doc);
    	
    	StringWriter strWriter = new StringWriter();
    	
    	try {
			transformer.transform(domSource, new StreamResult(strWriter));
			String str = strWriter.toString();
			
			if (output != null)
				output.write(str);
			else
				getTransportHandler().send(str.getBytes("UTF-8"));
		} catch (TransformerException e) {
			throw new IOException("Exception while transforming XML", e);
		}
    }

    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IDataHandler<T> dataHandler) {
        final XMLProtocolHandler2<T> instance = new XMLProtocolHandler2<T>(direction, access, dataHandler, options);
        return instance;
    }

    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        else {
            return ITransportExchangePattern.OutOnly;
        }
    }

    @Override
    public void onConnect(final ITransportHandler caller) {

    }

    @Override
    public void onDisonnect(final ITransportHandler caller) {

    }

    @Override
    public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
        if (!(o instanceof XMLProtocolHandler2))
            return false;
        
        return true;
    }
}
