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
package de.uniol.inf.is.odysseus.server.xml;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * XML Protocol Handler which transforms a complete xml document into a nested
 * key-value object and vice versa.
 *
 * @author Henrik Surm
 * @author Marco Grawunder
 *
 */
public class XMLProtocolHandler2<T extends KeyValueObject<? extends IMetaAttribute>>
		extends AbstractProtocolHandler<T> {
	public static final String NAME = "XML2";
    public static final String ROOTELEMENTNAME = "rootElementName";

	private static final Logger LOG = LoggerFactory.getLogger(XMLProtocolHandler2.class);
	private static TransformerFactory tf = TransformerFactory.newInstance();

	private InputStream input;
	private BufferedWriter output;
	private Transformer transformer;
	private List<T> result = new LinkedList<>();
	private String rootElementName;

	@Override
	public String getName() {
		return NAME;
	}

	public XMLProtocolHandler2() {
		super();
	}

	public XMLProtocolHandler2(final ITransportDirection direction, final IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap options) {
		super(direction, access, dataHandler, options);

		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
		rootElementName = options.getString(ROOTELEMENTNAME, null);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		setDone(false);
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
				input = getTransportHandler().getInputStream();
		} else {
			if ((getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
				output = new BufferedWriter(new OutputStreamWriter(getTransportHandler().getOutputStream()));
		}
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN) && input != null) {
			if (input != null)
				input.close();
		} else {
			if (output != null)
				output.close();
		}

		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		try {
			return result.size() > 0 || this.input.available() > 0;
		} catch (Throwable t) {
			if (t instanceof IOException) {
				setDone(true);
			}
			return false;
		}
	}

	@Override
	public void process(InputStream message) {
		try {
			getTransfer().transfer(parseXml(message));
		} catch (IOException e) {
			XMLProtocolHandler2.LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		try {
			getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
		} catch (IOException e) {
			XMLProtocolHandler2.LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	public void process(String message) {
		try {
			getTransfer().transfer(parseXml(new ByteArrayInputStream(message.getBytes())));
		} catch (IOException e) {
			XMLProtocolHandler2.LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(String[] message) {
		try {
			getTransfer().transfer(parseXml(new ByteArrayInputStream(String.join("", message).getBytes())));
		} catch (IOException e) {
			XMLProtocolHandler2.LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private T parseXml(InputStream input) throws IOException {

		// Deliver Input from former runs
		if (result.size() > 0) {
			return result.remove(0);
		}

		if (input.available() == 0) {
			return null;
		}

		try {
			KeyValueObject<IMetaAttribute> resultObj = KeyValueObject.createFromXML(input);

			result.add((T) resultObj);
			return result.size() > 0 ? result.remove(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public T getNext() throws IOException {
		return parseXml(input);
	}

	@SuppressWarnings("unchecked")
	private void addListToNode(Document doc, Node root, String itemName, List<Object> list) {
		for (Object obj : list) {
			Element node = doc.createElement(itemName);
			root.appendChild(node);

			if (obj instanceof Map)
				addMapToNode(doc, node, (Map<String, Object>) obj);
			if (obj instanceof List)
				throw new UnsupportedOperationException("A list may not contain a list. Use a map inbetween!");
			else
				node.appendChild(doc.createTextNode(obj.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	private void addMapToNode(Document doc, Node root, Map<String, Object> map) {
		for (Entry<String, Object> e : map.entrySet()) {
			Object value = e.getValue();

			if (value instanceof List) {
				addListToNode(doc, root, e.getKey(), (List<Object>) value);
			} else {
				Element node = doc.createElement(e.getKey());
				if (value instanceof Map) {
					addMapToNode(doc, node, (Map<String, Object>) value);
				} else {
					node.appendChild(doc.createTextNode(value.toString()));
				}
				root.appendChild(node);
			}
		}
	}

	@Override
	public void write(final T object) throws IOException {

		try {
			String xml = object.getAsXML();
			if(rootElementName!=null) {
				xml = xml.replaceAll("ObjectNode", rootElementName);
			}
			if (output != null) {
				output.write(xml);
			} else {
				getTransportHandler().send(xml.getBytes("UTF-8"));
			}
		} catch (Exception e) {
			throw new IOException("Exception while transforming to XML", e);
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access,
			final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
		final XMLProtocolHandler2<T> instance = new XMLProtocolHandler2<T>(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof XMLProtocolHandler2))
			return false;

		return true;
	}
}
