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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
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

/**
 * XML Protocol Handler which transforms a complete XPath result into a key-value object and vice versa
 * 
 * @author Henrik Surm
 * 
 */
public class XMLProtocolHandler2<T extends KeyValueObject<? extends IMetaAttribute>> extends AbstractProtocolHandler<T> 
{
    public static final String NAME = "XML2";

    private static final Logger LOG = LoggerFactory.getLogger(XMLProtocolHandler2.class);
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    @SuppressWarnings("unused")
	private static DocumentBuilder db;

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
    private OutputStream output;
    private List<T> result = new LinkedList<>();

    @Override public String getName() { return NAME; }
    
    public XMLProtocolHandler2() 
    {
        super();
    }

    public XMLProtocolHandler2(final ITransportDirection direction, final IAccessPattern access, IDataHandler<T> dataHandler, OptionMap options) 
    {
        super(direction, access, dataHandler, options);        
    }

    @Override public void open() throws UnknownHostException, IOException 
    {
        getTransportHandler().open();
        if (getDirection().equals(ITransportDirection.IN)) 
        {
            if ((getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL)))
                input = getTransportHandler().getInputStream();
        }
        else {
            output = getTransportHandler().getOutputStream();
        }
    }

    @Override public void close() throws IOException 
    {
        if (getDirection().equals(ITransportDirection.IN) && input != null) 
        	input.close();
        else 
            output.close();
        
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
        System.out.println(msg);

        try {
            getTransfer().transfer(parseXml(new ByteBufferBackedInputStream(message)));
        }
        catch (IOException e) {
            XMLProtocolHandler2.LOG.error(e.getMessage(), e);
        }
    }

    private T parseXml(InputStream input) throws IOException 
    {
        // Deliver Input from former runs
        if (result.size() > 0)
            return result.remove(0);

        if (input.available() == 0)
            return null;

/*        try 
        {
            Document dom = db.parse(input);
            try {
                // DOM parser closes input stream
                input.skip(input.available());
            }
            catch (Throwable t) {
                // Nothing
            }
            
            KeyValueObject<? extends IMetaAttribute> resultObj = new KeyValueObject<>();
        }
        catch (Exception e)
        {        	
        }*/
        
        return null;
    }

    @Override
    public T getNext() throws IOException 
    {
        return parseXml(input);
    }

    @Override
    public void write(final T object) throws IOException 
    {
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
