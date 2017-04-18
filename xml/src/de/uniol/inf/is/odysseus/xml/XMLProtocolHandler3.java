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
package de.uniol.inf.is.odysseus.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern; 
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;

/**
 * XML Protocol Handler which transforms a complete xml document into a nested
 * key-value object and vice versa
 *
 * @author Henrik Surm
 * @author Marco Grawunder
 * @author Stephan Sachal
 *
 */
// TODO Rename
public class XMLProtocolHandler3<T extends XMLStreamObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T>
{
	public static final String NAME = "XML3";
	public static final String TAG_TO_STRIP = "tag_to_strip";

	private static final Logger LOG = LoggerFactory.getLogger(XMLProtocolHandler3.class);

	private InputStream input;
	private OutputStream output;
	private final List<String> xpaths = new ArrayList<String>();
	private String tagToStrip;
	private List<T> result = new LinkedList<>();
    private XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    private XMLEventReader eventReader;

	/**
	 * Create a new XML Data Handler
	 *
	 */
	public XMLProtocolHandler3()
	{
		super();
    }

    private void init_internal() {
        OptionMap options = optionsMap;
        if(options.get(TAG_TO_STRIP) != null) 
        {
			this.tagToStrip = options.get(TAG_TO_STRIP).toString();
		}
    }

	/**
	 * Create a new XML Data Handler
	 *
	 * @param direction
	 * @param access
	 * @param transfer
	 * @param dataHandler
	 * @param options
	 */
	public XMLProtocolHandler3(final ITransportDirection direction, final IAccessPattern access, IStreamObjectDataHandler<T> dataHandler, OptionMap options)
	{
		super(direction, access, dataHandler, options);
		init_internal();
	}

	@Override
	public void close() throws IOException
	{
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN))
		{
			if (this.input != null)
			{
				this.input.close();
			}
		} else
		{
			this.output.close();
		}
		this.getTransportHandler().close();
	}

	@Override
	public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access,
			final OptionMap options, final IStreamObjectDataHandler<T> dataHandler)
	{
		final XMLProtocolHandler3<T> instance = new XMLProtocolHandler3<T>(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public ITransportExchangePattern getExchangePattern()
	{
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN))
		{
			return ITransportExchangePattern.InOnly;
		} else
		{
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public T getNext() throws IOException
	{
		return parseXml(input);
	}

	private List<String> getXPaths()
	{
		return Collections.unmodifiableList(this.xpaths);
	}

	@Override
	public boolean hasNext() throws IOException
	{
        try {
            return result.size() > 0 || this.input.available() > 0;
        }
        catch (Throwable t) {
            return false;
        }		
	}

	@Override
	public void open() throws UnknownHostException, IOException
	{
		this.getTransportHandler().open();
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN))
		{
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
			{
				this.input = this.getTransportHandler().getInputStream();
				try
				{
					eventReader = inputFactory.createXMLEventReader(input);
				} catch (XMLStreamException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        
			}
		} else
		{
			this.output = this.getTransportHandler().getOutputStream();
		}
	}

	private T parseXml(InputStream input) throws IOException
	{
        if (input.available() == 0)	return null;

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = null;
        StringWriter XMLOutput = new StringWriter();
		try
		{
			while(eventReader.hasNext())
			{
				XMLEvent event = eventReader.nextEvent();
				if(event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().getLocalPart().equalsIgnoreCase(tagToStrip))
				{
					eventWriter = outputFactory.createXMLEventWriter(XMLOutput);
					eventWriter.add(event);
				}
				else if(event.getEventType() == XMLStreamConstants.END_ELEMENT && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(tagToStrip))
				{
					eventWriter.add(event);			
					eventWriter.close();
					eventWriter = null;
					return this.getDataHandler().readData(XMLOutput.toString());
				}
				else if(eventWriter != null) 
				{
					eventWriter.add(event);
				}
			}
			eventReader.close();			
		} catch (XMLStreamException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	private InputStream DocumentToInputStream(Document doc)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Source xmlSource = new DOMSource(doc);
		Result outputTarget = new StreamResult(outputStream);
		try
		{
			TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
		} catch (TransformerException | TransformerFactoryConfigurationError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	@Override
	public void process(InputStream message)
	{
		try
		{
			getTransfer().transfer(parseXml(message));
		} catch (IOException e)
		{
			XMLProtocolHandler3.LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void process(long callerId, ByteBuffer message)
	{
		String msg = new String(message.array(), Charset.forName("UTF-8"));
		System.out.println(msg);

		try
		{
			getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
		} catch (IOException e)
		{
			XMLProtocolHandler3.LOG.error(e.getMessage(), e);
		}
	}

	private void setXPaths(final List<String> xpaths)
	{
		this.xpaths.clear();
		this.xpaths.addAll(xpaths);
	}

	@Override
	public void write(final T object) throws IOException
	{
		/*
		 * final StringBuilder sb = new StringBuilder(); sb.append("<tuple>");
		 * if (prettyprint) sb.append("\n"); final SDFSchema schema =
		 * this.getDataHandler().getSchema(); for (int i = 0; i < schema.size();
		 * i++) { final String attr = schema.get(i).getAttributeName();
		 * sb.append("<").append(attr).append(">");
		 * sb.append(object.getAttribute(i).toString());
		 * sb.append("</").append(attr).append(">"); if (prettyprint)
		 * sb.append("\n"); } sb.append("</tuple>"); if (prettyprint)
		 * sb.append("\n"); this.output.write(sb.toString().getBytes());
		 */
		this.output.write(object.toString(true).getBytes());
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
